package MQproject.client.Interface;

public interface Client {

    public int serverPortNumber = 5000;

    public String ipAddress = "127.0.0.1";

    public void runClient();

    public void stopClient();

    public void subscribe(Object server);

    public void unsubscribe(Object server);

    public void pull(Object server);

    public void push(Object server, String key, String value);

}
