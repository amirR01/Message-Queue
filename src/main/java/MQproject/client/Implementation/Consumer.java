package MQproject.client.Implementation;

public class Consumer {
    private MessageBrokerClient client;
    private Object brokerIP;

    public Consumer(MessageBrokerClient client) {
        this.client = client;
    }

    public void subscribeToServer(Object server, Object topic) {
        client.subscribe(topic);
        // Assume that the broker IP is returned after subscribing
        this.brokerIP = client.getServer();
    }

    public void consumeMessage() {
        // Use the broker IP to pull a message from the server
        client.pull(brokerIP);

        // Add your logic to process the message here
    }
}