import java.io.*;
import java.net.*;
import java.util.*;

public class ChatProgram {
    public static void main(String[] args) throws Exception {
        // Create a new thread for the server
        new Thread(new ChatServer()).start();

        // Create a new thread for the client
        new Thread(new ChatClient()).start();
    }
}

class ChatServer implements Runnable {
    // List of connected clients
    List<Socket> clients = new ArrayList<>();

    @Override
    public void run() {
        try {
            // Create a ServerSocket and bind it to a port
            ServerSocket serverSocket = new ServerSocket(8888);

            // Accept incoming connections and create a new thread for each client
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                new Thread(new ChatClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class for handling each client
    class ChatClientHandler implements Runnable {
        Socket clientSocket;

        ChatClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Get the InputStream and OutputStream for the client socket
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                // Read and write messages to and from the client
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true);

                String line;
                while ((line = reader.readLine()) != null) {
                    // Broadcast the message to all connected clients
                    for (Socket client : clients) {
                        writer = new PrintWriter(client.getOutputStream(), true);
                        writer.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
