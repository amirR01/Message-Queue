package MQproject.broker.Interface;

public interface Broker {

    public void runBroker();

    public void stopBroker();

    public Object getPartitionsAndClientsMapping();

    public void resetMapping();

}
