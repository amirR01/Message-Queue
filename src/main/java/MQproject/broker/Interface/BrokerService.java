package MQproject.broker.Interface;

import MQproject.broker.model.message.BrokerBrokerMessage;
import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import org.springframework.stereotype.Service;

public interface BrokerService {


    public void runBroker();

    public void stopBroker();

    public BrokerClientMessage consumeMessage(BrokerClientMessage message);

    public void produceMessage(BrokerClientMessage message);

    public void updateReplicasData(BrokerBrokerMessage message);

    public void updatePartitionsHeadIndex(BrokerBrokerMessage message);

    public Object getPartitionsAndBrokersMapping();

    public void handleNewInformationAboutPartitions(BrokerServerMessageAboutPartitions message);

    public Object getPartitionReplicaBrokers(int partitionId);

}
