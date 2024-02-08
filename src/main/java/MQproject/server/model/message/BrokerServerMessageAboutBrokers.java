package MQproject.server.model.message;

import java.util.ArrayList;

public class BrokerServerMessageAboutBrokers {
    public ArrayList<BrokerServerSmallerMessageAboutBrokers> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutBrokers extends BaseMessage {
        public Integer brokerId;
        public String brokerIp;
        public Integer brokerPort;

        public BrokerServerSmallerMessageAboutBrokers(Integer brokerId, String brokerIp, int brokerPort, MessageType messageType) {
            this.brokerId = brokerId;
            this.brokerIp = brokerIp;
            this.brokerPort = brokerPort;
            this.messageType = messageType;
        }
    }
}