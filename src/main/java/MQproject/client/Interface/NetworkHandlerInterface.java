package MQproject.client.Interface;

import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
@Service
public interface NetworkHandlerInterface {
    int connect(String address, int port);
    void disconnect(int localPort);
    String readMessage(int localPort);
    void sendMessage(int localPort, String message);
}