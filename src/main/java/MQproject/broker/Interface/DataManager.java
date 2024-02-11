package MQproject.broker.Interface;


import MQproject.broker.Implementation.Tuple;
import MQproject.broker.model.dataManagerModels.Partition;

public interface DataManager {
    public void addMessage(String message, int partitionId, boolean isReplica);

    public String readMessage(int partitionId);

    public void addPartition(Integer partitionId, Integer leaderBrokerId, Integer replicaBrokerId, String partitionData, Integer headIndex, boolean isReplica);

    public Integer getReplicaBrokerId(int partitionId);

    public Tuple<Partition, String> getPartition(Integer partitionId);

    public void updateHeadIndex(Integer partitionId, Integer headIndex);

    public Integer getHeadIndex(Integer partitionId);

    public void removePartition(int partitionId);

    public void makePartitionPrimary(int partitionId);
}
