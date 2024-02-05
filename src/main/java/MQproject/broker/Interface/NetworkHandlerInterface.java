package MQproject.broker.Interface;

import org.springframework.stereotype.Service;

@Service
public interface NetworkHandlerInterface {
    int connect(String ip, int port);
    void disconnect(String ip, int port);
    String readMessage(String ip, int port);
    void sendMessage(String ip, int port, String message);
}