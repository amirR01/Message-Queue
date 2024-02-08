package MQproject.client.Interface;

import MQproject.client.model.message.BrokerClientMessage;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
@Service
public interface Producer {
    void runProducer();

    void stopProducer();

    void produceMessage(BrokerClientMessage message);

    void listenForChangesFromServer();

    void connectToServer();

    void disconnectFromServer();


}