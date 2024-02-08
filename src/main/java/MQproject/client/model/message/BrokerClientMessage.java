package MQproject.client.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerClientMessage {
    public List<BrokerClientSmallerMessage> messages = new ArrayList<>();

    public static class BrokerClientSmallerMessage extends BaseMessage {
        public Integer clientId;
        public Integer partitionId;

        public BrokerClientSmallerMessage(Integer clientId, Integer partitionId, String data, MessageType messageType) {
            this.clientId = clientId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }

    }
}
