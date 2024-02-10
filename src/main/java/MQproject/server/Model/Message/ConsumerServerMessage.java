package MQproject.server.Model.Message;


import java.util.ArrayList;

public class ConsumerServerMessage {
    public ArrayList<ConsumerServerSmallerMessage> messages = new ArrayList<>();

    public static class ConsumerServerSmallerMessage extends BaseMessage {
        public Integer clientId;
        public Integer brokerId;         // null
        public String brokerIp;          // null
        public Integer brokerPort;       // null
        public Integer partitionId;     // null
        // TODO: sync ConsumerServerSmallerMessage in client and broker too

        public ConsumerServerSmallerMessage(Integer ClientId, Integer brokerId, String brokerIp, int brokerPort, int partitionId, MessageType messageType) {
            this.clientId = ClientId;
            this.brokerId = brokerId;
            this.brokerIp = brokerIp;
            this.brokerPort = brokerPort;
            this.partitionId = partitionId;
            this.messageType = messageType;
        }
    }
}