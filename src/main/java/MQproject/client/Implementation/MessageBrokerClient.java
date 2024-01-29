package MQproject.client.Implementation;

import MQproject.client.Interface.Client;
import MQproject.client.Interface.HandleServerConnection;

public class MessageBrokerClient implements Client {
    private final HandleServerConnection handleServerConnection;
    public CommandLineInterfaceImpl commandLineInterface;
    private Object server;

    private final Producer producer;
    private final Consumer consumer;

    public MessageBrokerClient() {
        this.handleServerConnection = new HandleServerConnectionImpl();
        this.consumer = new Consumer(this);
        this.producer = new Producer(this);
        this.commandLineInterface = new CommandLineInterfaceImpl(producer, consumer, this);
    }

    @Override
    public void runClient() {
        handleServerConnection.connectToServer(server);
    }

    @Override
    public void stopClient() {
        handleServerConnection.disconnectFromServer();
    }

    @Override
    public void subscribe(Object partition) {
        // Add your logic to subscribe to a topic here
        // server.sub(partition)
        // returns the broker of the partition
    }

    @Override
    public void unsubscribe(Object partition) {
        // Add your logic to unsubscribe from a topic here
        // set
    }

    @Override
    public void pull(Object server) {
        // Add your logic to pull a message from the server here
    }

    @Override
    public void push(Object server) {
        // Add your logic to push a message to the server here
    }

    public Object getServer() {
        return server;
    }

    public Object setServer(Object server) {
        return this.server = server;
    }
}