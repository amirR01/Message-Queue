package MQproject.server.Implementation;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Broker;
import MQproject.server.Model.Data.Tuple;
import MQproject.server.Model.message.*;
import MQproject.server.Model.message.BrokerServerMessageAboutClients.BrokerServerSmallerMessageAboutClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ServerImplementation implements ServerService {

    @Autowired
    private RoundRobinLoadBalancer producerLoadBalancer;
    // @Autowired
    // private RoundRobinLoadBalancer consumerLoadBalancer;
    
    private final RestTemplate restTemplate = new RestTemplate();

    private HashMap<Integer, Broker> brokersIds = new HashMap<>();
    private Set<Integer> generatedTokens = new HashSet<>();
    private Random random = new Random();

    private HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions;

    private HashMap<Integer, ArrayList<Integer>> producerIdToPartitions;
    public HashMap<Integer, Tuple<String, Integer>> brokersAddress = new HashMap<>();


    public static void main(String[] args) {
        ServerImplementation s = new ServerImplementation();
        System.out.println("hello");
        s.runServer();
        System.out.println("after hello");
    }


    public void addPartitionAPI(Integer brokerId, BrokerServerMessageAboutPartitions message) {

        restTemplate.postForEntity(
                "http://" + brokersIds.get(brokerId).getIp() + ":"
                        + brokersIds.get(brokerId).getPort()
                        + "/api/broker-server/add-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
        // TODO: can also get ack response from brokers 
    }


    public void removePartitionAPI(Integer brokerId, BrokerServerMessageAboutPartitions message) {

        restTemplate.postForEntity(
                "http://" + brokersIds.get(brokerId).getIp() + ":"
                        + brokersIds.get(brokerId).getPort()
                        + "/api/broker-server/remove-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
        // TODO: can also get ack response from brokers 
    }

    public void movePartitionAPI(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId)
        // leader partition move
        restTemplate.postForEntity(
                "http://" + brokersIds.get(brokerId).getIp() + ":"
                        + brokersIds.get(brokerId).getPort()
                        + "/api/broker-server/move-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
        // TODO: can also get ack response from brokers 
    }


    public void clonePartitionAPI(Integer brokerId, BrokerServerMessageAboutPartitions message) {

        restTemplate.postForEntity(
                "http://" + brokersIds.get(brokerId).getIp() + ":"
                        + brokersIds.get(brokerId).getPort()
                        + "/api/broker-server/add-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
        // TODO: can also get ack response from brokers 
    }

    public void becomeLeaderAPI(Integer brokerId, BrokerServerMessageAboutPartitions message) {

        restTemplate.postForEntity(
                "http://" + brokersIds.get(brokerId).getIp() + ":"
                        + brokersIds.get(brokerId).getPort()
                        + "/api/broker-server/become-leader",
                        message, 
                        BrokerServerMessageAboutBrokers.class
        );
        // TODO: can also get ack response from brokers 
    }

    @Override
    public void runServer() {
        // TODO Auto-generated method stub
    }

    @Override
    public void stopServer() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'stopServer'");
    }


    @Override
    public void informBroker(Integer brokerId, BrokerServerMessageAboutClients message) {
        Broker broker = brokersIds.get(brokerId);
        restTemplate.postForEntity(
            "http://" + broker.getIp() + ":"
                    + broker.getPort()
                    + "/api/broker-server/become-leader",
                    message,
                    BrokerServerMessageAboutBrokers.class
    );
        // TODO: can also get ack response from brokers
    }


    @Override
    public ConsumerServerMessage subscribe(ConsumerServerMessage message) {
        for (ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.PRODUCE_MESSAGE) {

                // TODO: add a better Load Balancer 
                Broker toAssignBroker = producerLoadBalancer.getNextBroker();
                smallerMessage.brokerId = toAssignBroker.getId();
                smallerMessage.brokerPort = toAssignBroker.getPort();
                smallerMessage.brokerIp = toAssignBroker.getIp();
                smallerMessage.partitionId = null;

                // Inform brokers that a new subscriber added
                BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
                brokerMessage.messages.add(
                    new BrokerServerSmallerMessageAboutClient(
                        smallerMessage.partitionId, smallerMessage.brokerId, smallerMessage.ClientId, MessageType.INFORM_BROKER_ABOUT_SUBSCRIBER));
                informBroker(smallerMessage.brokerId, brokerMessage);
            }
        }
        return message;
    }


    @Override
    public ProducerServerMessage produce(ProducerServerMessage message) {
        // producerIdToPartitions.put(message.messages.get(0), new ArrayList())
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.PRODUCE_MESSAGE) {
                // TODO: add a better Load Balancer
                // TODO: save the producers id and the partition id in a map
                // producerIdToPartitions.put(smallerMessage.ClientId, smallerMessage.PartitionId);
                Broker toAssignedBroker = producerLoadBalancer.getNextBroker();
                smallerMessage.brokerId = toAssignedBroker.getId();
                smallerMessage.brokerPort = toAssignedBroker.getPort();
                smallerMessage.brokerIp = toAssignedBroker.getIp();
                // TODO: put approperiate partition in smallerMessage.PartitionId
                smallerMessage.PartitionId = null;

                // Inform brokers that a new producer added
                BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
                brokerMessage.messages.add(
                    new BrokerServerSmallerMessageAboutClient(
                        smallerMessage.PartitionId, smallerMessage.brokerId, smallerMessage.ClientId, MessageType.INFORM_BROKER_ABOUT_PRODUCER));
                informBroker(smallerMessage.brokerId, brokerMessage);
            }
        }
        return message;
    }


    public int generateToken() {
        int token;
        do {
            token = random.nextInt(Integer.MAX_VALUE);
        } while (!generatedTokens.add(token)); // Ensure token is unique
        return token;
    }

    @Override
    public BrokerServerMessageAboutBrokers registerBroker(BrokerServerMessageAboutBrokers message) {
        for (BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_BROKER) {
                smallerMessage.brokerId = generateToken();
                brokersIds.put(smallerMessage.brokerId, new Broker(smallerMessage.brokerIp, smallerMessage.brokerPort, smallerMessage.brokerId));
            } else {
                // nothing
            }
        }
        return message;
    }

    @Override
    public BrokerServerMessageAboutBrokers listAllBrokers() {
        BrokerServerMessageAboutBrokers message = new BrokerServerMessageAboutBrokers();
        for (Broker broker : brokersIds.values()) {
            message.messages.add(new BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers(broker.getId(), broker.getIp(), broker.getPort(), MessageType.LIST_BROKERS));
        }
        
        return message;
    }
}
