package MQproject.server.Implementation;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Broker;
import MQproject.server.Model.data.Tuple;
import MQproject.server.Model.message.*;
import MQproject.server.Model.message.BrokerServerMessageAboutClients.BrokerServerSmallerMessageAboutClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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


    public void addPartitionAPI(Integer brokerId, Integer partitionId, boolean isReplica) {
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
            new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                partitionId, brokerId, -1, isReplica, MessageType.ADD_PARTITION));

        Broker broker = brokersIds.get(brokerId);

        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/add-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
    }


    public void removePartitionAPI(Integer brokerId, Integer partitionId, boolean isReplica) {

        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
            new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                partitionId, brokerId, -1, isReplica, MessageType.REMOVE_PARTITION));

        Broker broker = brokersIds.get(brokerId);

        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/remove-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
    }

    public void movePartitionAPI(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId, boolean isReplica) {
        // call approperiate function in load balancer
        //LoadBalancerResponse response = callLoadBalancer();
        // leader partition move
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
            new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                partitionId, sourceBrokerId, destinationBrokerId, isReplica, MessageType.MOVE_PARTITION));

        Broker broker = brokersIds.get(sourceBrokerId);

        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/move-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
    }


    public void clonePartitionAPI(Integer brokerId, Integer partitionId, boolean isReplica) {
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
            new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                partitionId, brokerId, -1, isReplica, MessageType.CLONE_PARTITION));

        Broker broker = brokersIds.get(brokerId);
        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/clone-partition",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
    }

    public void becomeLeaderAPI(Integer brokerId, Integer partitionId) {
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
            new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                partitionId, brokerId, -1, false, MessageType.BECOME_PARTITION_LEADER));

        Broker broker = brokersIds.get(brokerId);
        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/become-leader",
                        message, 
                        BrokerServerMessageAboutPartitions.class
        );
    }

    @Override
    public void runServer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkBrokersStatus, 0, 60, TimeUnit.SECONDS);
    }


    private void checkBrokersStatus() {
        long currentTime = System.currentTimeMillis();
        List<Integer> inactiveBrokers = new ArrayList<>();

        for (Map.Entry<Integer, Broker> entry : brokersIds.entrySet()) {
            int brokerId = entry.getKey();
            Broker broker = entry.getValue();
            long lastSeenTime = broker.getLastSeenTime();

            if (currentTime - lastSeenTime > 60000) { 
                // Broker is inactive for more than 1 minute
                inactiveBrokers.add(brokerId);
            }
        }

        for (Integer brokerId : inactiveBrokers) {
            brokersIds.remove(brokerId);
            System.out.println("Removed inactive broker: " + brokerId);
        }
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
                    + "/api/broker-server/inform-broker",
                    message,
                    BrokerServerMessageAboutClients.class
    );
    }


    @Override
    public ConsumerServerMessage subscribe(ConsumerServerMessage message) {
        for (ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.PRODUCE_MESSAGE) {

                // TODO: add a better Load Balancer 
                // get response from load balancer 

                Broker toAssignBroker = producerLoadBalancer.getNextBroker();

                int clientId = smallerMessage.ClientId;
                smallerMessage.brokerId = toAssignBroker.getId();
                smallerMessage.brokerPort = toAssignBroker.getPort();
                smallerMessage.brokerIp = toAssignBroker.getIp();
                smallerMessage.partitionId = null;    // TODO: put approperiate partitionId here from LB Response
                int newPartitionId = smallerMessage.partitionId;

                // update consumerIdToPartitions
                ArrayList<Integer> clientPartitions = consumerIdToPartitions.get(clientId);
                if (clientPartitions != null) {
                    clientPartitions.add(newPartitionId); 
                } else {
                    clientPartitions = new ArrayList<>();
                    clientPartitions.add(newPartitionId);
                    consumerIdToPartitions.put(clientId, clientPartitions);
                }

                // Inform brokers that a new subscriber added
                // put a hashmap here between subscriber id and it's partitions
                BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
                brokerMessage.messages.add(
                    new BrokerServerSmallerMessageAboutClients(
                        clientId, consumerIdToPartitions.get(
                            smallerMessage.ClientId), MessageType.INFORM_BROKER_ABOUT_SUBSCRIBER));
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

                int clientId = smallerMessage.ClientId;
                smallerMessage.brokerId = toAssignedBroker.getId();
                smallerMessage.brokerPort = toAssignedBroker.getPort();
                smallerMessage.brokerIp = toAssignedBroker.getIp();
                smallerMessage.PartitionId = null;    // TODO: put approperiate partitionId here from LB Response
                int newPartitionId = smallerMessage.PartitionId;

                // update consumerIdToPartitions
                ArrayList<Integer> clientPartitions = producerIdToPartitions.get(clientId);
                if (clientPartitions != null) {
                    clientPartitions.add(newPartitionId); 
                } else {
                    clientPartitions = new ArrayList<>();
                    clientPartitions.add(newPartitionId);
                    producerIdToPartitions.put(clientId, clientPartitions);
                }

                // Inform brokers that a new producer added
                BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
                brokerMessage.messages.add(
                    new BrokerServerSmallerMessageAboutClients(
                        clientId, producerIdToPartitions.get(
                            clientId), MessageType.INFORM_BROKER_ABOUT_PRODUCER));
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
            if (smallerMessage.messageType == MessageType.REGISTER_BROKER ||
            brokersIds.get(smallerMessage.brokerId) == null) {                      // for dead brokers
                smallerMessage.brokerId = generateToken();
                brokersIds.put(smallerMessage.brokerId, new Broker(smallerMessage.brokerIp, smallerMessage.brokerPort, smallerMessage.brokerId));
            } else {
                // nothing
            }
        }
        return message;
    }

    @Override
    public ConsumerServerMessage registerConsumer(ConsumerServerMessage message) {
        for (ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_CONSUMER ||
             consumerIdToPartitions.get(smallerMessage.ClientId) == null) {          // for dead consumers

                smallerMessage.ClientId = generateToken();
                ArrayList<Integer> clientPartitions = new ArrayList<>();
                consumerIdToPartitions.put(smallerMessage.ClientId, clientPartitions);
            } else {
                // nothing
            }
        }
        return message;
    }


    @Override
    public ProducerServerMessage registerProducer(ProducerServerMessage message) {
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_PRODUCER ||
            producerIdToPartitions.get(smallerMessage.ClientId) == null) {          // for dead producers

               smallerMessage.ClientId = generateToken();
               ArrayList<Integer> clientPartitions = new ArrayList<>();
               producerIdToPartitions.put(smallerMessage.ClientId, clientPartitions);
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
