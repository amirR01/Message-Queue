package MQproject.client.Implementation;

public class Consumer {
    private MessageBrokerClient client;
    private Object brokerIP;

    public Consumer(MessageBrokerClient client) {
        this.client = client;
    }

    public void subscribeToServer(Object server, Object partition) {
        client.subscribe(partition);
        // Assume that the broker IP is returned after subscribing
        this.brokerIP = client.getServer();
        client.commandLineInterface.printInfo("Subscribed to broker " + this.brokerIP + " on partition " + partition);
    }

    public void consumeMessage() {
        // Use the broker IP to pull a message from the server
        client.pull(brokerIP);
        client.commandLineInterface.printMessage("Message consumed");
        // Add your logic to process the message here
    }
}