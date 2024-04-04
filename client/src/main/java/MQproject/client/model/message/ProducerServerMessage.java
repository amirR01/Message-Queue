package MQproject.client.model.message;

import java.util.ArrayList;

public class ProducerServerMessage {
    public ArrayList<ProducerServerSmallerMessage> messages = new ArrayList<>();

    public static class ProducerServerSmallerMessage extends BaseMessage {
        public Integer clientId;
        public String key;
        public Integer partitionId;
        public Integer brokerId;
        public String brokerIp;
        public Integer brokerPort;

public ProducerServerSmallerMessage(Integer clientId, String key, Integer partitionId, Integer brokerId, String brokerIp, Integer brokerPort, MessageType messageType) {
            this.clientId = clientId;
            this.key = key;
            this.partitionId = partitionId;
            this.brokerId = brokerId;
            this.brokerIp = brokerIp;
            this.brokerPort = brokerPort;
            this.messageType = messageType;
        }
    }
}
