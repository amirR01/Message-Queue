package MQproject.broker.Controller;

import MQproject.broker.Interface.BrokerClientService;
import MQproject.broker.Interface.BrokerServerService;
import MQproject.broker.model.message.BrokerServerMessageAboutClients;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/broker-server")
public class BrokerServerController {
    @Autowired
    private BrokerServerService brokerServerService;
    @Autowired
    private BrokerClientService brokerClientService;

    @PostMapping(value = "/add-partition", consumes = "application/json")
    public ResponseEntity<Object> addPartition(@RequestBody BrokerServerMessageAboutPartitions message) {
        try {
            brokerServerService.addPartition(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }

    @PostMapping(value = "/move-partition", consumes = "application/json")
    public ResponseEntity<Object> movePartition(@RequestBody BrokerServerMessageAboutPartitions message) {
        try {
            brokerServerService.movePartition(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }

    @PostMapping(value = "/clone-partition", consumes = "application/json")
    public ResponseEntity<Object> clonePartition(@RequestBody BrokerServerMessageAboutPartitions message) {
        // add replica
        try {
            brokerServerService.clonePartition(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }

    @PostMapping(value = "/remove-partition", consumes = "application/json")
    public ResponseEntity<Object> removePartition(@RequestBody BrokerServerMessageAboutPartitions message) {
        try {
            brokerServerService.removePartition(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }

    @PostMapping(value = "/become-partition-leader", consumes = "application/json")
    public ResponseEntity<Object> becomeLeader(@RequestBody BrokerServerMessageAboutPartitions message) {
        try {
            brokerServerService.becomeLeader(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        return null;
    }
    @PostMapping(value = "/update-clients", consumes = "application/json")
    public ResponseEntity<BrokerServerMessageAboutClients> updateClients(@RequestBody BrokerServerMessageAboutClients message) {
        try {
            brokerClientService.updateClients(message);
            return ResponseEntity.ok(new BrokerServerMessageAboutClients());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            ResponseEntity.status(500).body(errorMessage);
        }
        // not reachable
        return null;
    }

}
