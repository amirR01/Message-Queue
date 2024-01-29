package MQproject.client.Implementation;

import MQproject.client.Interface.NetworkHandlerInterface;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class NetworkHandlerImpl implements NetworkHandlerInterface{
    @Override
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

    @Override
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

}
