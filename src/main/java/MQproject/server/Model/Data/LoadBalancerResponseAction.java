package MQproject.server.Model.Data;

public enum LoadBalancerResponseAction {
    ADD_PARTITION, // brokerId, newPartitionId, false
    MOVE_PARTITION, // sourceBrokerId, DestinationBrokerId, partitionId, false
    CLONE_PARTITION, // sourceBrokerId, DestinationBrokerId, partitionId, false
    REMOVE_PARTITION, // brokerId, partitionId, true
    BECOME_PARTITION_LEADER; // brokerId, partitionId, true
}
