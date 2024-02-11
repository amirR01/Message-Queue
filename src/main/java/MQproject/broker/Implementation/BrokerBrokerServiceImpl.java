package MQproject.broker.Implementation;

import MQproject.broker.Interface.BrokerBrokerService;
import MQproject.broker.Interface.BrokerServerService;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.message.BrokerBrokerMessage;
import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.MessageType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BrokerBrokerServiceImpl implements BrokerBrokerService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    public DataManager dataManager;
    @Autowired
    public BrokerServerService brokerServerService;


    public void sendHeadIndicesToReplicasAsync(List<Integer> changedPartitions) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (Integer partitionId : changedPartitions) {
                Integer replicaBrokerId = dataManager.getReplicaBrokerId(partitionId);
                if (replicaBrokerId != null) {
                    Tuple<String, Integer> brokerAddress = brokerServerService.getBrokerAddress(replicaBrokerId);
                    if (brokerAddress == null) {
                        // request the server for the brokers address
                        brokerAddress = brokerServerService.getBrokerAddress(replicaBrokerId);
                    }
                    BrokerBrokerMessage brokerBrokerMessage = new BrokerBrokerMessage();
                    brokerBrokerMessage.messages.add(
                            new BrokerBrokerMessage.BrokerBrokerSmallerMessage(
                                    brokerServerService.getMyBrokerId(), replicaBrokerId, partitionId,
                                    dataManager.getHeadIndex(partitionId), null, MessageType.UPDATE_HEAD_INDEX
                            )
                    );
                    restTemplate.postForEntity(
                            "http://" + brokerAddress.getFirst() + ":" + brokerAddress.getFirst() + "/api/broker-broker/update-partitions-head-index",
                            brokerBrokerMessage,
                            BrokerBrokerMessage.class
                    );
                }
            }
        });
    }


    public void sendDataAsyncToTheReplica(BrokerClientMessage.BrokerClientSmallerMessage smallerMessage) {
        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // send the data to the replicas
                Integer replicaBrokerId = dataManager.getReplicaBrokerId(smallerMessage.partitionId);
                if (replicaBrokerId != null) {
                    Tuple<String, Integer> brokerAddress = brokerServerService.getBrokerAddress(replicaBrokerId);
                    if (brokerAddress == null) {
                        // request the server for the brokers address
                        brokerAddress = brokerServerService.getBrokerAddress(replicaBrokerId);
                    }
                    BrokerBrokerMessage brokerBrokerMessage = new BrokerBrokerMessage();
                    brokerBrokerMessage.messages.add(
                            new BrokerBrokerMessage.BrokerBrokerSmallerMessage(
                                    brokerServerService.getMyBrokerId(), replicaBrokerId, smallerMessage.partitionId, null,
                                    smallerMessage.data, MessageType.REPLICATE_MESSAGE
                            )
                    );
                    restTemplate.postForEntity(
                            "http://" + brokerAddress.getFirst() + ":" + brokerAddress.getSecond() + "/api/broker-broker/update-replicas-data",
                            brokerBrokerMessage,
                            BrokerBrokerMessage.class
                    );
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void updateReplicasData(@NotNull BrokerBrokerMessage message) {
        for (BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.REPLICATE_MESSAGE) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                dataManager.addMessage(smallerMessage.data, smallerMessage.partitionId,true);
            }
        }
    }

    public void updatePartitionsHeadIndex(@NotNull BrokerBrokerMessage message) {
        for (BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.UPDATE_HEAD_INDEX) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                dataManager.updateHeadIndex(smallerMessage.partitionId, smallerMessage.headIndex);
            }
        }
    }

    public void receivePartitionAndBecomeLeader(BrokerBrokerMessage message) {
        BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage = message.messages.get(0);
        if (smallerMessage.messageType != MessageType.RECEIVE_PARTITION_BECOME_LEADER) {
            throw new IllegalArgumentException("Invalid message type");
        } else {
            if (!smallerMessage.leaderBrokerId.equals(brokerServerService.getMyBrokerId())) {
                throw new IllegalArgumentException("Invalid leader broker id");
            }
            String partitionData = smallerMessage.data;
            dataManager.addPartition(
                    smallerMessage.partitionId, brokerServerService.getMyBrokerId(), smallerMessage.replicaBrokerId,
                    partitionData, smallerMessage.headIndex, false
            );
        }
    }

    public void receivePartitionAndBecomeReplica(BrokerBrokerMessage message) {
        BrokerBrokerMessage.BrokerBrokerSmallerMessage smallerMessage = message.messages.get(0);
        if (smallerMessage.messageType != MessageType.RECEIVE_PARTITION_BECOME_REPLICA) {
            throw new IllegalArgumentException("Invalid message type");
        } else {
            if (!smallerMessage.replicaBrokerId.equals(brokerServerService.getMyBrokerId())) {
                throw new IllegalArgumentException("Invalid replica broker id");
            }
            String partitionData = smallerMessage.data;
            dataManager.addPartition(
                    smallerMessage.partitionId, smallerMessage.leaderBrokerId, brokerServerService.getMyBrokerId(),
                    partitionData, smallerMessage.headIndex, true
            );
        }

    }
}
