package MQproject.broker.Controller;

import MQproject.broker.Interface.BrokerService;
import MQproject.broker.model.message.BrokerBrokerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/broker-broker")
public class BrokerBrokerController {
    @Autowired
    private BrokerService brokerService;

    @PostMapping("/update-replicas-data")
    public ResponseEntity<Object> updateReplicasData(@RequestBody BrokerBrokerMessage message) {
        try {
            brokerService.updateReplicasData(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }
    @PostMapping("/update-partitions-head-index")
    public ResponseEntity<Object> updatePartitionsHeadIndex(@RequestBody BrokerBrokerMessage message) {
        try {
            brokerService.updatePartitionsHeadIndex(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }
}
