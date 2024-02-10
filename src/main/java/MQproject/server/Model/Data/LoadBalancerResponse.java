package MQproject.server.Model.Data;

import MQproject.server.Model.Data.LoadBalancerResponseAction;

public class LoadBalancerResponse {
    Integer sourceBrokerId;
    Integer destinationBrokerId;
    Integer partitionId;
    boolean isReplica;
    LoadBalancerResponseAction action;

    public LoadBalancerResponse(Integer sourceBrokerId, Integer partitionId, boolean isReplica, LoadBalancerResponseAction action) {
        this.sourceBrokerId = sourceBrokerId;
        this.partitionId = partitionId;
        this.action = action;
        this.isReplica = isReplica;
    }
    public LoadBalancerResponse(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId, LoadBalancerResponseAction action) {
        this.sourceBrokerId = sourceBrokerId;
        this.destinationBrokerId = destinationBrokerId;
        this.partitionId = partitionId;
        this.action = action;
    }
    
    public Integer getSourceBrokerId() {
        return sourceBrokerId;
    }
    public Integer getDestinationBrokerId() {
        return destinationBrokerId;
    }
    public Integer getPartitionId() {
        return partitionId;
    }
    public boolean isReplica() {
        return isReplica;
    }
    public LoadBalancerResponseAction getAction() {
        return action;
    }
}
