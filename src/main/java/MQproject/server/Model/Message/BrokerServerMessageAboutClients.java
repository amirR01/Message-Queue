package MQproject.server.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class BrokerServerMessageAboutClients {
    public ArrayList<BrokerServerSmallerMessageAboutClients> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutClients extends BaseMessage {
        public Integer clientId;
        public List<Integer> partitions;

        public BrokerServerSmallerMessageAboutClients(Integer clientId, List<Integer> partitions, MessageType messageType) {
            this.clientId = clientId;
            this.partitions = partitions;
            this.messageType = messageType;
        }
    }
}
