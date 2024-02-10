package MQproject.server.Interface;

import MQproject.server.Model.message.BrokerServerMessageAboutBrokers;
import MQproject.server.Model.message.BrokerServerMessageAboutClients;
import MQproject.server.Model.message.ConsumerServerMessage;
import MQproject.server.Model.message.ProducerServerMessage;

public interface ServerService {

    // number of brokers (Test)
    public int brokersNumber = 10;

    public void runServer();

    public void stopServer();

    // public void respondProducer(int producerPortNumber);

    public ProducerServerMessage produce(ProducerServerMessage message);

    public ConsumerServerMessage subscribe(ConsumerServerMessage message);

    public BrokerServerMessageAboutBrokers registerBroker(BrokerServerMessageAboutBrokers message);

    public ConsumerServerMessage registerConsumer(ConsumerServerMessage message);

    public ProducerServerMessage registerProducer(ProducerServerMessage message);

    public BrokerServerMessageAboutBrokers listAllBrokers();

    public void informBroker(Integer brokerId, BrokerServerMessageAboutClients message);
}
