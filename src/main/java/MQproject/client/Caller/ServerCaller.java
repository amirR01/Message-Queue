package MQproject.client.Caller;

import MQproject.client.model.message.ClientServerMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "myApiClient",
        url = "http://${MQproject.client.server.address}:${MQproject.client.server.port}"
)
public interface ServerCaller {
    @PostMapping("api/server-client/register")
    ClientServerMessage registerToServer(@RequestBody ClientServerMessage message);
}
