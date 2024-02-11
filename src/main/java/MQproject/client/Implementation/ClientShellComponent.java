package MQproject.client.Implementation;

import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ClientShellComponent {
    @Autowired
    public Producer producer;

    @Autowired
    public Consumer consumer;

    @ShellMethod(key = "produce")
    public void produce(@ShellOption String key,
                        @ShellOption String value) {
        System.out.println(key);
        System.out.println(value);
        producer.produceMessage(key, value);
    }

}
