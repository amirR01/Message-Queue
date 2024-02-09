package MQproject.server.Interface;

import MQproject.server.Model.message.BrokerServerMessageAboutBrokers;
import MQproject.server.Model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.Model.message.BrokerServerMessageAboutClient;
import MQproject.server.Model.message.ConsumerServerMessage;
import MQproject.server.Model.message.ProducerServerMessage;

public interface ServerService {

    public int portNumber = 5000;

    public String ipAddress = "127.0.0.1";

    // number of brokers 
    public int brokersNumber = 10;

    public void runServer();

    public void stopServer();

    // public void respondProducer(int producerPortNumber);

    public ProducerServerMessage produce(ProducerServerMessage message);

    public ConsumerServerMessage subscribe(ConsumerServerMessage message);

    public BrokerServerMessageAboutBrokers registerBroker(BrokerServerMessageAboutBrokers message);

    public BrokerServerMessageAboutBrokers listAllBrokers();

    public void informBroker(Integer brokerId, BrokerServerMessageAboutClient message);
}
