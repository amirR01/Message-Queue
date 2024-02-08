package MQproject.client.Caller;

import MQproject.client.model.message.ClientServerMessageAboutConsumers;
import MQproject.client.model.message.ClientServerMessageAboutProducers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "myApiClient",
        url = "http://${MQproject.broker.server.address}:${MQproject.broker.server.port}"
)
public interface ServerCaller {
    @PostMapping("api/server-client/register")
    ClientServerMessageAboutProducers registerToServer(@RequestBody ClientServerMessageAboutProducers message);
    ClientServerMessageAboutConsumers registerToServer(@RequestBody ClientServerMessageAboutConsumers message);
}
