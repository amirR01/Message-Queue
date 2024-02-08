package MQproject.broker.Implementation;

import MQproject.broker.Caller.ServerCaller;
import MQproject.broker.Interface.BrokerService;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.message.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BrokerServiceImpl implements BrokerService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    public DataManager dataManager;
    @Autowired
    public ServerCaller serverCaller;
    public Integer myBrokerId;
    @Value("${MQproject.broker.my.address}")
    public String myIp;
    @Value("${MQproject.broker.my.port}")
    public Integer myPort;

    public HashMap<Integer, List<Integer>> producersPartitions = new HashMap<>();
    public HashMap<Integer, List<Integer>> consumersPartitions = new HashMap<>();
    public HashMap<Integer, Tuple<String, Integer>> brokersAddress = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();


    public void runBroker() {
        // register yourself to the server
        registerToServer();
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
            // TODO: read from the partition that client wants.
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
                dataManager.addMessage(smallerMessage.data, smallerMessage.partitionId, false);
                // TODO(): send data for the replicas asynchronously
                sendDataAsyncToTheReplica(smallerMessage);
            }
        }
    }

    private void sendDataAsyncToTheReplica(BrokerClientMessage.BrokerClientSmallerMessage smallerMessage) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // send the data to the replicas
            Integer replicaBrokerId = dataManager.getReplicaBrokerId(smallerMessage.partitionId);
            Tuple<String, Integer> brokerAddress = brokersAddress.get(replicaBrokerId);
            if (brokerAddress == null) {
                // request the server for the brokers address
                updateBrokersAddress();
                brokerAddress = brokersAddress.get(replicaBrokerId);
            }
            BrokerBrokerMessage brokerBrokerMessage = new BrokerBrokerMessage();
            brokerBrokerMessage.messages.add(
                    new BrokerBrokerMessage.BrokerBrokerSmallerMessage(
                            myBrokerId, smallerMessage.partitionId, smallerMessage.data, MessageType.REPLICATE_MESSAGE
                    )
            );
            restTemplate.postForEntity(
                    "http://" + brokerAddress.getFirst() + ":" + brokerAddress.getFirst() + "/api/broker-broker/update-replicas-data",
                    brokerBrokerMessage,
                    BrokerBrokerMessage.class
            );
        });
    }

    private void updateBrokersAddress() {
        BrokerServerMessageAboutBrokers message = serverCaller.getBrokersList();
        for (BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers smallerMessage : message.messages) {
            brokersAddress.put(smallerMessage.brokerId, new Tuple<>(smallerMessage.brokerIp, smallerMessage.brokerPort));
        }
    }

    private void registerToServer() {
        BrokerServerMessageAboutBrokers bigMessage = new BrokerServerMessageAboutBrokers();
        bigMessage.messages.add(
                new BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers(
                        null, myIp, myPort, MessageType.REGISTER_BROKER
                )
        );
        BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers response =
                serverCaller.registerToServer(bigMessage).messages.get(0);
        myBrokerId = response.brokerId;
    }

    public void handleNewInformationAboutPartitions(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.ADD_PARTITION) {
                dataManager.addPartition(
                        smallerMessage.partitionId, smallerMessage.leaderBrokerId,
                        smallerMessage.replicaBrokerId,
                        smallerMessage.data, 0, smallerMessage.isReplica
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


    public Object getPartitionReplicaBrokers(int partitionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateReplicasData(BrokerBrokerMessage message) {
        for (BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.REPLICATE_MESSAGE) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                dataManager.addMessage(smallerMessage.data, smallerMessage.partitionId, true);
            }
        }
    }

    public void updatePartitionsHeadIndex(BrokerBrokerMessage message) {
        for (BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.UPDATE_HEAD_INDEX) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                dataManager.updateHeadIndex(smallerMessage.partitionId, Integer.parseInt(smallerMessage.data));
            }
        }
    }
}
