package MQproject.server.Model.data;

public enum LoadBalancerResponseAction {
    ADD_PARTITION, // brokerId, newPartitionId
    MOVE_PARTITION, // sourceBrokerId, DestinationBrokerId, partitionId
    CLONE_PARTITION, // sourceBrokerId, DestinationBrokerId, partitionId
    REMOVE_PARTITION, // brokerId, partitionId
    BECOME_PARTITION_LEADER; // brokerId, partitionId
}
