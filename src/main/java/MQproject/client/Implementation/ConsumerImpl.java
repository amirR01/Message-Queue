package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.CommandLineInterface;
import MQproject.client.Interface.Consumer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.ConsumerServerMessage;
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
    private HashMap<String, Tuple<Integer, Tuple<Integer, Tuple<String, Integer>>>> addressMap;

    public Integer myConsumerID;
    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;


    public void consumeMessage(String key) {
        // Use the consumer IP to pull a message from the server
        BrokerClientMessage bigMessage = new BrokerClientMessage();
        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        myConsumerID, null, null, MessageType.CONSUME_MESSAGE));

        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + this.addressMap.get(key).getSecond().getSecond().getSecond() + ":"
                        + this.addressMap.get(key).getSecond().getSecond().getFirst()
                        + "/api/broker-client/consume-message",
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
    public void subscribe(String key) {
        ConsumerServerMessage bigMessage = new ConsumerServerMessage();
        bigMessage.messages.add(
                new ConsumerServerMessage.ConsumerServerSmallerMessage(
                        myConsumerID, key, null, null, null,  MessageType.ASSIGN_BROKER)
        );
        ConsumerServerMessage.ConsumerServerSmallerMessage response =
                serverCaller.assignBroker(bigMessage).messages.get(0);
        addressMap.put(key, new Tuple<>(response.brokerId,
                new Tuple<>(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort))));

        // subscribe logic
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void pull(String key) {
        ConsumerServerMessage bigMessage = new ConsumerServerMessage();
        bigMessage.messages.add(
                new ConsumerServerMessage.ConsumerServerSmallerMessage(
                        myConsumerID, key, null, null, null, MessageType.ASSIGN_BROKER)
        );
        ConsumerServerMessage.ConsumerServerSmallerMessage response =
                serverCaller.assignBroker(bigMessage).messages.get(0);
        addressMap.put(key, new Tuple<>(response.brokerId,
                new Tuple<>(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort))));

        consumeMessage(key);

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