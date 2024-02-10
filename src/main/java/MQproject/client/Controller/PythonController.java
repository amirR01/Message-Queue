package MQproject.client.Controller;


import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/python")
public class PythonController {

    @Autowired
    Consumer consumer;

    @Autowired
    Producer producer;

    @PostMapping("/pull")
    public String pullFromPython() {
        String data = consumer.pull();
        return data;
    }

    @PostMapping("/push")
    public String pushToPython(String key, String message) {
        producer.produceMessage(key, message);
        return "Pushed to Queue";
    }

    @PostMapping("/subscribe")
    public String subscribeToPython() {
        consumer.subscribe_for_the_python_client();
        return "OK";
    }

    @PostMapping("/pull-as-subscriber")
    public String pullAsSubscriberFromPython() {
        List<String> messages = consumer.pull_for_the_python_client();
        return messages.toString();
    }

}
