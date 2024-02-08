package MQproject.server.model.message;

import java.util.ArrayList;
import java.util.List;

public class ServerConsumerMessage {
    public List<ServerConsumerSmallerMessage> messages = new ArrayList<>();

    public static class ServerConsumerSmallerMessage extends BaseMessage {
        public Integer clientId;
        public Integer partitionId;
        public String data; 
        public MessageType messageType;

        public ServerConsumerSmallerMessage(int clientId, int partitionId, String data, MessageType messageType) {
            this.clientId = clientId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }
    }
}
