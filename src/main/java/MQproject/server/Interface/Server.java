package MQproject.server.Interface;

import java.io.*;
import java.util.*;

public interface Server {

    public int portNumber = 5000;

    public String ipAddress = "127.0.0.1";

    public void runServer();

    public void stopServer();

    public void getClientMessage();

    public int getClientPortNumber();

    public void startLoadBalancer(String key, String value);

    public void respondProducer(int producerPortNumber);

    public void respondSubscription(ArrayList<String> broker_ips);


}
