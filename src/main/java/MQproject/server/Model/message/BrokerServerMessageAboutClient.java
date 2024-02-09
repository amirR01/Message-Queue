package MQproject.server.Model.message;

import java.util.ArrayList;
import java.util.List;

public class BrokerServerMessageAboutClient {
    public List<BrokerServerSmallerMessageAboutClient> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutClient extends BaseMessage {
        public int partitionId;
        public int brokerId;
        public int clientId;

        public BrokerServerSmallerMessageAboutClient(int partitionId, int brokerId, int clientId, MessageType messageType) {
            this.partitionId = partitionId;
            this.brokerId = brokerId;
            this.clientId = clientId;
            this.messageType = messageType;
        }
    }
}
