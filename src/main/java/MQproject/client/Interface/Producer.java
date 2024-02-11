package MQproject.client.Interface;

public interface Producer {
    void runProducer();

    void stopProducer();

    String produceMessage(String message, String key);
}