package MQproject.client.Interface;

public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    public void subscribe(Object server);

    public void unsubscribe(Object server);

    public void pull(Object server);

    public void push(Object server);

}
