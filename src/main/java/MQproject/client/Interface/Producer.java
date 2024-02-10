package MQproject.client.Interface;

import MQproject.client.model.message.BrokerClientMessage;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
public interface Producer {
    void runProducer();

    void stopProducer();

    void produceMessage(String message, String key);
}