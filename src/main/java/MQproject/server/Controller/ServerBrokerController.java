package MQproject.server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import MQproject.server.Interface.ServerService;
import MQproject.server.Model.Message.BrokerServerMessageAboutBrokers;


@RestController
@RequestMapping("/api/server-broker")
public class ServerBrokerController {
    @Autowired
    private ServerService serverService;


    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<BrokerServerMessageAboutBrokers> registerBroker(@RequestBody BrokerServerMessageAboutBrokers message) {
        BrokerServerMessageAboutBrokers response = serverService.registerBroker(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/list", consumes = "application/json")
    public ResponseEntity<BrokerServerMessageAboutBrokers> listAllBrokers() {
        BrokerServerMessageAboutBrokers response = serverService.listAllBrokers();
        return ResponseEntity.ok(response);
    }

//    @PostMapping(value = "/inform-broker", consumes = "application/json")
//    public ResponseEntity<ConsumerServerMessage> informBroker(@RequestBody ConsumerServerMessage message) {
//        ConsumerServerMessage response = serverService.informBroker(message);
//        return ResponseEntity.ok(response);
//    }

}