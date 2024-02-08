package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.CommandLineInterfaceOut;
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
import java.util.concurrent.CompletableFuture;

public class ConsumerImpl implements Consumer {
    @Autowired
    public ConsumerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public ServerCaller serverCaller;

    @Autowired
    public CommandLineInterfaceOut commandLineInterface;

    private final RestTemplate restTemplate;
    private HashMap<Integer, Tuple<String, Tuple<String, Integer>>> addressMap;



    public Integer myConsumerID;
    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;

    public void getBrokerAddress() {
        ConsumerServerMessage bigMessage = new ConsumerServerMessage();
        bigMessage.messages.add(
                new ConsumerServerMessage.ConsumerServerSmallerMessage(
                        myConsumerID, null, null, null,  null,MessageType.ASSIGN_BROKER)
        );
        ConsumerServerMessage responses =
                serverCaller.assignBroker(bigMessage);
        for (ConsumerServerMessage.ConsumerServerSmallerMessage response : responses.messages) {
            addressMap.put(response.brokerId, new Tuple<>(response.key, new Tuple<>(response.brokerIp, response.brokerPort)));
        }
    }

    public void consumeMessage() {
        // Use the consumer IP to pull a message from the server
        Integer brokerId = addressMap.keySet().iterator().next();
        BrokerClientMessage bigMessage = new BrokerClientMessage();
        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        myConsumerID, null, null, MessageType.CONSUME_MESSAGE));

        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + addressMap.get(brokerId).getFirst() + ":"
                        + addressMap.get(brokerId).getSecond()
                        + "/api/broker-client/consume-message",
                bigMessage,
                BrokerClientMessage.class
        );
        BrokerClientMessage responseBody = response.getBody();

        // ASYNC function call on response body
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            commandLineInterface.printMessage(responseBody.messages);
        });

    }

    @Override
    public void runConsumer() {
        registerToServer();
    }

    @Override
    public void stopConsumer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void subscribe() {
        getBrokerAddress();
        int i = 0;
        while (true) {
            i = i + 1;
            consumeMessage();
            if (i == 10) {
                getBrokerAddress();
                i = 0;
            }
        }
    }

    @Override
    public void pull() {
        getBrokerAddress();
        consumeMessage();
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