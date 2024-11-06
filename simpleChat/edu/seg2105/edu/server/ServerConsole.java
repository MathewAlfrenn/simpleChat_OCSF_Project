package edu.seg2105.server.backend;

import ocsf.server.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF {

    private EchoServer server;
    private Scanner userInput;

    public ServerConsole(EchoServer server) {
        this.server = server;
        this.userInput = new Scanner(System.in);
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }

    // Method to start listening for commands
    public void start() {
        String input;
        while (true) {
            input = userInput.nextLine();
            handleCommand(input);
        }
    }

    // Method to handle user commands
    private void handleCommand(String command) {
        if (command.equals("#quit")) {
            server.quitServer();
        } else if (command.equals("#stop")) {
            server.stopListening();
        } else if (command.equals("#close")) {
            server.stopListening();
            server.closeAllClients();
        } else if (command.startsWith("#setport")) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                int port = Integer.parseInt(parts[1]);
                server.setPort(port);
                System.out.println("Port changed to " + port);
            }
        } else if (command.equals("#start")) {
            if (!server.isListening()) {
                server.startListening();
            } else {
                System.out.println("Server is already listening.");
            }
        } else if (command.equals("#getport")) {
            System.out.println("Current port: " + server.getPort());
        } else {
            // Echo message to all clients
            server.sendToAllClients("SERVER MSG> " + command);
            System.out.println("SERVER MSG> " + command);
        }
    }
}
