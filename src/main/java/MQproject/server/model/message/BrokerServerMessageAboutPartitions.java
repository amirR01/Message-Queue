package MQproject.server.model.message;

import java.util.*;

public class BrokerServerMessageAboutPartitions {
    public List<BrokerServerSmallerMessageAboutPartitions> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutPartitions extends BaseMessage {
        public int partitionId;
        public int leaderBrokerId;
        public int replicaBrokerId;
        public boolean isReplica;

        public BrokerServerSmallerMessageAboutPartitions(int partitionId, int leaderBrokerId, int replicaBrokerId, boolean isReplica, MessageType messageType) {
            this.partitionId = partitionId;
            this.leaderBrokerId = leaderBrokerId;
            this.replicaBrokerId = replicaBrokerId;
            this.isReplica = isReplica;
            this.messageType = messageType;
        }
    }
}