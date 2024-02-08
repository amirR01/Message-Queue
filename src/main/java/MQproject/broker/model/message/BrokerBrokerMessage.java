package MQproject.broker.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerBrokerMessage {
    public List<BrokerBrokerSmallerMessage> messages = new ArrayList<>();

    public static class BrokerBrokerSmallerMessage extends BaseMessage {
        public Integer leaderBrokerId;
        public Integer replicaBrokerId;
        public Integer partitionId;
        public Integer headIndex;


        public BrokerBrokerSmallerMessage(Integer leaderBrokerId, Integer replicaBrokerId, Integer partitionId, Integer headIndex, String data, MessageType messageType) {
            this.leaderBrokerId = leaderBrokerId;
            this.replicaBrokerId = replicaBrokerId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }

    }

}
