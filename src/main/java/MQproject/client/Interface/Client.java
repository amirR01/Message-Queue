package MQproject.client.Interface;

public interface Client {
    public void runClient();

    public void stopClient();

    public void subscribe(Object server);

    public void unsubscribe(Object server);

    public void pull(Object server);

    public void push(Object server);

}
