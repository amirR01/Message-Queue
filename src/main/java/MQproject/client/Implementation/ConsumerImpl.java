package MQproject.client.Implementation;

import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.NetworkHandlerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ConsumerImpl implements Consumer{

    @Autowired
    private NetworkHandlerInterface networkHandler;
    private Object brokerIP;

    private HashMap<String, Tuple<String, Integer>> addressMap;

    public void subscribeToServer(Object server, Object partition) {
        //client.subscribe(partition);
        // Assume that the broker IP is returned after subscribing
        //this.brokerIP = client.getServer();
        //client.commandLineInterface.printInfo("Subscribed to broker " + this.brokerIP + " on partition " + partition);
    }

    public void consumeMessage() {
        // Use the broker IP to pull a message from the server

        networkHandler.readMessage(8080); /// change the local port

    }

    @Override
    public void runConsumer() {

    }

    @Override
    public void stopConsumer() {

    }

    @Override
    public void subscribe(Object server) {

    }

    @Override
    public void unsubscribe(Object server) {

    }

    @Override
    public void pull(Object server) {
        try {
            //readNetwork(serverPortNumber, ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: add approperiate exception
        }
    }

    @Override
    public void push(Object server) {
        try {
            System.out.println("here");
            //CommandLineInterfaceImpl.networkHandler.writeNetwork(value, serverPortNumber , ipAddress);
            System.out.println("after here");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: add approperiate exception
        }
    }
}