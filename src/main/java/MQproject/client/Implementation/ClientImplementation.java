package MQproject.client.Implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import MQproject.client.Interface.Client;

public class ClientImplementation implements Client{

    // NetworkTest readd
    public void readNetwork(int serverPortNumber, String ipAddress) throws UnknownHostException {
        
        // add a socket connection with server
        InetAddress host = InetAddress.getLocalHost();
        try {
            Socket clientSocket = new Socket(host.getHostName(), serverPortNumber);
            InputStreamReader socketReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(socketReader);
            String recvLine = bufferedReader.readLine();
            System.out.println(recvLine);

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Networktest write
    public void writeNetwork(String message, int serverPortNumber, String ipAddress) {
        
        // add a socket connection with server
        try {
            InetAddress host = InetAddress.getLocalHost();

            Socket clientSocket = new Socket(host.getHostName(), serverPortNumber);
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            printWriter.println(message);

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientImplementation c = new ClientImplementation();
        c.push(0, "0001", "First Message!");
    }

    @Override
    public void runClient() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'runClient'");
    }

    @Override
    public void stopClient() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopClient'");
    }

    @Override
    public void subscribe(Object server) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subscribe'");
    }

    @Override
    public void unsubscribe(Object server) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
    }

    @Override
    public void pull(Object server) {

        try {
            readNetwork(serverPortNumber, ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: add approperiate exception
        }
    }

    @Override
    public void push(Object server, String key, String value) {

        try {
            System.out.println("here");
            writeNetwork(value, serverPortNumber , ipAddress); 
            System.out.println("after here");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: add approperiate exception
        }
    }    
}


