package MQproject.server.Interface;


import MQproject.server.model.message.BrokerServerMessageAboutBrokers;
import MQproject.server.model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.model.message.ConsumerServerMessage;
import MQproject.server.model.message.ProducerServerMessage;
import MQproject.server.Model.message.BrokerServerMessageAboutBrokers;
import MQproject.server.Model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.Model.message.ServerConsumerMessage;
import MQproject.server.Model.message.ServerProducerMessage;


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

    public ProducerServerMessage produce(ProducerServerMessage message);

    public ConsumerServerMessage subscribe(ConsumerServerMessage message);

    public BrokerServerMessageAboutPartitions handleNewPartitions(BrokerServerMessageAboutPartitions message);

    public BrokerServerMessageAboutBrokers registerBroker(BrokerServerMessageAboutBrokers message);

    public BrokerServerMessageAboutBrokers listAllBrokers();

    public ConsumerServerMessage informBroker(ConsumerServerMessage message);
}
