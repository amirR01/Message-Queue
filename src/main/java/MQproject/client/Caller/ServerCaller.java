package MQproject.client.Caller;

import MQproject.client.model.message.ClientServerMessage;
import MQproject.client.model.message.ConsumerServerMessage;
import MQproject.client.model.message.ProducerServerMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "myApiClient",
        url = "http://${MQproject.client.server.address}:${MQproject.client.server.port}"
)
public interface ServerCaller {
    @PostMapping("api/server-producer/register")
    ClientServerMessage registerToServerForProducer(@RequestBody ClientServerMessage message);
    // subscribe
    @PostMapping("api/server-consumer/register")
    ClientServerMessage registerToServerForConsumer(@RequestBody ClientServerMessage message);
    // subscribe
    @PostMapping("api/server-consumer/subscribe")
    ConsumerServerMessage assignBroker(@RequestBody ConsumerServerMessage message);
    // produce
    @PostMapping("api/server-producer/produce")
    ProducerServerMessage assignPartition(@RequestBody ProducerServerMessage message);
}
