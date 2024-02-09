package MQproject.server.Model.data;

public class LoadBalancerResponse {
    Integer sourceBrokerId;
    Integer destinationBrokerId;
    Integer partitionId;
    LoadBalancerResponseAction action;

    public LoadBalancerResponse(Integer sourceBrokerId, Integer partitionId, LoadBalancerResponseAction action) {
        this.sourceBrokerId = sourceBrokerId;
        this.partitionId = partitionId;
        this.action = action;
    }
    public LoadBalancerResponse(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId, LoadBalancerResponseAction action) {
        this.sourceBrokerId = sourceBrokerId;
        this.destinationBrokerId = destinationBrokerId;
        this.partitionId = partitionId;
        this.action = action;
    }
}
