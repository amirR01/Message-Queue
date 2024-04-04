package MQproject.client.Interface;

import java.util.List;

public interface Consumer {
    public void runConsumer();

    public void stopConsumer();

    List<String> pull_for_the_python_client();

    public void subscribe();

    public String pull();

    void subscribe_for_the_python_client();
}
