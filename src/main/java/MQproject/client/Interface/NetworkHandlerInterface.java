package MQproject.client.Interface;

import org.springframework.stereotype.Service;

@Service
public interface NetworkHandlerInterface {
    int connect(String ip, int port);
    void disconnect(String ip, int port);
    String readMessage(int localPort);
    void sendMessage(int localPort, String message);
}