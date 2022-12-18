import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
  public static void main(String[] args) {
    try {
      // Connect to the server
      Socket socket = new Socket("localhost", 9999);
      System.out.println("Connected to the server: " + socket);

      // Set up input and output streams for the client
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System
