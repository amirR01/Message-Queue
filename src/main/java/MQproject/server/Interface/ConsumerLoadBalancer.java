package MQproject.server.Interface;

// Situations:
// 1. Consumer subscribes
// 2. Consumer Unsubscribes
// any others?

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConsumerLoadBalancer {
    public void balanceOnDeath(HashMap<Integer, ArrayList<Integer>> consumerPartitions, Integer deadConsumerId);
    public void balanceOnBirth(HashMap<Integer, ArrayList<Integer>> consumerPartitions, Integer bornConsumerId);
    public void balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> consumerPartitions, Integer bornPartitionId);
    public void balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> consumerPartitions, Integer deadPartitionId);
}
