package MQproject.server.Implementation;

import MQproject.server.Interface.BrokerLoadBalancer;
import MQproject.server.Model.data.LoadBalancerResponse;
import MQproject.server.Model.data.LoadBalancerResponseAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrokerLoadBalancerImpl implements BrokerLoadBalancer {
    public ArrayList<LoadBalancerResponse> balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                Integer deadBrokerId) {
        //TODO
        return null;
    }

    public ArrayList<LoadBalancerResponse> balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                                HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                                Integer bornBrokerId) {
        //TODO
        return null;
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
                LoadBalancerResponseAction.ADD_PARTITION
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


    private void addPartition() {
    }
    private void removePartition() {

    }
    private void clonePartition() {

    }
    private void movePartition() {

    }
}
