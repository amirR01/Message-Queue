package MQproject.broker.model.dataManagerModels;

public class Partition {
    public int partitionId;
    public int headIndex;

    public boolean isReplica = false;

    public String partitionsAddress;

    public Partition(int partitionId, int headIndex, boolean isReplica, String partitionsAddress) {
        this.partitionId = partitionId;
        this.headIndex = headIndex;
        this.isReplica = isReplica;
        this.partitionsAddress = partitionsAddress;
    }
}
