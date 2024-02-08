package MQproject.broker.Interface;


import MQproject.broker.Implementation.Tuple;
import MQproject.broker.model.dataManagerModels.Partition;

public interface DataManager {
    public void addMessage(String message, int partitionId, Boolean isReplica);

    public String readMessage(int partitionId);

    public void addPartition(int partitionId, int leaderBrokerId, int replicaBrokerId, String partitionData, int headIndex, boolean isReplica);

    public Integer getReplicaBrokerId(int partitionId);

    public Tuple<Partition, String> getPartition(Integer partitionId);

    public void updateHeadIndex(Integer partitionId, Integer headIndex);

    public Integer getHeadIndex(Integer partitionId);

    public void removePartition(int partitionId);

    public void makePartitionPrimary(int partitionId);
}
