package MQproject.server.Model.Message;

import java.util.ArrayList;
import java.util.List;
public class BrokerServerMessageAboutClients {
    public ArrayList<BrokerServerSmallerMessageAboutClients> messages = new ArrayList<>();

    public static class BrokerServerSmallerMessageAboutClients extends BaseMessage {
        public Integer ClientId;
        public List<Integer> partitions;

        public BrokerServerSmallerMessageAboutClients(Integer ClientId, List<Integer> partitions, MessageType messageType) {
            this.ClientId = ClientId;
            this.partitions = partitions;
            this.messageType = messageType;
        }
    }
}
