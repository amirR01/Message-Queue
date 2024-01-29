package MQproject.client.Interface;

import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
@Service
public interface NetworkHandlerInterface {
    public void readNetwork(int serverPortNumber, String ipAddress) throws UnknownHostException;

    public void writeNetwork(String message, int serverPortNumber, String ipAddress);
}
