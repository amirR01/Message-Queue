package MQproject.client.Interface;

import MQproject.client.Implementation.Tuple;

import java.util.HashMap;

public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    public void subscribe();

    public String pull();

    HashMap<Integer, Tuple<String, Tuple<String, Integer>>> subscribe_for_the_python_client();
}
