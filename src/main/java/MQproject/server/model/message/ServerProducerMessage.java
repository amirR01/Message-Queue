package MQproject.server.model.message;

public class ServerProducerMessage {
    public List<BrokerClientSmallerMessage> messages = new ArrayList<>();

    public static class BrokerClientSmallerMessage extends BaseMessage {
        public Integer clientId;
        public Integer partitionId;

        public ServerProducerSmallerMessage(int clientId, int partitionId, String data, MessageType messageType) {
            this.clientId = clientId;
            this.partitionId = partitionId;
            this.data = data;
            this.messageType = messageType;
        }

    }

}
