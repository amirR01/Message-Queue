package MQproject.client.Interface;

import org.springframework.stereotype.Service;

@Service
public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    public void subscribe();

    public void pull();

}
