package MQproject.broker.Implementation;

import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.dataManagerModels.DataManagementConfig;
import MQproject.broker.model.dataManagerModels.Partition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;

@Service
public class DataManagerImpl implements DataManager {
    @Autowired
    private DataManagementConfig config;
    private HashMap<Integer, Partition> partitions = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    // TODO: remove read messages and free the disk
    // kafka uses segments to handle files not getting to big.

    @PostConstruct
    public void init() {
        try {
            loadPartitionObjectFromFile();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's requirements
        }
    }

    // TODO(): search to know it that reduce performance that all files are in same directory
    @Scheduled(fixedRate = 60000)
    private void storePartitionObjectInFile() throws IOException {
        // convert partition object to json
        String partitionJson = mapper.writeValueAsString(partitions);
        File file = new File(config.getPartitionsAddress() + "partitions.json");
        // write the data to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(partitionJson);
        writer.close();
    }

    private void loadPartitionObjectFromFile() throws IOException {
        String fileAddress = config.getPartitionsAddress() + "partitions.json";
        File file = new File(fileAddress);
        if (file.exists()) {
            // read the data from the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String partitionJson = reader.readLine();
            reader.close();
            // convert json to partition object
            TypeReference<HashMap<Integer, Partition>> typeRef = new TypeReference<HashMap<Integer, Partition>>() {
            };
            partitions = mapper.readValue(partitionJson, typeRef);
        }
    }

    public void addPartition(int partitionId, int leaderBrokerId, int replicaBrokerId, String partitionData, int headIndex, boolean isReplica) {
        // check if this partition already exists
        if (partitions.containsKey(partitionId)) {
            throw new RuntimeException("partition already exists");
        }
        String partitionsAddress = config.getPartitionsAddress() + partitionId + ".txt";
        Partition newPartition = new Partition(partitionId, leaderBrokerId, replicaBrokerId, headIndex, isReplica, partitionsAddress);
        partitions.put(partitionId, newPartition);
        // create a file for the new partition and write the data to it
        System.out.println(partitionsAddress);
        File file = new File(partitionsAddress);
        // write the data to the file
        try {
            // create a new file
            file.createNewFile();
            // write the data to the file
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (partitionData != null) {
                fileOutputStream.write(partitionData.getBytes());
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String message, int partitionId, Boolean isReplica) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        if (partition.isReplica == isReplica) {
            throw new RuntimeException("partition is not available for this operation");
        }
        // write the message to the file
        try (RandomAccessFile file = new RandomAccessFile(partition.partitionsAddress, "r")) {
            file.seek(file.length());
            // Write the data
            file.write((message + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage(int partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        String message = "";
        try (RandomAccessFile file = new RandomAccessFile(partition.partitionsAddress, "r")) {
            file.seek(partition.headIndex);
            // check if there is a new message
            if (file.getFilePointer() == file.length()) {
                return null;
            }
            message = file.readLine();
            // update the head index
            partition.headIndex = (int) file.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void makePartitionReplica(int partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        partition.isReplica = true;
    }

    public void makePartitionPrimary(int partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        partition.isReplica = false;
    }

    public Integer getReplicaBrokerId(int partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        return partition.replicaBrokerId;
    }

    public void updateHeadIndex(Integer partitionId, Integer headIndex) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        if (!partition.isReplica) {
            throw new RuntimeException("partition is not available for this operation");
        }
        if (headIndex < partition.headIndex) {
            throw new RuntimeException("invalid head index");
        }
        partition.headIndex = headIndex;
    }

    public Integer getHeadIndex(Integer partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        return partition.headIndex;
    }

    public void removePartition(int partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        // delete the file
        File file = new File(partition.partitionsAddress);
        file.delete();
        // delete the partition from the map
        partitions.remove(partitionId);
    }

    public Tuple<Partition, String> getPartition(Integer partitionId) {
        Partition partition = partitions.get(partitionId);
        if (partition == null) {
            throw new RuntimeException("partition not found");
        }
        String partitionData;
        try (RandomAccessFile file = new RandomAccessFile(partition.partitionsAddress, "r")) {
            file.seek(0);
            byte[] data = new byte[(int) file.length()];
            file.read(data);
            partitionData = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
            partitionData = null;
        }
        return new Tuple<>(partition, partitionData);
    }

    public DataManagementConfig getConfig() {
        return config;
    }

    public void setConfig(DataManagementConfig config) {
        this.config = config;
    }

}
