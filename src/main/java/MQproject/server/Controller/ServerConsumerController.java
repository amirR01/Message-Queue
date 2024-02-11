package MQproject.server.Controller;

import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Message.ConsumerServerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server-consumer")
public class ServerConsumerController {
    @Autowired
    private ServerService serverService;

    @PostMapping(value = "/subscribe", consumes = "application/json")
    public ResponseEntity<Object> subscribe(@RequestBody ConsumerServerMessage message) {
        try {
            ConsumerServerMessage responseMessage = serverService.subscribe(message);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        // not reachable
        return null;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ConsumerServerMessage> registerConsumer(@RequestBody ConsumerServerMessage message) {
        ConsumerServerMessage response = serverService.registerConsumer(message);
        return ResponseEntity.ok(response);
    }
}
