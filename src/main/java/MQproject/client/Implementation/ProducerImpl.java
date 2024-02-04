package MQproject.client.Implementation;

import MQproject.client.Interface.NetworkHandlerInterface;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.UnknownHostException;
import java.util.HashMap;



@Component
public class ProducerImpl implements Producer {
    @Autowired
    private NetworkHandlerInterface networkHandler;

    private HashMap<String, Tuple<String, Integer>> addressMap;

    @Override
    public void runProducer() {

    }

    @Override
    public void stopProducer() {
        // Add your logic to stop the producer here
    }

    @Override
    public void produceMessage(String message) {
        Tuple<String, Integer> ip_port = addressMap.get("broker");
        networkHandler.sendMessage(ip_port.getSecond(), message);
    }

    @Override
    public void listenForChangesFromServer() {
        new Thread(() -> {
            while (true) {
                Tuple<String, Integer> server_ip_port = addressMap.get("server");
                String response = networkHandler.readMessage(server_ip_port.getSecond());
                if (response != null) {
                    String[] response_split = response.split(" ");
                    if (response_split[0].equals("update")) {
                        addressMap.put(response_split[1], new Tuple<String,Integer>(response_split[2], Integer.parseInt(response_split[3])));
                    }
                }
            }
        }).start();
    }

    @Override
    public void connectToServer(Object server) throws UnknownHostException {
        Tuple<String, Integer> server_ip_port = addressMap.get("server");
        networkHandler.sendMessage(server_ip_port.getSecond(), "connect");
        while (true){
            String response = networkHandler.readMessage(server_ip_port.getSecond());
            if (response != null){
                String[] response_split = response.split(" ");
                if (response_split[0].equals("connect")){
                    Tuple<String, Integer> pair = new Tuple<>("example", 123);
                    addressMap.put(response_split[1], pair);
                }
            }
        }

    }

    @Override
    public void disconnectFromServer() {
        //this.server = null;
        // Add your logic to disconnect from the server here
    }

}