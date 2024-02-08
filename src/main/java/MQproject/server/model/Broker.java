package MQproject.server.model;

public class Broker {
    private String ip;
    private int port;
    private int id;

    public Broker(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
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
