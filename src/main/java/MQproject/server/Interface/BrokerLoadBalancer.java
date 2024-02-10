package MQproject.server.Interface;
// Situations:
// 1. Broker registers
// 2. Broker dies
// 3. New partition gets added.
// any others?

import MQproject.server.Model.Data.LoadBalancerResponse;

import java.util.ArrayList;
import java.util.HashMap;

public interface BrokerLoadBalancer {
    public ArrayList<LoadBalancerResponse> balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                                     Integer deadBrokerId);
    public ArrayList<LoadBalancerResponse> balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                     Integer bornBrokerId);
    public ArrayList<LoadBalancerResponse> balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                        Integer bornPartitionId);
    public ArrayList<LoadBalancerResponse> balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                        Integer deadPartitionId);
                       
}
