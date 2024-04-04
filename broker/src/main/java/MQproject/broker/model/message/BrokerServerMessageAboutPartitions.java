package MQproject.broker.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerServerMessageAboutPartitions {
    public ArrayList<BrokerServerSmallerMessageAboutPartitions> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutPartitions extends BaseMessage {
        public Integer partitionId;
        public Integer leaderBrokerId;
        public Integer replicaBrokerId;

        public BrokerServerSmallerMessageAboutPartitions(Integer partitionId, Integer leaderBrokerId, Integer replicaBrokerId, MessageType messageType) {
            this.partitionId = partitionId;
            this.leaderBrokerId = leaderBrokerId;
            this.replicaBrokerId = replicaBrokerId;
            this.messageType = messageType;
        }
    }
}
