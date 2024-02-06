package MQproject.broker.Interface;

import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import org.springframework.stereotype.Service;

@Service
public interface BrokerService {


    public void runBroker();

    public void stopBroker();

    public BrokerClientMessage consumeMessage(BrokerClientMessage message);

    public void produceMessage(BrokerClientMessage message);

    public Object getPartitionsAndBrokersMapping();

    public void connectToServer();

    public void disconnectFromServer();

    public void handleNewInformationAboutPartitions(BrokerServerMessageAboutPartitions message);

    public Object getPartitionReplicaBrokers(int partitionId);


}
