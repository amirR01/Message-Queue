package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.Producer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.MessageType;
import MQproject.client.model.message.ProducerServerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;

public class ProducerImpl implements Producer {

    @Autowired
    public ServerCaller serverCaller;

    private HashMap<String, Tuple<Integer, Tuple<Integer, Tuple<String, Integer>>>> addressMap;
    // first: key, second: partition ID, third: brokerID, fourth: brokerIP, fifth: port
    public Integer myProducerID;

    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;

    private RestOperations restTemplate;

    @Override
    public void runProducer() {
        registerToServer();
    }

    @Override
    public void stopProducer() {
        // Add your logic to stop the producer here
    }

    @Override
    public void produceMessage(String message, String key) {
        if (!addressMap.containsKey(key)){
            ProducerServerMessage bigMessage =
                    new ProducerServerMessage();

            bigMessage.messages.add(
                    new ProducerServerMessage.ProducerServerSmallerMessage(
                            myProducerID,key, null ,null,null, null, MessageType.GET_PARTITION
                    )
            );
            ProducerServerMessage.ProducerServerSmallerMessage response =
                    serverCaller.assignPartition(bigMessage).messages.get(0);

            addressMap.put(key, new Tuple<>(response.PartitionId,
                    new Tuple<>(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort))));
        }

        BrokerClientMessage bigMessage = new BrokerClientMessage();
        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        myProducerID, null, null, MessageType.PRODUCE_MESSAGE));


        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + addressMap.get(key).getSecond().getSecond().getFirst() + ":"
                        + addressMap.get(key).getSecond().getSecond().getSecond()
                        + "/api/broker-client/consume-message",
                bigMessage,
                BrokerClientMessage.class
        );
    }

    private void registerToServer() {
        ClientServerMessage bigMessage =
                new ClientServerMessage();

        bigMessage.messages.add(
                new ClientServerMessage.ClientServerSmallerMessage(
                        null,myIp,myPort, MessageType.REGISTER_PRODUCER
                )
        );
        ClientServerMessage.ClientServerSmallerMessage response =
                serverCaller.registerToServer(bigMessage).messages.get(0);
        myProducerID = response.ClientId;
    }

}