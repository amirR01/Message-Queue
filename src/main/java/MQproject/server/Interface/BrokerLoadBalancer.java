package MQproject.server.Interface;

// Situations:
// 1. Broker registers
// 2. Broker dies
// 3. New partition gets added.
// any others?

import java.util.ArrayList;
import java.util.HashMap;

public interface BrokerLoadBalancer {
    public void balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                     Integer deadBrokerId);
    public void balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                     HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                     Integer bornBrokerId);
    public void balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions,
                                        Integer bornPartitionId);
    public void balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions,
                                        HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartition,
                                        Integer deadPartitionId);
}
