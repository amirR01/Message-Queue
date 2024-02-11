package MQproject.client.Implementation;

import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ClientShellComponent {
    @Autowired
    public Producer producer;

    @Autowired
    public Consumer consumer;

    @Value("${MQproject.client.consumer}")
    public Boolean isConsumer;

    @Value("${MQproject.client.producer}")
    public Boolean isProducer;


    @ShellMethod(key = "produce")
    public void produce(@ShellOption String key,
                        @ShellOption String value) {
        if (isProducer){
            producer.produceMessage(key, value);
        }
    }

    @ShellMethod(key = "pull")
    public void pull() {
        if (isConsumer){
            consumer.pull();
        }
    }

    @ShellMethod(key = "subscribe")
    public void subscribe() {
        if (isConsumer){
            consumer.subscribe();
        }
    }

}
