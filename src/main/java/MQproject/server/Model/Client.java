package MQproject.server.Model;

import java.util.ArrayList;

public class Client {

    private int id;
    private long lastSeenTime; 
    private ClientType clientType;
    private ArrayList<Integer> partitions;


    public Client(int id, ClientType clientType, ArrayList<Integer> partitions) {
        this.id = id;
        this.clientType = clientType;
        this.partitions = partitions;
        updateLastSeenTime();
    }

    public void updateLastSeenTime() {
        this.lastSeenTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public ArrayList<Integer> getPartitions() {
        return partitions;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

}
