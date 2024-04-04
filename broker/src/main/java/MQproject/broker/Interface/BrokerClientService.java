package MQproject.broker.Interface;

import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutClients;

import java.util.List;

public interface BrokerClientService {

    public BrokerClientMessage consumeMessage(BrokerClientMessage message);

    public void produceMessage(BrokerClientMessage message);

    public void updateClients(BrokerServerMessageAboutClients message);
}
