package MQproject.client.Interface;

import java.net.UnknownHostException;

public interface NetworkHandlerInterface {
    public void readNetwork(int serverPortNumber, String ipAddress) throws UnknownHostException;

    public void writeNetwork(String message, int serverPortNumber, String ipAddress);
}
