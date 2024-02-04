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
    private final HashMap<Tuple<String, Integer>, Socket> portSocketMap;

    public NetworkHandlerImpl() {
        this.portSocketMap = new HashMap<>();
    }


    public int connect(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            Tuple<String, Integer> ipPort = new Tuple<>(ip, port);
            portSocketMap.put(ipPort, socket);
            return 0;
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
    public void disconnect(String ip, int port) {
        try {
            Tuple<String, Integer> ipPort = new Tuple<>(ip, port);
            Socket socket = portSocketMap.get(ipPort); // Double check equality logic
            if (socket != null) {
                socket.close();
                portSocketMap.remove(ipPort);
            }
        } catch (Exception e) {
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
}

