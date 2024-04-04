package MQproject.server.Model;

public class Broker {
    private String ip;
    private int port;
    private int id;
    private long lastSeenTime; 

    public Broker(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        updateLastSeenTime();
    }

    public void updateLastSeenTime() {
        this.lastSeenTime = System.currentTimeMillis();
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }
}
