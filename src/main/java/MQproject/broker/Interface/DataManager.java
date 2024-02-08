package MQproject.broker.Interface;


import MQproject.broker.model.dataManagerModels.DataManagementConfig;
import org.springframework.stereotype.Service;

public interface DataManager {
    public void addMessage(String message, int partitionId, Boolean isReplica);

    public String readMessage(int partitionId);

    public void addPartition(int partitionId, int leaderBrokerId, int replicaBrokerId, String partitionData, int headIndex, boolean isReplica);
    public Integer getReplicaBrokerId(int partitionId);


    public void deletePartition(int partitionId);

    public void makePartitionReplica(int partitionId);

    public void makePartitionPrimary(int partitionId);

    public DataManagementConfig getConfig();

    public void setConfig(DataManagementConfig config);

    public void sendDataToReplica(String data, Integer PartitionId, Integer brokerId);

    public void updateHeadIndex(Integer partitionId, Integer headIndex);
}
