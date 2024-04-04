package MQproject.broker.Interface;

import MQproject.broker.Implementation.Tuple;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;

public interface BrokerServerService {

    public void runBroker();

    public void stopBroker();

    public void addPartition(BrokerServerMessageAboutPartitions message);

    public void movePartition(BrokerServerMessageAboutPartitions message);

    public void clonePartition(BrokerServerMessageAboutPartitions message);

    public void removePartition(BrokerServerMessageAboutPartitions message);

    public void becomeLeader(BrokerServerMessageAboutPartitions message);

    public Tuple<String, Integer> getBrokerAddress(Integer replicaBrokerId);

    public Integer getMyBrokerId();
}
