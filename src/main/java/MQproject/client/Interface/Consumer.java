package MQproject.client.Interface;

import org.springframework.stereotype.Service;

@Service
public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    public void subscribe(Object server);

    public void unsubscribe(Object server);

    public void pull(Object server);

    public void push(Object server);

    public void connectToServer();

}
