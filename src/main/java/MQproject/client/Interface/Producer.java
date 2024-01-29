package MQproject.client.Interface;

import java.net.UnknownHostException;

public interface Producer {
    void runProducer();

    void stopProducer();

    void produceMessage(String message);

    void listenForChangesFromServer();

    void connectToServer(Object server) throws UnknownHostException;

    void disconnectFromServer();
}