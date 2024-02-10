package MQproject.client.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/python")
public class PythonController {
    @PostMapping("/pull")
    public String pullFromPython() {
        return "Pulled from Python";
    }

    @PostMapping("/push")
    public String pushToPython() {
        return "Pushed to Python";
    }

    @PostMapping("/subscribe")
    public String subscribeToPython() {
        return "Subscribed to Python";
        // subscribe here
    }

    @PostMapping("/pull-as-subscriber")
    public String pullAsSubscriberFromPython() {
        return "Pulled as subscriber from Python";
    }
}
