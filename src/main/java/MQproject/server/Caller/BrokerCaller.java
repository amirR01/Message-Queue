package MQproject.server.Caller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import MQproject.server.model.message.ConsumerServerMessage;


public interface BrokerCaller {
    @PostMapping("api/server/inform")
    ConsumerServerMessage informBroker(@RequestBody ConsumerServerMessage message);
    // TODO: inform about the partitions
}