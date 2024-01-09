package MQproject.broker.model.dataManagerModels;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class DataManagementConfig {
    @Value("${MQproject.broker.partitions.address}")
    private String partitionsAddress;

    public String getPartitionsAddress() {
        return partitionsAddress;
    }
}
