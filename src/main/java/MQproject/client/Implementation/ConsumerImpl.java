package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.CommandLineInterface;
import MQproject.client.Interface.Consumer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class ConsumerImpl implements Consumer {
    @Autowired
    public ConsumerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public ServerCaller serverCaller;

    @Autowired
    public CommandLineInterface commandLineInterface;

    private final RestTemplate restTemplate;
    private HashMap<String, Tuple<String, Integer>> addressMap;

    public Integer myConsumerID;
    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;


    public void subscribeToServer(Object server, Object partition) {
        //client.subscribe(partition);
        // Assume that the broker IP is returned after subscribing
        //this.brokerIP = client.getServer();
        //client.commandLineInterface.printInfo("Subscribed to broker " + this.brokerIP + " on partition " + partition);
    }

    public void consumeMessage() {
        // Use the consumer IP to pull a message from the server
        BrokerClientMessage bigMessage = new BrokerClientMessage();
        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        myConsumerID, null, null, MessageType.CONSUME_MESSAGE));

        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + this.myIp + ":" + this.myPort + "/api/broker-client/consume-message",
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
        connectToServer();
        // register yourself to the server
        registerToServer();

        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public void connectToServer() {

    }


    private void registerToServer() {
        ClientServerMessage bigMessage = new ClientServerMessage();
        bigMessage.messages.add(
                new ClientServerMessage.ClientServerSmallerMessage(
                        null, myIp, myPort, MessageType.REGISTER_CONSUMER
                )
        );
        ClientServerMessage.ClientServerSmallerMessage response =
                serverCaller.registerToServer(bigMessage).messages.get(0);
        myConsumerID = response.ClientId;
    }

}