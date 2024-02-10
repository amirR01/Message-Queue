package MQproject.client.Interface;

import MQproject.client.Implementation.Tuple;
import org.springframework.stereotype.Service;

@Service
public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    public void subscribe();

    public String pull();

}
