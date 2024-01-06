package MQproject.broker.implementation;

import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.DataManagementConfig;
import org.springframework.stereotype.Controller;

@Controller
public class DataManagerImpl implements DataManager {

    public void addMessage(String message,String partition) {
        // TODO implement here
    }

    public String readMessage(String partition) {
        // TODO implement here
        return "";
    }

    public void addPartition(String partition) {
        // TODO implement here
    }

    public DataManagementConfig getConfig() {
        // TODO implement here
        return null;
    }

    public void setConfig(DataManagementConfig config) {
        // TODO implement here
    }

}
