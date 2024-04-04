package MQproject.client.model.message;
import java.util.ArrayList;

public class ClientServerMessage {
    public ArrayList<ClientServerSmallerMessage> messages = new ArrayList<>();

    public static class ClientServerSmallerMessage extends BaseMessage {
        public Integer clientId;
        public String clientIp;
        public Integer clientPort;

        public ClientServerSmallerMessage(Integer clientId, String clientIp, Integer clientPort, MessageType messageType) {
            this.clientId = clientId;
            this.clientIp = clientIp;
            this.clientPort = clientPort;
            this.messageType = messageType;
        }
    }
}
