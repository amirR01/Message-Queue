package MQproject.server.Interface;

public interface Monitoring {

    public void runMonitoring();

    public void stopMonitoring();

    public void addMetrics();

    public void removeMetrics();

    public void getMetrics();

    public void resetMetrics();

}

