package MQproject.broker.model.dataManagerModels;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataManagementConfig {
    @Value("${MQproject.broker.partitions.address}")
    private String partitionsAddress;

    public String getPartitionsAddress() {
        return partitionsAddress;
    }

}
