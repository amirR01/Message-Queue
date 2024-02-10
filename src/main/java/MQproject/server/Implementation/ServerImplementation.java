package MQproject.server.Implementation;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Broker;
import MQproject.server.Model.Data.LoadBalancerResponse;
import MQproject.server.Model.Data.LoadBalancerResponseAction;
import MQproject.server.Model.Data.Tuple;
import MQproject.server.Model.Message.*;
import MQproject.server.Model.Message.BrokerServerMessageAboutClients.BrokerServerSmallerMessageAboutClients;
import MQproject.server.Implementation.BrokerLoadBalancerImpl;
import MQproject.server.Implementation.ConsumerLoadBalancerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ServerImplementation implements ServerService {

    @Autowired
    private RoundRobinLoadBalancer producerLoadBalancer;
    // @Autowired
    // private RoundRobinLoadBalancer consumerLoadBalancer;

    @Autowired
    private BrokerLoadBalancerImpl brokerLoadBalancer; 

    @Autowired
    private ConsumerLoadBalancerImpl consumerLoadBalancerImpl;


    private final RestTemplate restTemplate = new RestTemplate();

    private HashMap<Integer, Broker> brokersIds = new HashMap<>();
    private Set<Integer> generatedTokens = new HashSet<>();
    private Random random = new Random();

    private HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions;

    private HashMap<Integer, ArrayList<Integer>> producerIdToPartitions;
    public HashMap<Integer, Tuple<String, Integer>> brokersAddress = new HashMap<>();

    // TODO: put these in application properties
    // number of brokers (Test)
    public int brokersNumber = 3;

    public int consumersNumber = 0;

    public int producersNumber = 0;




    public static void main(String[] args) {
        ServerImplementation s = new ServerImplementation();
        s.runServer();
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

    // can be used in scaling up system 
    public void addPartitionUtil(Integer partitionId) {          

        ArrayList<LoadBalancerResponse> responses = 
        brokerLoadBalancer.balanceOnPartitionBirth(brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, partitionId);
        
        for (LoadBalancerResponse response : responses) {
            if (response.getAction() == LoadBalancerResponseAction.ADD_PARTITION) {
                addPartitionAPI(response.getSourceBrokerId(), response.getPartitionId(), response.isReplica());
            }
        } 
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

        brokerLoadBalancer.balanceOnPartitionDeath(brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, partitionId);
    }
    
    // can be used in scaling down system
    public void removePartitionUtil(Integer partitionId) {         

        ArrayList<LoadBalancerResponse> responses = 
        brokerLoadBalancer.balanceOnPartitionDeath(brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, partitionId);
        
        for (LoadBalancerResponse response : responses) {
            if (response.getAction() == LoadBalancerResponseAction.REMOVE_PARTITION) {
                removePartitionAPI(response.getSourceBrokerId(), response.getPartitionId(), response.isReplica());
            }
        } 
    }

    public void movePartitionAPI(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId, boolean isReplica) {
        
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
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(this::checkBrokersStatus, 0, 60, TimeUnit.SECONDS);

        for (int i = 0; i < brokersNumber; i++) {
            // TODO: call onBrokerBirth after Adding new brokers
        }

         
    }

    @Scheduled(fixedRate = 60000)
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
            // not to remove here call load balancer.
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
                        smallerMessage.brokerId = addNewBrokerUtil(smallerMessage.brokerIp, smallerMessage.brokerPort);
            } else {
                // set last seen time to current time
            }
        }
        return message;
    }

    public int addNewBrokerUtil(String brokerIp, int brokerPort) {
        int brokerId = generateToken();
        brokersIds.put(brokerId, new Broker(brokerIp, brokerPort, brokerId));
        brokersNumber++;
        
        // TODO: call load balancer function
        
        return brokerId;  // on success
    }

    @Override
    public ConsumerServerMessage registerConsumer(ConsumerServerMessage message) {
        for (ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_CONSUMER ||
                    consumerIdToPartitions.get(smallerMessage.ClientId) == null) {          // for dead consumers

                smallerMessage.ClientId = addNewConsumerUtil();

            } else {
                // TODO: update lastTime in each broker register calls
            }
        }
        return message;
    }

    public int addNewConsumerUtil() {
        int clientId = generateToken();
        ArrayList<Integer> clientPartitions = new ArrayList<>();
        consumerIdToPartitions.put(clientId, clientPartitions);
        consumersNumber++;

        // TODO: call load balancer function

        return clientId;
    }


    @Override
    public ProducerServerMessage registerProducer(ProducerServerMessage message) {
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_PRODUCER ||
                    producerIdToPartitions.get(smallerMessage.ClientId) == null) {          // for dead producers

                smallerMessage.ClientId = addNewProducerUtil();

            } else {
                // TODO: update lastTime in each broker register calls
            }
        }
        return message;
    }

    public int addNewProducerUtil() {
        int clientId = generateToken();
        ArrayList<Integer> clientPartitions = new ArrayList<>();
        producerIdToPartitions.put(clientId, clientPartitions);
        producersNumber++;

        // TODO: call load balancer function

        return clientId;
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
