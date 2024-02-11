package MQproject.broker.Implementation;

import MQproject.broker.Caller.ServerCaller;
import MQproject.broker.Interface.BrokerServerService;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.dataManagerModels.Partition;
import MQproject.broker.model.message.BrokerBrokerMessage;
import MQproject.broker.model.message.BrokerServerMessageAboutBrokers;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import MQproject.broker.model.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class BrokerServerServiceImpl implements BrokerServerService {
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

    public HashMap<Integer, Tuple<String, Integer>> brokersAddress = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            runBroker();
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your application's requirements
        }
    }

    public void runBroker() {
        // register yourself to the server
        registerToServer();
    }

    public void stopBroker() {
    }

    @Scheduled(fixedRate = 15000)
    private void heartBeat() {
        registerToServer();
    }

    private void registerToServer() {
        BrokerServerMessageAboutBrokers bigMessage = new BrokerServerMessageAboutBrokers();
        bigMessage.messages.add(
                new BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers(
                        myBrokerId, myIp, myPort, MessageType.REGISTER_BROKER
                )
        );
        BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers response =
                serverCaller.registerToServer(bigMessage).messages.get(0);
        myBrokerId = response.brokerId;
    }

    public Integer getMyBrokerId() {
        return myBrokerId;
    }

    public Tuple<String, Integer> getBrokerAddress(Integer brokerId) {
        brokersAddress.get(brokerId);
        if (brokersAddress.get(brokerId) == null) {
            updateBrokersAddress();
        }
        return brokersAddress.get(brokerId);
    }

    private void updateBrokersAddress() {
        try {
            BrokerServerMessageAboutBrokers message = serverCaller.getBrokersList();
            for (BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers smallerMessage : message.messages) {
                brokersAddress.put(smallerMessage.brokerId, new Tuple<>(smallerMessage.brokerIp, smallerMessage.brokerPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addPartition(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.ADD_PARTITION) {
                dataManager.addPartition(
                        smallerMessage.partitionId, smallerMessage.leaderBrokerId,
                        smallerMessage.replicaBrokerId,
                        smallerMessage.data, 0, false
                );
            } else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }

    public void movePartition(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.MOVE_PARTITION) {
                BrokerBrokerMessage brokerBrokerMessage = new BrokerBrokerMessage();
                Tuple<String, Integer> brokerAddress = getBrokerAddress(smallerMessage.leaderBrokerId);
                Tuple<Partition, String> partition = dataManager.getPartition(smallerMessage.partitionId);
                brokerBrokerMessage.messages.add(
                        new BrokerBrokerMessage.BrokerBrokerSmallerMessage(
                                smallerMessage.leaderBrokerId, partition.getFirst().replicaBrokerId,
                                smallerMessage.partitionId, partition.getFirst().headIndex, partition.getSecond(),
                                MessageType.RECEIVE_PARTITION_BECOME_LEADER
                        )
                );
                restTemplate.postForEntity(
                        "http://" + brokerAddress.getFirst() + ":" + brokerAddress.getSecond() + "/api/broker-broker/receive-partition-and-become-leader",
                        brokerBrokerMessage,
                        BrokerBrokerMessage.class
                );
            } else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }

    public void clonePartition(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.CLONE_PARTITION) {
                BrokerBrokerMessage brokerBrokerMessage = new BrokerBrokerMessage();
                Tuple<String, Integer> brokerAddress = getBrokerAddress(smallerMessage.replicaBrokerId);
                Tuple<Partition, String> partition = dataManager.getPartition(smallerMessage.partitionId);
                partition.getFirst().replicaBrokerId = smallerMessage.replicaBrokerId;
                brokerBrokerMessage.messages.add(
                        new BrokerBrokerMessage.BrokerBrokerSmallerMessage(
                                smallerMessage.leaderBrokerId, smallerMessage.replicaBrokerId,
                                smallerMessage.partitionId, partition.getFirst().headIndex, partition.getSecond(),
                                MessageType.RECEIVE_PARTITION_BECOME_REPLICA
                        )
                );
                restTemplate.postForEntity(
                        "http://" + brokerAddress.getFirst() + ":" + brokerAddress.getSecond() + "/api/broker-broker/receive-partition-and-become-replica",
                        brokerBrokerMessage,
                        BrokerBrokerMessage.class
                );
            } else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }

    public void removePartition(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REMOVE_PARTITION) {
                dataManager.removePartition(smallerMessage.partitionId);
            } else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }

    public void becomeLeader(BrokerServerMessageAboutPartitions message) {
        for (BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.BECOME_PARTITION_LEADER) {
                dataManager.makePartitionPrimary(smallerMessage.partitionId);
            } else {
                throw new IllegalArgumentException("Invalid message type");
            }
        }
    }
}
