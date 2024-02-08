package MQproject.client.model.message;

import java.util.ArrayList;

public class ClientServerMessageAboutConsumers {
    public ArrayList<ClientServerSmallerMessageAboutConsumers> messages = new ArrayList<>();

    public static class ClientServerSmallerMessageAboutConsumers extends BaseMessage {
        public Integer consumerId;
        public String consumerIp;
        public Integer consumerPort;

        public ClientServerSmallerMessageAboutConsumers(Integer consumerId, String consumerIp, int consumerPort
                , MessageType messageType) {
            this.consumerId = consumerId;
            this.consumerIp = consumerIp;
            this.consumerPort = consumerPort;
            this.messageType = messageType;
        }
    }
}
