package MQproject.broker.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerClientMessage {
    public List<BrokerClientSmallerMessage> messages = new ArrayList<>();

    public static class BrokerClientSmallerMessage extends BaseMessage {
        public int clientId;
        public int partitionId;

        public BrokerClientSmallerMessage(int clientId, int partitionId, String data, MessageType messageType) {
            this.clientId = clientId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }

    }
}
