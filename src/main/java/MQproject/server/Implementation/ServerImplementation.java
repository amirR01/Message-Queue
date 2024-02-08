package MQproject.server.Implementation;

import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.lang3.ObjectUtils.Null;

import MQproject.server.Interface.ServerService;
import MQproject.server.model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.model.message.ServerConsumerMessage;
import MQproject.server.model.message.ServerProducerMessage;

import java.io.*;
@Service
public class ServerImplementation implements ServerService{

    @Autowired
    private RoundRobinLoadBalancer producerLoadBalancer;
    @Autowired
    private RoundRobinLoadBalancer consumerLoadBalancer;

    private HashMap<String, String> brokerKeys = new HashMap<>();  // should be set by server
    private ArrayList<String> allBrokers = new ArrayList<>();   // should be received from brokers
    private ArrayList<String> producerKeys = new ArrayList<>(); // should be received from producer
    

    public ServerImplementation() {
        // TODO: Get these brokers from server.
        allBrokers.add("Broker1");
        allBrokers.add("Broker2");
        allBrokers.add("Broker3");
    }



    public void clientHandler() {

        try {
            
            ServerSocket serverSocket = new ServerSocket(getClientPortNumber());

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                String message = getMessageToSend(2000);
                printWriter.println(message);
                printWriter.close();
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        clientHandler();
        // TODO Auto-generated method stub
    }

    @Override
    public void stopServer() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'stopServer'");
    }

    @Override
    public void getClientMessage() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getClientMessage'");
    }

    @Override
    public void startLoadBalancer(String key, String value) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'startLoadBalancer'");
    }

    @Override
    public void respondProducer(int producerPortNumber) {
        // try {
        //     return portNumber;
        // } catch (Exception e) {
        //     // TODO: handle exception
        // }

        // assign a random broker to the producer key
        for (int key = 0; key < producerKeys.size(); key++) {
            String nextBroker = producerLoadBalancer.getNextBroker();
            brokerKeys.put(producerKeys.get(key), nextBroker);
        }

        // TODO: Return that brokerKeys to producer.
    }

    @Override
    public ServerConsumerMessage handleSubscription(ServerConsumerMessage message) {
        // throw new UnsupportedOperationException("Unimplemented method 'respondSubscription'");

        ArrayList<String> broker_ips = new ArrayList<>();

        for (int broker = 0; broker < brokersNumber; broker++) {
            String nextBroker = consumerLoadBalancer.getNextBroker();
            broker_ips.add(nextBroker);
        }

        // return broker_ips;
        return null;
    }

    @Override
    public int getClientPortNumber() {
        // TODO Auto-generated method stub
        int result_port_number = 2000;
        try {
            return result_port_number;
        } catch (Exception e) {
            // TODO: add approperiate exeption
            e.printStackTrace();
            return 0;
        }
        // throw new UnsupportedOperationException("Unimplemented method 'getClientPortNumber'");
    }



    @Override
    public ServerProducerMessage handleProduction(ServerProducerMessage message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleProduction'");
    }



    @Override
    public BrokerServerMessageAboutPartitions handleNewPartitions(BrokerServerMessageAboutPartitions message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleNewPartitions'");
    }
}
