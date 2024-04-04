package MQproject.broker.Interface;

import MQproject.broker.model.message.BrokerBrokerMessage;
import MQproject.broker.model.message.BrokerClientMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BrokerBrokerService {
    public void sendHeadIndicesToReplicasAsync(List<Integer> changedPartitions);

    public void sendDataAsyncToTheReplica(BrokerClientMessage.BrokerClientSmallerMessage smallerMessage);

    public void updateReplicasData(BrokerBrokerMessage message);

    public void updatePartitionsHeadIndex(BrokerBrokerMessage message);

    public void receivePartitionAndBecomeLeader(BrokerBrokerMessage message);

    public void receivePartitionAndBecomeReplica(BrokerBrokerMessage message);
}
