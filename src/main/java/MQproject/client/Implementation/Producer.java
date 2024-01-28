package MQproject.client.Implementation;

// Producer.java
public class Producer {
    private MessageBrokerClient client;

    public Producer(MessageBrokerClient client) {
        this.client = client;
    }

    public void produceMessage(Object message, Object server) {
        // Add your logic to produce a message here
        // Then use the client to push the message to the server
        client.push(server);
    }
}