package MQproject.client.model.message;
import java.util.ArrayList;

public class ClientServerMessage {
    public ArrayList<ClientServerSmallerMessage> messages = new ArrayList<>();

    public static class ClientServerSmallerMessage extends BaseMessage {
        public Integer ClientId;
        public String ClientIp;
        public Integer ClientPort;

        public ClientServerSmallerMessage(Integer ClientId, String ClientIp, Integer ClientPort, MessageType messageType) {
            this.ClientId = ClientId;
            this.ClientIp = ClientIp;
            this.ClientPort = ClientPort;
            this.messageType = messageType;
        }
    }
}
