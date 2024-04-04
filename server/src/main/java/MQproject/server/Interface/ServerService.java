package MQproject.server.Interface;

import MQproject.server.Model.Message.BrokerServerMessageAboutBrokers;
import MQproject.server.Model.Message.ConsumerServerMessage;
import MQproject.server.Model.Message.ProducerServerMessage;

public interface ServerService {


    public void runServer();

    public void stopServer();

    // public void respondProducer(int producerPortNumber);

    public ProducerServerMessage produce(ProducerServerMessage message);

    public ConsumerServerMessage subscribe(ConsumerServerMessage message);

    public BrokerServerMessageAboutBrokers registerBroker(BrokerServerMessageAboutBrokers message);

    public ConsumerServerMessage registerConsumer(ConsumerServerMessage message);

    public ProducerServerMessage registerProducer(ProducerServerMessage message);

    public BrokerServerMessageAboutBrokers listAllBrokers();

    public void informBrokerAboutConsumer(Integer brokerId, Integer consumerId);
    
    public void informBrokerAboutProducer(Integer brokerId, Integer producerId);
}
