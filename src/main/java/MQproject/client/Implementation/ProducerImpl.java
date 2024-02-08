package MQproject.client.Implementation;

import MQproject.client.Caller.ServerCaller;
import MQproject.client.Interface.Producer;
import MQproject.client.model.message.BrokerClientMessage;
import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;

public class ProducerImpl implements Producer {

    @Autowired
    public ServerCaller serverCaller;

    private HashMap<String, Tuple<String, Integer>> addressMap;


    public Integer myProducerID;
    @Value("${MQproject.client.my.address}")
    public String myIp;
    @Value("${MQproject.client.my.port}")
    public Integer myPort;

    @Override
    public void runProducer() {
        connectToServer();

        registerToServer();
    }

    @Override
    public void stopProducer() {
        // Add your logic to stop the producer here
    }

    @Override
    public void produceMessage(BrokerClientMessage message) {
        // send the message to server using clientservermessageaboutconsumers


    }

    @Override
    public void listenForChangesFromServer() {

    }

    @Override
    public void connectToServer() {


    }

    @Override
    public void disconnectFromServer() {
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