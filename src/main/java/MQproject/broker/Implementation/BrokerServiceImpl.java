package MQproject.broker.Implementation;

import MQproject.broker.Interface.BrokerService;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import MQproject.broker.model.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class BrokerServiceImpl implements BrokerService {

    @Autowired
    public DataManager dataManager;

    public Integer myBrokerId;
    public Tuple<String, Integer> serveripPort = new Tuple<>("localhost", 9092);

    public HashMap<Integer, List<Integer>> producersPartitions = new HashMap<>();
    public HashMap<Integer, List<Integer>> consumersPartitions = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();


    public void runBroker() {
        connectToServer();
        // register yourself to the server
        registerToServer();

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BrokerClientMessage consumeMessage(BrokerClientMessage message) {
        BrokerClientMessage.BrokerClientSmallerMessage smallerMessage = message.messages.get(0);
        if (smallerMessage.messageType != MessageType.CONSUME_MESSAGE) {
            throw new IllegalArgumentException("Invalid message type");
        } else {
            Integer clientId = smallerMessage.clientId;
            List<Integer> partitions = consumersPartitions.get(clientId);
            if (partitions == null) {
                throw new IllegalArgumentException("Client not found");
            }
            BrokerClientMessage responseMessage = new BrokerClientMessage();
            for (Integer partitionId : partitions) {
                String data = dataManager.readMessage(partitionId);
                if (data != null) {
                    responseMessage.messages.add(
                            new BrokerClientMessage.BrokerClientSmallerMessage(
                                    clientId, partitionId, data, MessageType.ADD_MESSAGE));
                }
            }
            return responseMessage;
        }
    }

    public void produceMessage(BrokerClientMessage message) {
        for (BrokerClientMessage.BrokerClientSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.PRODUCE_MESSAGE) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                if (producersPartitions.get(smallerMessage.clientId) == null ||
                        !producersPartitions.get(smallerMessage.clientId).contains(smallerMessage.partitionId)) {
                    throw new IllegalArgumentException("Client not allowed to produce to this partition");
                }
                dataManager.addMessage(smallerMessage.data, smallerMessage.partitionId);
            }
        }
    }

    private void registerToServer() {
        // TODO(): handle getting self ip and port from network handler
    }

    public void handleNewInformationAboutPartitions(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.ADD_PARTITION) {
                dataManager.addPartition(
                        smallerMessage.partitionId, smallerMessage.leaderBrokerId,
                        smallerMessage.replicaBrokerId,
                        null, 0, smallerMessage.isReplica
                );
            } else {
                // not supported yet
            }
        }
    }




    public void stopBroker() {
    }

    public Object getPartitionsAndBrokersMapping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connectToServer() {
    }

    public void disconnectFromServer() {
    }


    public Object getPartitionReplicaBrokers(int partitionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
