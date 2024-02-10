package MQproject.server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Message.ProducerServerMessage;

@RestController
@RequestMapping("/api/server-producer")
public class ServerProducerController {
    @Autowired
    private ServerService serverService;

    @PostMapping(value = "/produce", consumes = "application/json")
    public ResponseEntity<Object> produce(@RequestBody ProducerServerMessage message) {
// TODO: inform about new produce update brokers data about consumers.
        try {
            ProducerServerMessage responseMessage = serverService.produce(message);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        // not reachable
        return null;
    }

    @PostMapping(value = "/register-producer", consumes = "application/json")
    public ResponseEntity<ProducerServerMessage> registerProducer(@RequestBody ProducerServerMessage message) {
        ProducerServerMessage response = serverService.registerProducer(message);
        return ResponseEntity.ok(response);
    }
}
