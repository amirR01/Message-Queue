package MQproject.client.model.message;

import java.util.ArrayList;

public class ConsumerServerMessage {
    public ArrayList<ConsumerServerSmallerMessage> messages = new ArrayList<>();

    public static class ConsumerServerSmallerMessage extends BaseMessage {
        public Integer ClientId;
        public Integer brokerId;
        public String brokerIp;
        public Integer brokerPort;
        
        public String key;
        public ConsumerServerSmallerMessage(Integer ClientId, String key, Integer brokerId, String brokerIp, Integer brokerPort, MessageType messageType) {
            this.ClientId = ClientId;
            this.brokerId = brokerId;
            this.brokerIp = brokerIp;
            this.brokerPort = brokerPort;
            this.messageType = messageType;
            this.key = key;
        }
    }
}
