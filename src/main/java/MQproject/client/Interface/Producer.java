package MQproject.client.Interface;

import MQproject.client.model.message.BrokerClientMessage;

import java.net.UnknownHostException;

public interface Producer {
    void runProducer();

    void stopProducer();

    void produceMessage(BrokerClientMessage message);

    void listenForChangesFromServer();

    void connectToServer();

    void disconnectFromServer();


}