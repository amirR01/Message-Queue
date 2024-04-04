package MQproject.broker.Caller;

import MQproject.broker.model.message.BrokerServerMessageAboutBrokers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "myApiClient",
        url = "http://${MQproject.broker.server.address}:${MQproject.broker.server.port}"
)
public interface ServerCaller {
    @PostMapping("api/server-broker/register")
    BrokerServerMessageAboutBrokers registerToServer(@RequestBody BrokerServerMessageAboutBrokers message);

    @GetMapping("api/server-broker/list")
    BrokerServerMessageAboutBrokers getBrokersList();
}
