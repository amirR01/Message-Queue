package MQproject.server.Implementation;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Broker;
import MQproject.server.Model.Client;
import MQproject.server.Model.ClientType;
import MQproject.server.Model.Data.LoadBalancerResponse;
import MQproject.server.Model.Data.Tuple;
import MQproject.server.Model.Message.*;
import MQproject.server.Model.Message.BrokerServerMessageAboutClients.BrokerServerSmallerMessageAboutClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ServerImplementation implements ServerService {


    @Autowired
    private BrokerLoadBalancerImpl brokerLoadBalancer;

    @Autowired
    private ConsumerLoadBalancerImpl consumerLoadBalancer;

    private final HashMap<Integer, Client> allConsumersIds = new HashMap<>();
    private final HashMap<Integer, Client> allProducersIds = new HashMap<>();

    // TODO: check for those who are responsible for consuming that they are alive
    private final Map<Integer, Long> havingPartitionsConsumersTime = new HashMap<>();


    private final RestTemplate restTemplate = new RestTemplate();

    private HashMap<Integer, Broker> brokersIds = new HashMap<>();
    private Set<Integer> generatedTokens = new HashSet<>();
    private Random random = new Random();

    private HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions = new HashMap<>();

    private HashMap<Integer, ArrayList<Integer>> producerIdToPartitions = new HashMap<>();
    public HashMap<Integer, Tuple<String, Integer>> brokersAddress = new HashMap<>();
    public HashMap<String, Integer> keyToPartition = new HashMap<>();


    public static void main(String[] args) {
        ServerImplementation s = new ServerImplementation();
        s.runServer();
    }


    public void addPartitionAPI(Integer brokerId, Integer partitionId) {
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
                new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                        partitionId, brokerId, null, MessageType.ADD_PARTITION));

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
    public void addPartitionUtil(Integer producerId, String key) {
        int partitionId = generateToken();
        keyToPartition.put(key, partitionId);
        ArrayList<LoadBalancerResponse> responses =
                brokerLoadBalancer.balanceOnPartitionBirth(brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, partitionId);
        // partition management.
        responseToApiCallMapping(responses);
        // consumer management.
        if (!consumerIdToPartitions.isEmpty()) {
            Integer consumerId = consumerLoadBalancer.balanceOnPartitionBirth(consumerIdToPartitions, partitionId);
            Integer brokerId = findThisPartitionBroker(partitionId);
            informBrokerAboutConsumer(brokerId, consumerId);
        }
    }

    private void responseToApiCallMapping(ArrayList<LoadBalancerResponse> responses) {
        for (LoadBalancerResponse response : responses) {
            switch (response.getAction()) {
                case MOVE_PARTITION:
                    movePartitionAPI(response.getSourceBrokerId(), response.getDestinationBrokerId(), response.getPartitionId());
                    break;
                case REMOVE_PARTITION:
                    removePartitionAPI(response.getSourceBrokerId(), response.getPartitionId());
                    break;
                case CLONE_PARTITION:
                    clonePartitionAPI(response.getSourceBrokerId(), response.getDestinationBrokerId(), response.getPartitionId());
                    break;
                case ADD_PARTITION:
                    addPartitionAPI(response.getSourceBrokerId(), response.getPartitionId());
                    break;
                case BECOME_PARTITION_LEADER:
                    becomeLeaderAPI(response.getSourceBrokerId(), response.getPartitionId());
                    break;
                default:
                    // Do nothing
                    break;
            }
        }


    }

    private void addPartitionToProducer(Integer producerId, int partitionId) {
        ArrayList<Integer> partitions = producerIdToPartitions.get(producerId);
        if (partitions != null) {
            partitions.add(partitionId);
        } else {
            partitions = new ArrayList<>();
            partitions.add(partitionId);
            producerIdToPartitions.put(producerId, partitions);
        }
    }


    public void removePartitionAPI(Integer brokerId, Integer partitionId) {
        // TODO: these are just api's check for not to do anything else
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
                new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                        partitionId, null, null, MessageType.REMOVE_PARTITION));

        Broker broker = brokersIds.get(brokerId);

        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/remove-partition",
                message,
                BrokerServerMessageAboutPartitions.class
        );
    }

    public void movePartitionAPI(Integer sourceBrokerId, Integer destinationBrokerId, Integer partitionId) {

        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
                new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                        partitionId, destinationBrokerId, null, MessageType.MOVE_PARTITION));

        Broker broker = brokersIds.get(sourceBrokerId);

        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/move-partition",
                message,
                BrokerServerMessageAboutPartitions.class
        );
    }


    public void clonePartitionAPI(Integer leaderBrokerId, Integer replicaBrokerId, Integer partitionId) {
        BrokerServerMessageAboutPartitions message = new BrokerServerMessageAboutPartitions();
        message.messages.add(
                new BrokerServerMessageAboutPartitions.BrokerServerSmallerMessageAboutPartitions(
                        partitionId, leaderBrokerId, replicaBrokerId, MessageType.CLONE_PARTITION));

        Broker broker = brokersIds.get(leaderBrokerId);
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
                        partitionId, brokerId, null, MessageType.BECOME_PARTITION_LEADER));

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
//
//        for (int i = 0; i < brokersNumber; i++) {
//            // TODO: call onBrokerBirth after Adding new brokers
//        }
    }
// TODO:
//    @Scheduled(fixedRate = 60000)
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
            // load balancing on broker death
            ArrayList<LoadBalancerResponse> responses =
                    brokerLoadBalancer.balanceOnBrokerDeath(
                            brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, brokerId);
            responseToApiCallMapping(responses);
            System.out.println("Removed inactive broker: " + brokerId);
        }
    }


    @Scheduled(fixedRate = 60000)
    private void checkConsumerStatus() {
        long currentTime = System.currentTimeMillis();
        List<Integer> inactiveConsumers = new ArrayList<>();

        for (Map.Entry<Integer, Client> entry : allConsumersIds.entrySet()) {
            int clientId = entry.getKey();
            Client client = entry.getValue();
            long lastSeenTime = client.getLastSeenTime();

            if (currentTime - lastSeenTime > 60000) {
                // Broker is inactive for more than 1 minute
                inactiveConsumers.add(clientId);
            }
        }

        for (Integer clientId : inactiveConsumers) {
            // not to remove here call load balancer.
            allConsumersIds.remove(clientId);
            consumerLoadBalancer.balanceOnConsumerDeath(consumerIdToPartitions, clientId);
            System.out.println("Removed inactive consumer: " + clientId);
        }
    }

    @Override
    public void stopServer() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'stopServer'");
    }

    public void informBrokerAboutConsumer(Integer brokerId, Integer consumerId) {
        BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
        brokerMessage.messages.add(
                new BrokerServerSmallerMessageAboutClients(
                        consumerId,
                        consumerIdToPartitions.get(
                                consumerId), MessageType.INFORM_ABOUT_CONSUMER));


        informApi(brokerId, brokerMessage);
    }

    public void informBrokerAboutProducer(Integer brokerId, Integer producerId) {
        BrokerServerMessageAboutClients brokerMessage = new BrokerServerMessageAboutClients();
        brokerMessage.messages.add(
                new BrokerServerSmallerMessageAboutClients(
                        producerId,
                        producerIdToPartitions.get(
                                producerId), MessageType.INFORM_ABOUT_PRODUCER));
        informApi(brokerId, brokerMessage);
    }

    private void informApi(Integer brokerId, BrokerServerMessageAboutClients brokerMessage) {
        Broker broker = brokersIds.get(brokerId);
        restTemplate.postForEntity(
                "http://" + broker.getIp() + ":"
                        + broker.getPort()
                        + "/api/broker-server/update-clients",
                brokerMessage,
                BrokerServerMessageAboutClients.class
        );
    }


    @Override
    public ConsumerServerMessage subscribe(ConsumerServerMessage message) {
        ConsumerServerMessage bigResponse = new ConsumerServerMessage();
        ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage = message.messages.get(0);
        if (smallerMessage.messageType == MessageType.ASSIGN_BROKER) {
            // TODO: check the constraint
            if (consumerIdToPartitions.get(smallerMessage.clientId) == null) {
                allConsumersIds.put(smallerMessage.clientId , new Client(smallerMessage.clientId, ClientType.CONSUMER));
                consumerLoadBalancer.balanceOnConsumerBirth(
                        consumerIdToPartitions,
                        new ArrayList<>(keyToPartition.values())
                        , smallerMessage.clientId);
            }
            allConsumersIds.get(smallerMessage.clientId).updateLastSeenTime();
            List<Integer> partitions = consumerIdToPartitions.get(smallerMessage.clientId);
            for (Integer partition : partitions) {
                // where is this partition?
                Integer brokerId = findThisPartitionBroker(partition);
                Broker broker = brokersIds.get(brokerId);
                bigResponse.messages.add(
                        new ConsumerServerMessage.ConsumerServerSmallerMessage(
                                smallerMessage.clientId, broker.getId(), broker.getIp(), broker.getPort(), partition, MessageType.ASSIGN_BROKER));
                informBrokerAboutConsumer(brokerId, smallerMessage.clientId);
            }
        }
        return bigResponse;
    }

    private Integer findThisPartitionBroker(Integer partition) {
        for (Map.Entry<Integer, ArrayList<Integer>> entry : brokerIdToLeaderPartitions.entrySet()) {
            Integer brokerId = entry.getKey();
            ArrayList<Integer> partitions = entry.getValue();
            if (partitions.contains(partition)) {
                return brokerId;
            }
        }
        throw new IllegalArgumentException("Partition not found");
    }


    @Override
    public ProducerServerMessage produce(ProducerServerMessage message) {
        // producerIdToPartitions.put(message.messages.get(0), new ArrayList())
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.GET_PARTITION) {
                if (!keyToPartition.containsKey(smallerMessage.key)) {
                    addPartitionUtil(smallerMessage.clientId, smallerMessage.key);
                }
                int clientId = smallerMessage.clientId;
                int partitionId = keyToPartition.get(smallerMessage.key);

                addPartitionToProducer(clientId, partitionId);

                int brokerId = findThisPartitionBroker(partitionId);
                informBrokerAboutProducer(brokerId, clientId);
                Broker toAssignedBroker = brokersIds.get(brokerId);
                smallerMessage.brokerId = toAssignedBroker.getId();
                smallerMessage.brokerPort = toAssignedBroker.getPort();
                smallerMessage.brokerIp = toAssignedBroker.getIp();
                smallerMessage.partitionId = partitionId;
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
            if (smallerMessage.messageType == MessageType.REGISTER_BROKER
                    && (smallerMessage.brokerId == null || !brokersIds.containsKey(smallerMessage.brokerId))) {
                System.out.println("new broker");
                System.out.println(smallerMessage.brokerId);
                // for dead brokers
                Integer brokerId = addNewBrokerUtil(smallerMessage.brokerIp, smallerMessage.brokerPort);
                smallerMessage.brokerId = brokerId;
                loadBalanceAsynclyAddingNewBroker(brokerId);

            } else {
                // set last seen time to current time
                Broker broker = brokersIds.get(smallerMessage.brokerId);
                broker.updateLastSeenTime();
            }
        }
        return message;
    }

    private void loadBalanceAsynclyAddingNewBroker(Integer brokerId) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // TODO: Do the needed actions.
            ArrayList<LoadBalancerResponse> responses =
                    brokerLoadBalancer.balanceOnBrokerBirth(
                            brokerIdToLeaderPartitions, brokerIdToReplicaPartitions, brokerId);            // partition management.
            responseToApiCallMapping(responses);
        });
    }

    public int addNewBrokerUtil(String brokerIp, int brokerPort) {
        int brokerId = generateToken();
        brokersIds.put(brokerId, new Broker(brokerIp, brokerPort, brokerId));

        // TODO: What to add here?
        // new broker with previous partitions
        // new broker with no partitions


        return brokerId;  // on success
    }

    @Override
    public ConsumerServerMessage registerConsumer(ConsumerServerMessage message) {
        for (ConsumerServerMessage.ConsumerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_CONSUMER) {          // for dead consumers
                smallerMessage.clientId = generateToken();

            } else {
            }
        }
        return message;
    }

    @Override
    public ProducerServerMessage registerProducer(ProducerServerMessage message) {
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.REGISTER_PRODUCER ||
                    producerIdToPartitions.get(smallerMessage.clientId) == null) {          // for dead producers

                smallerMessage.clientId = addNewProducerUtil();

            } else {
                allProducersIds.get(smallerMessage.clientId).updateLastSeenTime();
            }
        }
        return message;
    }

    public int addNewProducerUtil() {
        int clientId = generateToken();
        ArrayList<Integer> clientPartitions = new ArrayList<>();
        producerIdToPartitions.put(clientId, clientPartitions);
        // TODO: check if any load balancer calls needed

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
