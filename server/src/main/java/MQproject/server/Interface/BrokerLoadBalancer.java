package MQproject.server.Interface;

import MQproject.server.Model.Data.LoadBalancerResponse;

import java.util.ArrayList;
import java.util.HashMap;

public interface BrokerLoadBalancer {
    ArrayList<LoadBalancerResponse> balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                     Integer deadBrokerId);
    ArrayList<LoadBalancerResponse> balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                     Integer bornBrokerId);
    ArrayList<LoadBalancerResponse> balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                        Integer bornPartitionId);
    ArrayList<LoadBalancerResponse> balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                        Integer deadPartitionId);
                       
}
