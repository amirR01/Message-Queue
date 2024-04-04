package MQproject.broker.model.dataManagerModels;

public class Partition {
    public Integer partitionId;
    public Integer leaderBrokerId;
    public Integer replicaBrokerId;
    public Integer headIndex;
    public boolean isReplica = false;
    public String partitionsAddress;

    public Partition(Integer partitionId, Integer leaderBrokerId, Integer replicaBrokerId, Integer headIndex, boolean isReplica, String partitionsAddress) {
        this.partitionId = partitionId;
        this.leaderBrokerId = leaderBrokerId;
        this.replicaBrokerId = replicaBrokerId;
        this.headIndex = headIndex;
        this.isReplica = isReplica;
        this.partitionsAddress = partitionsAddress;
    }
}
