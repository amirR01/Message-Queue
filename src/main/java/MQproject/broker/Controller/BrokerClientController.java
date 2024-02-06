package MQproject.broker.Controller;

import MQproject.broker.Interface.BrokerService;
import MQproject.broker.model.message.BrokerClientMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/broker-client")
public class BrokerClientController {
    @Autowired
    private BrokerService brokerService;

    @PostMapping(value = "/produce-message", consumes = "application/json")
    public ResponseEntity<BrokerClientMessage> produceMessage(@RequestBody BrokerClientMessage message) {
        try {
            brokerService.produceMessage(message);
            return ResponseEntity.ok(new BrokerClientMessage());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        // not reachable
        return null;
    }

    @PostMapping(value = "/consume-message", consumes = "application/json")
    public ResponseEntity<BrokerClientMessage> consumeMessage(@RequestBody BrokerClientMessage message) {
        try {
            BrokerClientMessage responseMessage = brokerService.consumeMessage(message);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        // not reachable
        return null;
    }

}
