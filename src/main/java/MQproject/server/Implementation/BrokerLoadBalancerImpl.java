package MQproject.server.Implementation;

import MQproject.server.Interface.BrokerLoadBalancer;
import MQproject.server.Model.Data.LoadBalancerResponse;
import MQproject.server.Model.Data.LoadBalancerResponseAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrokerLoadBalancerImpl implements BrokerLoadBalancer {
    public ArrayList<LoadBalancerResponse> balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                Integer deadBrokerId) {
        ArrayList<LoadBalancerResponse> responses = new ArrayList<>();

        ArrayList<Integer> deadBrokerLeaders = brokerIdToLeaderPartitions.remove(deadBrokerId);
        ArrayList<Integer> deadBrokerReplicas = brokerIdToReplicaPartitions.remove(deadBrokerId);

        // giving leadership to the replicas of dead leaders
        HashMap<Integer, Integer> partitionIdToReplicaBroker = getPartitionIdToBroker(brokerIdToReplicaPartitions);

        for (int leaderId : deadBrokerLeaders) {
            int replicaBrokerId = partitionIdToReplicaBroker.get(leaderId);
            responses.add(new LoadBalancerResponse(
                    replicaBrokerId,
                    leaderId,
                    true,
                    LoadBalancerResponseAction.BECOME_PARTITION_LEADER
            ));
            // remove id from brokerIdToReplicaPartitions and add it to brokerIdToLeaderPartitions
            brokerIdToReplicaPartitions.get(replicaBrokerId).remove(Integer.valueOf(leaderId));
            ArrayList<Integer> leaderPartitions = brokerIdToLeaderPartitions.get(replicaBrokerId);
            leaderPartitions.add(leaderId);
            brokerIdToLeaderPartitions.put(replicaBrokerId, leaderPartitions);
        }

        // distributing replicas
        HashMap<Integer, Integer> partitionIdToLeaderBroker = getPartitionIdToBroker(brokerIdToLeaderPartitions);

        ArrayList<Integer> missingReplicas = new ArrayList<>();
        missingReplicas.addAll(deadBrokerReplicas);
        missingReplicas.addAll(deadBrokerLeaders);

        // distribute replicas in a greedy way: give as much as possible to the broker which can accept the most
        ArrayList<Integer> sortedBrokers = sortBrokersByLoad(brokerIdToReplicaPartitions, false);
        for (int replicaId : missingReplicas) {
            int leaderBrokerId = partitionIdToLeaderBroker.get(replicaId);
            int targetReplicaBrokerId = -1;
            for (int brokerId : sortedBrokers) {
                if (brokerId != leaderBrokerId) {
                    targetReplicaBrokerId = brokerId;
                    break;
                }
            }
            responses.add(new LoadBalancerResponse(
                    leaderBrokerId,
                    targetReplicaBrokerId,
                    false,
                    LoadBalancerResponseAction.CLONE_PARTITION
            ));
            brokerIdToReplicaPartitions.get(targetReplicaBrokerId).add(replicaId);
        }

        return responses;
    }

    public ArrayList<LoadBalancerResponse> balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                Integer bornBrokerId) {

        ArrayList<LoadBalancerResponse> responses = new ArrayList<>();

        ArrayList<Integer> bornBrokerLeaders = new ArrayList<>();
        ArrayList<Integer> bornBrokerReplicas = new ArrayList<>();

        // leaders
        int mostLeaderLoadedBrokerId = getMostLoadedBroker(brokerIdToLeaderPartitions);
        ArrayList<Integer> mostLeaderLoadedBrokerPartitions = brokerIdToLeaderPartitions.get(mostLeaderLoadedBrokerId);
        int numLeadersToMove = mostLeaderLoadedBrokerPartitions.size() / 2;
        for (int i = 0; i < numLeadersToMove; i++) {
            int leaderId = mostLeaderLoadedBrokerPartitions.remove(0);
            bornBrokerLeaders.add(leaderId);
            responses.add(new LoadBalancerResponse(
                    mostLeaderLoadedBrokerId,
                    bornBrokerId,
                    leaderId,
                    false,
                    LoadBalancerResponseAction.MOVE_PARTITION
            ));
        }

        // replicas
        HashMap<Integer, Integer> partitionIdToLeaderBroker = getPartitionIdToBroker(brokerIdToLeaderPartitions);

        int mostReplicaLoadedBrokerId = getMostLoadedBroker(brokerIdToReplicaPartitions);
        ArrayList<Integer> mostReplicaLoadedBrokerPartitions = brokerIdToReplicaPartitions.get(mostReplicaLoadedBrokerId);
        int numReplicasToMove = mostReplicaLoadedBrokerPartitions.size() / 2;
        for (int i = 0; i < mostReplicaLoadedBrokerPartitions.size() || bornBrokerReplicas.size() < numReplicasToMove; i++) {
            int replicaId = mostReplicaLoadedBrokerPartitions.get(i);
            if (!bornBrokerLeaders.contains(replicaId)) {
                bornBrokerReplicas.add(replicaId);
                int leaderBrokerId = partitionIdToLeaderBroker.get(replicaId);
                responses.add(new LoadBalancerResponse(
                        leaderBrokerId,
                        bornBrokerId,
                        replicaId,
                        false,
                        LoadBalancerResponseAction.CLONE_PARTITION
                ));
                responses.add(new LoadBalancerResponse(
                        mostReplicaLoadedBrokerId,
                        replicaId,
                        true,
                        LoadBalancerResponseAction.REMOVE_PARTITION
                ));
            }
        }
        mostReplicaLoadedBrokerPartitions.removeAll(bornBrokerReplicas);

        brokerIdToLeaderPartitions.put(bornBrokerId, bornBrokerLeaders);
        brokerIdToReplicaPartitions.put(bornBrokerId, bornBrokerReplicas);

        return responses;
    }

    public ArrayList<LoadBalancerResponse> balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                   HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                   Integer bornPartitionId) {
        // TODO ECIDE: USED OR NOT?
        return null;
    }

    public ArrayList<LoadBalancerResponse> balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                   HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                   Integer bornPartitionId) {
        ArrayList<LoadBalancerResponse> responses = new ArrayList<>();
        // Find the least loaded broker for leaders
        int leastLeaderLoadedBrokerId = getLeastLoadedBroker(brokerIdToLeaderPartitions);

        // set it to be the leader of the new partition
        ArrayList<Integer> leastLeaderLoadedBrokerPartitions = brokerIdToLeaderPartitions.get(leastLeaderLoadedBrokerId);
        leastLeaderLoadedBrokerPartitions.add(bornPartitionId);
        // leastLeaderLoadedBrokerId, bornPartitionId, message = addPartition
        responses.add(new LoadBalancerResponse(
                leastLeaderLoadedBrokerId,
                bornPartitionId,
                false,
                LoadBalancerResponseAction.ADD_PARTITION
        ));


        ArrayList<Integer> sortedBrokers = sortBrokersByLoad(brokerIdToReplicaPartitions, false);

        int leastReplicaLoadedBrokerId = -1;
        for (Integer brokerId : sortedBrokers) {
            if (brokerId != leastLeaderLoadedBrokerId) {
                leastReplicaLoadedBrokerId = brokerId;
                break;
            }
        }

        // Set it to be the replica of the new partition
        ArrayList<Integer> leastReplicaLoadedBrokerPartitions = brokerIdToReplicaPartitions.get(leastReplicaLoadedBrokerId);
        leastReplicaLoadedBrokerPartitions.add(bornPartitionId);
        responses.add(new LoadBalancerResponse(
                leastLeaderLoadedBrokerId,
                leastReplicaLoadedBrokerId,
                bornPartitionId,
                false,
                LoadBalancerResponseAction.CLONE_PARTITION
        ));
        return responses;
    }

    private int getLeastLoadedBroker(HashMap<Integer, ArrayList<Integer>> brokerPartitions) {
        int minPartitions = Integer.MAX_VALUE;
        int leastLoadedBrokerId = -1;
        for (Map.Entry<Integer, ArrayList<Integer>> entry : brokerPartitions.entrySet()) {
            int numPartitions = entry.getValue().size();
            if (numPartitions < minPartitions) {
                minPartitions = numPartitions;
                leastLoadedBrokerId = entry.getKey();
            }
        }
        return leastLoadedBrokerId;
    }

    private int getMostLoadedBroker(HashMap<Integer, ArrayList<Integer>> brokerPartitions) {
        int maxPartitions = Integer.MIN_VALUE;
        int mostLoadedBrokerId = -1;
        for (Map.Entry<Integer, ArrayList<Integer>> entry : brokerPartitions.entrySet()) {
            int numPartitions = entry.getValue().size();
            if (numPartitions > maxPartitions) {
                maxPartitions = numPartitions;
                mostLoadedBrokerId = entry.getKey();
            }
        }
        return mostLoadedBrokerId;
    }
    private ArrayList<Integer> sortBrokersByLoad(HashMap<Integer, ArrayList<Integer>> brokerPartitions, boolean reverse) {
        // Create a list of broker IDs
        ArrayList<Integer> brokerIds = new ArrayList<>(brokerPartitions.keySet());

        // Sort the broker IDs based on the number of partitions they have
        brokerIds.sort((brokerId1, brokerId2) -> {
            int load1 = brokerPartitions.getOrDefault(brokerId1, new ArrayList<>()).size();
            int load2 = brokerPartitions.getOrDefault(brokerId2, new ArrayList<>()).size();
            return reverse ? Integer.compare(load2, load1) : Integer.compare(load1, load2);
        });

        return brokerIds;
    }
    private HashMap<Integer, Integer> getPartitionIdToBroker(HashMap<Integer, ArrayList<Integer>> brokerIdToPartitions) {
        HashMap<Integer, Integer> partitionIdToBroker = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<Integer>> entry : brokerIdToPartitions.entrySet()) {
            int brokerId = entry.getKey();
            ArrayList<Integer> leaderIds = entry.getValue();
            for (Integer leaderId : leaderIds) {
                partitionIdToBroker.put(leaderId, brokerId);
            }
        }
        return partitionIdToBroker;
    }
}
