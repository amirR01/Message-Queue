package MQproject.client.Controller;

import MQproject.client.Implementation.Tuple;
import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
        return "Pushed to Python";
    }

    @PostMapping("/subscribe")
    public String subscribeToPython() {
        HashMap<Integer, Tuple<String, Tuple<String, Integer>>> addressmap = consumer.subscribe_for_the_python_client();
        // now pull every n secs

        return "Subscribed to Python";
    }

    @PostMapping("/pull-as-subscriber")
    public String pullAsSubscriberFromPython() {

        return "Pulled as subscriber from Python";
    }

}
