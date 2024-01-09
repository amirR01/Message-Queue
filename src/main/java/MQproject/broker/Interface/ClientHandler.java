package MQproject.broker.Interface;

import MQproject.broker.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ClientHandler {


    public void handleNewClient();

    public void handleClientExit();

    public void sendMessage(String message, int clientId);

    public Client getClientById(String clientId);

    public List<Client> getClients();

    public List<Integer> getClientIds();

    public void onSendMessageComplete();
}
