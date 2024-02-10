package MQproject.server.Model;

import java.util.ArrayList;

public class Client {

    private int id;
    private ArrayList<Integer> partitions;


    public Client(int id, ArrayList<Integer> partitions) {
        this.id = id;
        this.partitions = partitions;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPartitions() {
        return partitions;
    }
}
