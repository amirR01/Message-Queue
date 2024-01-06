package MQproject.client.Interface;

public interface HandleServerConnection {
    public void connectToServer(Object server);

    public void disconnectFromServer();

    public Object getConnectedServerInfo();

}
