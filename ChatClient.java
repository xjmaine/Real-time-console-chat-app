import java.io.*;
import java.net.*;

public class ChatClient {
  public static void main(String[] args) throws Exception {
    // Create a client socket
    Socket socket = new Socket("localhost", 6789);
    System.out.println("Connected to chat server");

    // Create input and output streams for the socket
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    // Read messages from the user and send them to the server
    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String message = userInput.readLine();
      out.println(message);
      System.out.println("Sent: " + message);

      // Read and display the server's response
      String response = in.readLine();
      System.out.println("Received: " + response);
    }
  }
}
