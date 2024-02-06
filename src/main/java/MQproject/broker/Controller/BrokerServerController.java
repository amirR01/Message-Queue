package MQproject.broker.Controller;

import MQproject.broker.Interface.BrokerService;
import MQproject.broker.model.message.BrokerServerMessageAboutPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/broker-server")
public class BrokerServerController {
    @Autowired
    private BrokerService brokerService;

    @PostMapping(value = "/add-partition", consumes = "application/json")
    public ResponseEntity<Object> addPartition(@RequestBody BrokerServerMessageAboutPartitions message) {
        brokerService.handleNewInformationAboutPartitions(message);
        return ResponseEntity.ok().build();
    }
}
