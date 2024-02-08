package MQproject.client.Implementation;

import MQproject.client.Interface.CommandLineInterface;
import MQproject.client.Interface.Consumer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class ConsumerImpl implements Consumer {
    @Autowired
    public ConsumerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public CommandLineInterface commandLineInterface;
    private Object brokerIP;
    private final RestTemplate restTemplate;
    private HashMap<String, Tuple<String, Integer>> addressMap;

    private String brokerAddress = "127.0.0.1";
    private String brokerPort = "8080";

    private Integer clientId = 0;


    public void subscribeToServer(Object server, Object partition) {
        //client.subscribe(partition);
        // Assume that the broker IP is returned after subscribing
        //this.brokerIP = client.getServer();
        //client.commandLineInterface.printInfo("Subscribed to broker " + this.brokerIP + " on partition " + partition);
    }

    public void consumeMessage() {
        // Use the broker IP to pull a message from the server
        BrokerClientMessage bigMessage = new BrokerClientMessage();
        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        clientId, null, null, MessageType.CONSUME_MESSAGE));

        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + this.brokerAddress + ":" + this.brokerPort + "/api/broker-client/consume-message",
                bigMessage,
                BrokerClientMessage.class
        );
        BrokerClientMessage responseBody = response.getBody();
        for (BrokerClientMessage.BrokerClientSmallerMessage msg : responseBody.messages) {
            commandLineInterface.printMessage(msg.data);
        }


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