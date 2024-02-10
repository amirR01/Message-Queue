package MQproject.server.Model.Message;

import java.util.*;

public class ServerProducerMessage {
    public List<ServerProducerMessage> messages = new ArrayList<>();

    public static class ServerProducerSmallerMessage extends BaseMessage {
        public Integer clientId;
        public Integer partitionId;
        public String data; 
        public MessageType messageType;

        public ServerProducerSmallerMessage(int clientId, int partitionId, String data, MessageType messageType) {
            this.clientId = clientId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }
    }
}