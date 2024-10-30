package PBL4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int PORT_NUMBER = 2507;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final Map<String, ClientHandler> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Server is running and waiting for connections..." + InetAddress.getLocalHost());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle the client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error while setting up the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendToClient(String sender, String receiver, String message) {
        ClientHandler recipient = clientMap.get(receiver);
        if (recipient != null) {
            recipient.sendMessage(sender, message);
        } else {
            System.out.println("Có đâu mà gửi");
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String clientIP;

        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clientIP = socket.getInetAddress().getHostAddress();
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                System.out.println("vao run");
                clientIP = in.readUTF(); // Read the client's name
                System.out.println(clientIP);
                clientMap.put(clientIP, this);


                String message;
                while (true) {
                    message = in.readUTF();
                    System.out.println("tin nhan message: " + message);
                    if (!message.isEmpty()) {
                        // Process the message and send it to the specific recipient
                        MessageHandler msg = new MessageHandler(message);
                        String response = msg.remoteResponse();
                        String receiver = msg.receiver();

                        System.out.println("respon: " + response + " receiver: " + receiver);
                        sendToClient(clientIP, receiver, response);

                        System.out.println("send to client: ");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error while processing client input: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeSocket();
            }
        }

        public void sendMessage(String sender, String message) {
            try {
                System.out.println(sender + " " + message);
                out.writeUTF(sender + "," + message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void closeSocket() {
            try {
                clientSocket.close();
                clients.remove(this);
                clientMap.remove(clientIP);
            } catch (IOException e) {
                System.err.println("Error while closing the socket: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

}
