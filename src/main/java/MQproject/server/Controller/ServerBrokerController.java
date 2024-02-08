package MQproject.server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import MQproject.server.Interface.ServerService;
import MQproject.server.Model.message.BrokerServerMessageAboutPartitions;
import MQproject.server.Model.message.BrokerServerMessageAboutBrokers;


@RestController
@RequestMapping("/api/server-broker")
public class ServerBrokerController {
    @Autowired
    private ServerService serverService;

    @PostMapping("/register-broker")
    public ResponseEntity<BrokerServerMessageAboutBrokers> registerBroker(@RequestBody BrokerServerMessageAboutBrokers message) {
        BrokerServerMessageAboutBrokers response = serverService.registerBroker(message);
        return ResponseEntity.ok(response);
    }
//    @PostMapping("/list")
    // TODO: retrun list of all brokers
}