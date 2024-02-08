package MQproject.broker.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerBrokerMessage {
    public List<BrokerBrokerSmallerMessage> messages = new ArrayList<>();

    public static class BrokerBrokerSmallerMessage extends BaseMessage {
        public Integer brokerId;
        public Integer partitionId;


        public BrokerBrokerSmallerMessage(Integer brokerId, Integer partitionId, String data, MessageType messageType) {
            this.brokerId = brokerId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }

    }

}
