package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.CommandLineInterfaceOut;
import MQproject.client.Interface.Consumer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.ConsumerServerMessage;
import MQproject.client.model.message.MessageType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ConsumerImpl implements Consumer {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ServerCaller serverCaller;

    @Autowired
    public CommandLineInterfaceOut commandLineInterface;

    private HashMap<Integer, Tuple<String, Integer>> addressMap = new HashMap<>();


    public Integer myConsumerID;
    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;
    @Value("${MQproject.client.consumer}")
    public Boolean isConsumer;

    @PostConstruct
    public void init() {
        if (isConsumer) {
            runConsumer();
        }
    }

    public void getBrokerAddress() {
        ConsumerServerMessage bigMessage = new ConsumerServerMessage();
        bigMessage.messages.add(
                new ConsumerServerMessage.ConsumerServerSmallerMessage(
                        myConsumerID, null, null, null, null, MessageType.ASSIGN_BROKER)
        );
        ConsumerServerMessage responses =
                serverCaller.assignBroker(bigMessage);
        for (ConsumerServerMessage.ConsumerServerSmallerMessage response : responses.messages) {
            addressMap.put(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort));
        }
    }

    public String getBrokerAddressPull() {
        ConsumerServerMessage bigMessage = new ConsumerServerMessage();
        bigMessage.messages.add(
                new ConsumerServerMessage.ConsumerServerSmallerMessage(
                        myConsumerID, null, null, null, null, MessageType.PULL_BROKER)
        );
        ConsumerServerMessage.ConsumerServerSmallerMessage response =
                serverCaller.assignBroker(bigMessage).messages.get(0);
        addressMap.put(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort));
        List<BrokerClientMessage.BrokerClientSmallerMessage> messages = consumeMessage(response.brokerId);

        return messages.get(0).data;
    }

    public List<BrokerClientMessage.BrokerClientSmallerMessage> consumeMessage(Integer brokerId) {
        // Use the consumer IP to pull a message from the server
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

        return responseBody.messages;
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
    public void subscribe_for_the_python_client() {
        getBrokerAddress();
    }

    @Override
    public List<String> pull_for_the_python_client() {
        List<BrokerClientMessage.BrokerClientSmallerMessage> messages = consumeMessage(addressMap.keySet().iterator().next());
        List<String> messages_data = new ArrayList<>();
        for (BrokerClientMessage.BrokerClientSmallerMessage message : messages) {
            messages_data.add(message.data);
        }
        return messages_data;
    }

    @Override
    public void subscribe() {
        getBrokerAddress();
        int i = 0;
        while (true) {
            if (!addressMap.keySet().isEmpty()) {
                for (Integer brokerId : addressMap.keySet()) {
                    i = i + 1;
                    List<BrokerClientMessage.BrokerClientSmallerMessage> messages =
                            consumeMessage(brokerId);
                    // ASYNC function call on response body
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        commandLineInterface.printMessage(messages);
                    });
                }
            }
            if (i >= 10) {
                getBrokerAddress();
                i = 0;
            }
        }
    }

    @Override
    public String pull() {
        return getBrokerAddressPull();
    }

    private void registerToServer() {
        ClientServerMessage bigMessage = new ClientServerMessage();
        bigMessage.messages.add(
                new ClientServerMessage.ClientServerSmallerMessage(
                        null, myIp, myPort, MessageType.REGISTER_CONSUMER
                )
        );
        ClientServerMessage.ClientServerSmallerMessage response =
                serverCaller.registerToServerForConsumer(bigMessage).messages.get(0);
        myConsumerID = response.clientId;
    }

}