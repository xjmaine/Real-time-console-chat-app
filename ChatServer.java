import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
  public static void main(String[] args) {
    try {
      // Create a server socket
      ServerSocket serverSocket = new ServerSocket(9999);

      // Create a list to store connected clients
      List<ClientHandler> clients = new ArrayList<>();

      while (true) {
        // Accept incoming client connections
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected: " + clientSocket);

        // Create a new client handler for the client
        ClientHandler client = new ClientHandler(clientSocket, clients);
        clients.add(client);

        // Start the client handler in a new thread
        new Thread(client).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class ClientHandler implements Runnable {
  private Socket clientSocket;
  private List<ClientHandler> clients;
  private String username;
  private BufferedReader input;
  private PrintWriter output;

  public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
    this.clientSocket = clientSocket;
    this.clients = clients;

    try {
      // Set up input and output streams for the client
      input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      output = new PrintWriter(clientSocket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      // Read the client's username
      username = input.readLine();
      broadcastMessage(username + " has joined the chat.");

      // Continuously read and broadcast messages from the client
      while (true) {
        String message = input.readLine();
        broadcastMessage(username + ": " + message);
      }
    } catch (IOException e) {
      // Client disconnected
      clients.remove(this);
      broadcastMessage(username + " has left the chat.");
    }
  }

  private void broadcastMessage(String message) {
    // Send the message to all connected clients
    for (ClientHandler client : clients) {
      client.output.println(message);
    }
  }
}
