package MQproject.server.Implementation;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Broker;
import MQproject.server.Model.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServerImplementation implements ServerService {

    @Autowired
    private RoundRobinLoadBalancer producerLoadBalancer;
    // @Autowired
    // private RoundRobinLoadBalancer consumerLoadBalancer;

    // private final RestTemplate restTemplate;


    // private HashMap<String, String> brokerKeys = new HashMap<>();  // should be set by server
    private ArrayList<String> allBrokers = new ArrayList<>();   // should be received from brokers
    // private ArrayList<String> producerKeys = new ArrayList<>(); // should be received from producer
    private HashMap<Integer, Broker> brokersIds = new HashMap<>();
    private Set<Integer> generatedTokens = new HashSet<>();
    private Random random = new Random();
    private HashMap<Integer, ArrayList<Integer>> consumerIdToPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions;
    private HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions;

    public String getMessageToSend(Integer clientPort) {
        // TODO: implement it better!
        String result = "Sallam";
        return result;
    }


    public static void main(String[] args) {
        ServerImplementation s = new ServerImplementation();
        System.out.println("hello");
        s.runServer();
        System.out.println("after hello");
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


    // @Override
    // public void respondProducer(int producerPortNumber) {
    //     // try {
    //     //     return portNumber;
    //     // } catch (Exception e) {
    //     //     // TODO: handle exception
    //     // }

    //     // assign a random broker to the producer key
    //     for (int key = 0; key < producerKeys.size(); key++) {
    //         String nextBroker = producerLoadBalancer.getNextBroker();
    //         brokerKeys.put(producerKeys.get(key), nextBroker);
    //     }

    //     // TODO: Return that brokerKeys to producer.
    // }


    // public void addPartititon(Integer brokerId, Integer partitionId) {
    //     // Use the consumer IP to pull a message from the server
    //     BrokerClientMessage bigMessage = new BrokerClientMessage();
    //     bigMessage.messages.add(
    //             new BrokerClientMessage.BrokerClientSmallerMessage(
    //                     myConsumerID, null, null, MessageType.CONSUME_MESSAGE));

    //     ResponseEntity<BrokerClientMessage> response = restTemplate.postForEntity(
    //             "http://" + addressMap.get(brokerId).getFirst() + ":"
    //                     + addressMap.get(brokerId).getSecond()
    //                     + "/api/broker-client/consume-message",
    //             bigMessage,
    //             BrokerClientMessage.class
    //     );
    //     BrokerClientMessage responseBody = response.getBody();

    //     // ASYNC function call on response body
    //     CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    //         commandLineInterface.printMessage(responseBody.messages);
    //     });

    // }


    @Override
    public ConsumerServerMessage informBroker(ConsumerServerMessage message) {
        return message;
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

            }
        }
        // TODO: inform broker that a new subscriber added.
        informBroker(message);
        return message;
    }


    @Override
    public ProducerServerMessage produce(ProducerServerMessage message) {
        for (ProducerServerMessage.ProducerServerSmallerMessage smallerMessage : message.messages) {
            if (smallerMessage.messageType == MessageType.PRODUCE_MESSAGE) {
                // TODO: add a better Load Balancer
                // TODO: save the producers id and the partition id in a map
                Broker toAssignedBroker = producerLoadBalancer.getNextBroker();
                smallerMessage.brokerId = toAssignedBroker.getId();
                smallerMessage.brokerPort = toAssignedBroker.getPort();
                smallerMessage.brokerIp = toAssignedBroker.getIp();
                // TODO: put approperiate partition in smallerMessage.PartitionId
                smallerMessage.PartitionId = null;
            }
        }
        // TODO: inform the brokers
        return message;
    }

    @Override
    public BrokerServerMessageAboutPartitions handleNewPartitions(BrokerServerMessageAboutPartitions message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleNewPartitions'");
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
