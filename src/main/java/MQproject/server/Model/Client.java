package MQproject.server.Model;

import java.util.ArrayList;

public class Client {

    private int id;
    private long lastSeenTime; 
    private ClientType clientType;


    public Client(int id, ClientType clientType) {
        this.id = id;
        this.clientType = clientType;
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


    public long getLastSeenTime() {
        return lastSeenTime;
    }

}
