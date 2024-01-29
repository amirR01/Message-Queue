// // Client.java

// package MQproject.client.Implementation;


// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.net.Socket;

// public class Client {
//     public static void main(String[] args) {
//         final String SERVER_ADDRESS = "localhost";
//         final int SERVER_PORT = 12345;

//         try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
//             System.out.println("Connected to server: " + SERVER_ADDRESS + ":" + SERVER_PORT);

//             // Send a message to the server
//             String message = "Hello from Client!";
//             try (OutputStream outputStream = socket.getOutputStream()) {
//                 outputStream.write(message.getBytes());
//                 System.out.println("Sent to server: " + message);
//             }

//             // Receive the response from the server
//             try (InputStream inputStream = socket.getInputStream()) {
//                 byte[] buffer = new byte[1024];
//                 int bytesRead = inputStream.read(buffer);
//                 String responseMessage = new String(buffer, 0, bytesRead);
//                 System.out.println("Received from server: " + responseMessage);
//             }

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
