package MQproject.server.Implementation;
import MQproject.server.Interface.ConsumerLoadBalancer;

import java.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ConsumerLoadBalancerImpl implements ConsumerLoadBalancer {
    public void balanceOnConsumerDeath(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer deadConsumerId) {
        // Remove partitions of dead consumer
        ArrayList<Integer> partitionsToRemove = consumerIdToPartitions.get(deadConsumerId);
        consumerIdToPartitions.remove(deadConsumerId);

        // Find consumer with the least partitions
        int leastLoadedConsumerId = getLeastLoadedConsumer(consumerIdToPartitions);

        // Add partitions of dead consumer to least loaded consumer
        ArrayList<Integer> leastLoadedConsumerPartitions = consumerIdToPartitions.get(leastLoadedConsumerId);
        leastLoadedConsumerPartitions.addAll(partitionsToRemove);
    }

    public void balanceOnConsumerBirth(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer bornConsumerId) {
        // Find consumer with most partitions
        int mostLoadedConsumerId = getMostLoadedConsumer(consumerIdToPartitions);

        // Divide partitions of busiest consumer in half
        ArrayList<Integer> partitionsToMove = new ArrayList<>();
        ArrayList<Integer> mostLoadedConsumerPartitions = consumerIdToPartitions.get(mostLoadedConsumerId);
        int numPartitionsToMove = mostLoadedConsumerPartitions.size() / 2;
        for (int i = 0; i < numPartitionsToMove; i++) {
            partitionsToMove.add(mostLoadedConsumerPartitions.remove(0));
        }

        // Assign partitions to the new consumer
        consumerIdToPartitions.put(bornConsumerId, partitionsToMove);
    }
    public void balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer deadPartitionId) {
        // Find consumer with most partitions
        int mostLoadedConsumerId = getMostLoadedConsumer(consumerIdToPartitions);

        // Remove dead partition from its consumer
        for (Map.Entry<Integer, ArrayList<Integer>> entry : consumerIdToPartitions.entrySet()) {
            ArrayList<Integer> partitions = entry.getValue();
            if (partitions.contains(deadPartitionId)) {
                partitions.remove(deadPartitionId);
                if (entry.getKey() != mostLoadedConsumerId) {
                    // Swap dead partition with one from most loaded consumer
                    ArrayList<Integer> mostLoadedConsumerPartitions = consumerIdToPartitions.get(mostLoadedConsumerId);
                    int swapIndex = ThreadLocalRandom.current().nextInt(mostLoadedConsumerPartitions.size());
                    int swappedPartition = mostLoadedConsumerPartitions.get(swapIndex);
                    partitions.add(swappedPartition);
                    mostLoadedConsumerPartitions.remove(swapIndex);
                }
                break;
            }
        }
    }

    public Integer balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions, Integer bornPartitionId) {
        // Find consumer with least partitions
        int leastLoadedConsumerId = getLeastLoadedConsumer(consumerIdToPartitions);
        // Add born partition to least loaded consumer
        ArrayList<Integer> leastLoadedConsumerPartitions = consumerIdToPartitions.get(leastLoadedConsumerId);
        leastLoadedConsumerPartitions.add(bornPartitionId);

        return leastLoadedConsumerId;
    }

    private int getLeastLoadedConsumer(HashMap<Integer, ArrayList<Integer>> consumerPartitions) {
        int minPartitions = Integer.MAX_VALUE;
        int leastLoadedConsumerId = -1;
        for (Map.Entry<Integer, ArrayList<Integer>> entry : consumerPartitions.entrySet()) {
            int numPartitions = entry.getValue().size();
            if (numPartitions < minPartitions) {
                minPartitions = numPartitions;
                leastLoadedConsumerId = entry.getKey();
            }
        }
        return leastLoadedConsumerId;
    }

    private int getMostLoadedConsumer(HashMap<Integer, ArrayList<Integer>> consumerPartitions) {
        int maxPartitions = Integer.MIN_VALUE;
        int mostLoadedConsumerId = -1;
        for (Map.Entry<Integer, ArrayList<Integer>> entry : consumerPartitions.entrySet()) {
            int numPartitions = entry.getValue().size();
            if (numPartitions > maxPartitions) {
                maxPartitions = numPartitions;
                mostLoadedConsumerId = entry.getKey();
            }
        }
        return mostLoadedConsumerId;
    }

}
