package MQproject.broker.model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerServerMessageAboutPartitions {
    public List<BrokerServerSmallerMessage> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessage extends BaseMessage {
        public int partitionId;
        public int leaderBrokerId;
        public int replicaBrokerId;
        public boolean isReplica;

        public BrokerServerSmallerMessage(int partitionId, int leaderBrokerId, int replicaBrokerId, boolean isReplica, MessageType messageType) {
            this.partitionId = partitionId;
            this.leaderBrokerId = leaderBrokerId;
            this.replicaBrokerId = replicaBrokerId;
            this.isReplica = isReplica;
            this.messageType = messageType;
        }
    }
}
