package MQproject.client.Implementation;

import MQproject.client.Interface.NetworkHandlerInterface;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class NetworkHandlerImpl implements NetworkHandlerInterface {
    private static final Lock lock = new ReentrantLock();

    private final HashMap<Tuple<String, Integer>, Socket> portSocketMap = new HashMap<>();

    private final HashMap<Tuple<String, Integer>, ArrayList<String>> messageMap = new HashMap<>();

    @PostConstruct
    public void init() {
        listen();
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
    private void listen() {
        new Thread(() -> {
            try {
                // choose a random port number to listen on
                int port = 12345;
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    // create a new thread to handle the connection
                    new Thread(() -> {
                        try {
                            String ip = socket.getInetAddress().getHostAddress(); // check changing the socket variable in father thread don't affect the child thread
                            Integer p = socket.getPort();
                            Tuple<String, Integer> ipPort = new Tuple<>(ip, p);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String message = reader.readLine();
                            // handle the message
                            lock.lock();
                            ArrayList<String> messages = messageMap.get(ipPort);
                            if (messages == null) {
                                messages = new ArrayList<>();
                                messages.add(message);
                                messageMap.put(ipPort, messages);
                            }
                            else {
                                messages.add(message);
                            }
                            lock.unlock();
                        } catch (IOException e) {
                            // Handle IO exception
                            e.printStackTrace();
                        }
                    }).start();

                }
            } catch (UnknownHostException e) {

            } catch (IOException e) {
                // Handle IO exception
                e.printStackTrace();
            }
        }).start();
    }


}

