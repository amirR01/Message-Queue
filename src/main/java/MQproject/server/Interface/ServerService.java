package MQproject.server.Interface;

import java.util.*;

import MQproject.server.Controller.ServerProducerController;
import MQproject.server.model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.model.message.ServerConsumerMessage;
import MQproject.server.model.message.ServerProducerMessage;


public interface ServerService {

    public int portNumber = 5000;

    public String ipAddress = "127.0.0.1";

    // number of brokers 
    public int brokersNumber = 10;

    public void runServer();

    public void stopServer();

    public void getClientMessage();

    public int getClientPortNumber();

    public void startLoadBalancer(String key, String value);

    public void respondProducer(int producerPortNumber);

    public ServerProducerMessage handleProduction(ServerProducerMessage message);

    public ServerConsumerMessage handleSubscription(ServerConsumerMessage message);

    public BrokerServerMessageAboutPartitions handleNewPartitions(BrokerServerMessageAboutPartitions message);

}
