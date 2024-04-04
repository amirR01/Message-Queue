package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.Producer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.MessageType;
import MQproject.client.model.message.ProducerServerMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static java.lang.Thread.sleep;

@Service
public class ProducerImpl implements Producer {

    @Autowired
    public ServerCaller serverCaller;

    private HashMap<String, Tuple<Integer, Tuple<Integer, Tuple<String, Integer>>>> addressMap = new HashMap<>();
    public Integer myProducerID;

    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;
    @Value("${MQproject.client.producer}")
    public Boolean isProducer;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        if (isProducer) {
            runProducer();
        }
    }

    @Override
    public void runProducer() {
        registerToServer();
    }

    @Override
    public void stopProducer() {
        // Add your logic to stop the producer here
    }

    @Override
    public String produceMessage(String key, String message) {
        // get address if not valid
        // send the message to the broker
        if (!addressMap.containsKey(key)) {
            ProducerServerMessage bigMessage =
                    new ProducerServerMessage();

            bigMessage.messages.add(
                    new ProducerServerMessage.ProducerServerSmallerMessage(
                            myProducerID, key, null, null, null, null, MessageType.GET_PARTITION
                    )
            );
            ProducerServerMessage.ProducerServerSmallerMessage response =
                    serverCaller.assignPartition(bigMessage).messages.get(0);

            addressMap.put(key, new Tuple<>(response.partitionId,
                    new Tuple<>(response.brokerId, new Tuple<>(response.brokerIp, response.brokerPort))));
        }

        BrokerClientMessage bigMessage = new BrokerClientMessage();
        Integer partitionId = addressMap.get(key).getFirst();
        Integer brokerId = addressMap.get(key).getSecond().getFirst();
        String  brokerIp = addressMap.get(key).getSecond().getSecond().getFirst();
        Integer brokerPort = addressMap.get(key).getSecond().getSecond().getSecond();

        bigMessage.messages.add(
                new BrokerClientMessage.BrokerClientSmallerMessage(
                        myProducerID, partitionId, "key:" + key + "-" + "value:" + message
                        + "\n", MessageType.PRODUCE_MESSAGE));


        ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
                "http://" + brokerIp + ":"
                        + brokerPort
                        + "/api/broker-client/produce-message",
                bigMessage,
                BrokerClientMessage.class
        );
        return "done";
    }

    private void registerToServer(){

        ClientServerMessage bigMessage =
                new ClientServerMessage();

        bigMessage.messages.add(
                new ClientServerMessage.ClientServerSmallerMessage(
                        null, myIp, myPort, MessageType.REGISTER_PRODUCER
                )
        );
        try {
            ClientServerMessage.ClientServerSmallerMessage response =
                    serverCaller.registerToServerForProducer(bigMessage).messages.get(0);
            myProducerID = response.clientId;
        } catch (Exception e) {
            // retry
            System.out.println("trying to connect to server");
            // stack overflow probability
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            registerToServer();
        }
    }

}