package MQproject.client.Implementation;

import MQproject.client.Interface.NetworkHandlerInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class NetworkHandlerImpl implements NetworkHandlerInterface {
    private final HashMap<Integer, Socket> portSocketMap;

    public NetworkHandlerImpl() {
        this.portSocketMap = new HashMap<>();
    }
    @Override
    public int connect(String serverAddress, int port) {
        try {
            Socket socket = new Socket(serverAddress, port);
            int localPort = socket.getLocalPort();
            portSocketMap.put(localPort, socket);
            return localPort;
        } catch (UnknownHostException e) {
            // Handle unknown host exception
            e.printStackTrace();
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
        return -1; // Return -1 if connection fails
    }

    @Override
    public void disconnect(int localPort) {
        try {
            Socket socket = portSocketMap.get(localPort);
            if (socket != null) {
                socket.close();
                portSocketMap.remove(localPort);
            }
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
    }

    @Override
    public String readMessage(int localPort) {
        try {
            Socket socket = portSocketMap.get(localPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
        return null; // Return null if reading fails
    }

    @Override
    public void sendMessage(int localPort, String message) {
        try {
            Socket socket = portSocketMap.get(localPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NetworkHandlerInterface a = new NetworkHandlerImpl();
        String ip = "127.0.0.1";
        int peerPort = 12345;
        int localPort = a.connect(ip, peerPort);
        a.sendMessage(localPort, "hello buddy");
        String response = a.readMessage(localPort);
        System.out.println(response);
    }
}

