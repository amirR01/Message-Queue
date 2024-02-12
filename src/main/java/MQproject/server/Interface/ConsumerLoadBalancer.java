package MQproject.server.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConsumerLoadBalancer {
    void balanceOnConsumerDeath(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer deadConsumerId);
    void balanceOnConsumerBirth(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions,
                                       ArrayList<Integer> allPartitions ,Integer bornConsumerId);
    void balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer bornPartitionId);
    Integer balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer deadPartitionId);
}
