package PBL4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int port = 5504;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final Map<String, ClientHandler> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running...");
            while (true) {
                Socket clietSocket = serverSocket.accept();
                System.out.println("client connected: " + clietSocket);
                ClientHandler clientHandler = new ClientHandler(clietSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String clientIP;

        private DataInputStream in;
        private DataOutputStream out;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.clientIP = clientSocket.getInetAddress().getHostAddress();
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                clientIP = in.readUTF();
                System.out.println(clientIP);
                clientMap.put(clientIP, this);
                String message;
//                while (true) {
//                    message = in.readUTF();
//                    if(!message.isEmpty()){
//                        Message
//                    }
//
//                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
