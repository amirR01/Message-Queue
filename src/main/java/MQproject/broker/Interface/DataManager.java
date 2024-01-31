package MQproject.broker.Interface;


import MQproject.broker.model.dataManagerModels.DataManagementConfig;
import org.springframework.stereotype.Service;

@Service
public interface DataManager {
    public void addMessage(String message,int partitionId);

    public String readMessage(int partitionId);

    public void addPartition(int partitionId, byte[] partitionData, int headIndex, boolean isReplica);

    public void deletePartition(int partitionId);

    public void makePartitionReplica(int partitionId);

    public void makePartitionPrimary(int partitionId);

    public DataManagementConfig getConfig();

    public void setConfig(DataManagementConfig config);
}
