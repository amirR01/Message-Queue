package MQproject.broker.Implementation;

import MQproject.broker.Interface.ClientHandler;
import MQproject.broker.Interface.DataManager;
import MQproject.broker.model.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class ClientHandlerImpl implements ClientHandler {
    @Autowired
    private DataManager dataManager;
    private final HashMap<Integer, Client> clients = new HashMap<>();

    public void handleNewClient() {
    }

    ;

    public void handleClientExit() {
    }

    ;

    public void sendMessage(String message, int clientId) {
        Client client = clients.get(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found");
        }
    }

    public Client getClientById(String clientId) {
        return null;
    }

    ;

    public List<Client> getClients() {
        return null;
    }

    ;

    public List<Integer> getClientIds() {
        return null;
    }

    ;

    public void onSendMessageComplete() {
    }

    ;

}
