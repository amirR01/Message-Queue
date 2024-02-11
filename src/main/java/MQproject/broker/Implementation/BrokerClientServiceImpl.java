package MQproject.broker.Implementation;

import MQproject.broker.Interface.BrokerBrokerService;
import MQproject.broker.Interface.BrokerClientService;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.dataManagerModels.Partition;
import MQproject.broker.model.message.BrokerClientMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutClients;
import MQproject.broker.model.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BrokerClientServiceImpl implements BrokerClientService {
    @Autowired
    public DataManager dataManager;

    @Autowired
    public BrokerBrokerService brokerBrokerService;


    private HashMap<Integer, List<Integer>> producersPartitions = new HashMap<>();
    private HashMap<Integer, List<Integer>> consumersPartitions = new HashMap<>();

    public BrokerClientMessage consumeMessage(BrokerClientMessage message) {
        BrokerClientMessage.BrokerClientSmallerMessage smallerMessage = message.messages.get(0);
        if (smallerMessage.messageType != MessageType.CONSUME_MESSAGE) {
            throw new IllegalArgumentException("Invalid message type");
        } else {
            List<Integer> partitions = consumersPartitions.get(smallerMessage.clientId);
            if (partitions == null) {
                throw new IllegalArgumentException("Client not found");
            }
            List<Integer> changedPartitions = new ArrayList<>();
            BrokerClientMessage responseMessage = new BrokerClientMessage();
            for (Integer partitionId : partitions) {
                if (dataManager.isPartitionReplica(partitionId)) continue;
                String data = dataManager.readMessage(partitionId);
                if (data != null) {
                    responseMessage.messages.add(
                            new BrokerClientMessage.BrokerClientSmallerMessage(
                                    null, partitionId, data, MessageType.CONSUME_MESSAGE));
                    changedPartitions.add(partitionId);
                }
            }
            brokerBrokerService.sendHeadIndicesToReplicasAsync(changedPartitions);
            return responseMessage;
        }
    }

    public void produceMessage(BrokerClientMessage message) {
        for (BrokerClientMessage.BrokerClientSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType != MessageType.PRODUCE_MESSAGE) {
                throw new IllegalArgumentException("Invalid message type");
            } else {
                List<Integer> partitions = producersPartitions.get(smallerMessage.clientId);
                if (partitions == null || !partitions.contains(smallerMessage.partitionId)) {
                    throw new IllegalArgumentException("Client not allowed to produce to this partition");
                }
                dataManager.addMessage(smallerMessage.data, smallerMessage.partitionId, false);
                // TODO(): send data for the replicas asynchronously
                brokerBrokerService.sendDataAsyncToTheReplica(smallerMessage);
            }
        }
    }

    public void updateClients(BrokerServerMessageAboutClients message) {
        for (BrokerServerMessageAboutClients.BrokerServerSmallerMessageAboutClients smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.INFORM_ABOUT_PRODUCER) {
                if (smallerMessage.partitions == null){
                    throw new IllegalArgumentException("Invalid message");
                }
                producersPartitions.put(smallerMessage.clientId, smallerMessage.partitions);
            } else if (smallerMessage.messageType == MessageType.INFORM_ABOUT_CONSUMER) {
                if (smallerMessage.partitions == null){
                    throw new IllegalArgumentException("Invalid message");
                }
                consumersPartitions.put(smallerMessage.clientId, smallerMessage.partitions);
            }
            else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }

}
