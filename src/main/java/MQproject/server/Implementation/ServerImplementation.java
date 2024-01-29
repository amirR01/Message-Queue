package MQproject.server.Implementation;

import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MQproject.server.Interface.Server;

import java.io.*;
@Service
public class ServerImplementation implements Server{

    @Autowired
    private RoundRobinLoadBalancer loadBalancer;

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
        // TODO Auto-generated method stub
        // try {
        //     return portNumber;
        // } catch (Exception e) {
        //     // TODO: handle exception
        // }

        // assign a random broker to the producer key
        for (int key = 0; key < producerKeys.size(); key++) {
            String nextBroker = loadBalancer.getNextBroker();
            brokerKeys.put(producerKeys.get(key), nextBroker);
        }

        // TODO: Return that brokerKeys to producer.
    }

    @Override
    public void respondSubscription(ArrayList<String> broker_ips) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'respondSubscription'");
        
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
}
