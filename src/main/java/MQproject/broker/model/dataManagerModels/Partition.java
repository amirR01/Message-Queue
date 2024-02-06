package MQproject.broker.model.dataManagerModels;

public class Partition {
    public int partitionId;
    public int leaderBrokerId;
    public int replicaBrokerId;
    public int headIndex;
    public boolean isReplica = false;
    public String partitionsAddress;

    public Partition(int partitionId, int leaderBrokerId, int replicaBrokerId, int headIndex, boolean isReplica, String partitionsAddress) {
        this.partitionId = partitionId;
        this.leaderBrokerId = leaderBrokerId;
        this.replicaBrokerId = replicaBrokerId;
        this.headIndex = headIndex;
        this.isReplica = isReplica;
        this.partitionsAddress = partitionsAddress;
    }
}
