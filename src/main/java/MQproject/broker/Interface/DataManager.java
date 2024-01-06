package MQproject.broker.Interface;


import MQproject.broker.model.DataManagementConfig;
import org.springframework.stereotype.Service;

@Service
public interface DataManager {
    public void addMessage(String message,String partition);

    public String readMessage(String partition);

    public void addPartition(String partition);

    public DataManagementConfig getConfig();

    public void setConfig(DataManagementConfig config);
}
