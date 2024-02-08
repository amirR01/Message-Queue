package MQproject.client.model.message;
import java.util.ArrayList;

public class ClientServerMessageAboutProducers {
    public ArrayList<ClientServerSmallerMessageAboutProducers> messages = new ArrayList<>();

    public static class ClientServerSmallerMessageAboutProducers extends BaseMessage {
        public Integer ProducerId;
        public String ProducerIp;
        public Integer ProducerPort;

        public ClientServerSmallerMessageAboutProducers(Integer ProducerId, String ProducerIp, int ProducerPort
                , MessageType messageType) {
            this.ProducerId = ProducerId;
            this.ProducerIp = ProducerIp;
            this.ProducerPort = ProducerPort;
            this.messageType = messageType;
        }
    }
}
