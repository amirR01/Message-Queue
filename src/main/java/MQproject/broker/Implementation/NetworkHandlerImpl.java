package MQproject.broker.Implementation;

import MQproject.broker.Interface.NetworkHandlerInterface;
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
        listen(12345);
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
    public String readMessage(String ip, int port) {
        Tuple<String, Integer> ipPort = new Tuple<>(ip, port);
        lock.lock();
        ArrayList<String> messages = messageMap.get(ipPort);
        if (messages != null && !messages.isEmpty()) {
            String firstMessage = messages.remove(0);
            lock.unlock();
            return firstMessage;
        }
        lock.unlock();
        return null; // Handle the case when the list is empty
    }

    @Override
    public void sendMessage(String ip, int port, String message) {
        Tuple<String, Integer> ipPort = new Tuple<>(ip, port);
        Socket socket = portSocketMap.get(ipPort);
        if (socket != null) {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                // Handle IO exception
                e.printStackTrace();
            }
        }
    }
    private void listen(int port) {
        new Thread(() -> {
            try {
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

