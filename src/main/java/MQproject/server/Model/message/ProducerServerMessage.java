package MQproject.server.model.message;

import java.util.ArrayList;

public class ProducerServerMessage {
    public ArrayList<ProducerServerSmallerMessage> messages = new ArrayList<>();

    public static class ProducerServerSmallerMessage extends BaseMessage {
        public Integer ClientId;
        public String key;
        public Integer PartitionId;     // null
        public Integer brokerId;        // null
        public String brokerIp;         // null
        public Integer brokerPort;      // null

    public ProducerServerSmallerMessage(Integer ClientId, String key, Integer PartitionId, Integer brokerId, String brokerIp, int brokerPort, MessageType messageType) {
            this.ClientId = ClientId;
            this.key = key;
            this.PartitionId = PartitionId;
            this.brokerId = brokerId;
            this.brokerIp = brokerIp;
            this.brokerPort = brokerPort;
            this.messageType = messageType;
        }
    }
}