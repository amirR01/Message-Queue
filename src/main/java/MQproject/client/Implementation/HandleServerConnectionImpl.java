package MQproject.client.Implementation;

import MQproject.client.Interface.HandleServerConnection;

public class HandleServerConnectionImpl implements HandleServerConnection {

    private Object connectedServer;

    public Object getConnectedServer() {
        return connectedServer;
    }

    public void setConnectedServer(Object connectedServer) {
        this.connectedServer = connectedServer;
    }

    @Override
    public void connectToServer(Object server) {
        // Implementation for connecting to a server

    }

    @Override
    public void disconnectFromServer() {
        // Implementation for disconnecting from the server
    }

    @Override
    public Object getConnectedServerInfo() {
        // Implementation for getting information about the connected server
        return connectedServer;
    }
}