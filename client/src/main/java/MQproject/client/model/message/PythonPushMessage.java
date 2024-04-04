package MQproject.client.model.message;

public class PythonPushMessage {
    public String key;
    public String message;

    public PythonPushMessage(String key, String message) {
        this.key = key;
        this.message = message;
    }
}
