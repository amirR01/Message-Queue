package MQproject.broker.Interface;

import MQproject.broker.model.ClientInformation;

import java.util.List;

public interface ClientHandler {

    public void handleNewClient();

    public void handleClientExit();

    public void sendMessage(String message, int clientId);

    public void sendMessageToAll(String message);

    public List<Integer> getClientsId();

    public ClientInformation getClientInformation(int clientId);

    public void onSendMessageComplete();
}
