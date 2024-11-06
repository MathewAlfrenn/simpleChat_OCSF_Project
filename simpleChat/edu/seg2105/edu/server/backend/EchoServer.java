package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));

    // Check if the message is a string
    if (msg instanceof String) {
        String message = (String) msg;

        
        if (message.startsWith("#login")) {
            // Ensure the client is not already logged in
            if (client.getInfo("loginId") == null) {
                String loginId = message.substring(6).trim();  // get the ID from when the client send it to the serv

              

                
                client.setInfo("loginId", loginId);
                System.out.println(loginId + " logged in.");

                return;
                
            } else {
                sendErrorMessage(client, "You are already logged in.");
                return;
            }
        }

       
        
        //send to other clients
        String loginId = (String) client.getInfo("loginId");
        String publicMessage = loginId + ": " + message;
        this.sendToAllClients(publicMessage); // Send the message to all clients

    }
}
    
  

  private void sendErrorMessage(ConnectionToClient client, String errorMessage) {
    try {
        client.sendToClient("Error: " + errorMessage);
        client.close();
    } catch (IOException e) {
        System.err.println("Error closing client connection: " + e.getMessage());
    }
}
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
{
    int port = 0; // Port to listen on

    try
    {
        port = Integer.parseInt(args[0]); // Get port from command line
    }
    catch (Throwable t)
    {
        port = DEFAULT_PORT; // Set port to 5555 if no argument is provided
    }

   
    EchoServer sv = new EchoServer(port);

   
    try 
    {
        sv.listen(); // Start listening for connections
        System.out.println("Server started and listening on port " + port);
    } 
    catch (Exception ex) 
    {
        System.out.println("ERROR - Could not listen for clients!");
        return;
    }

    // Create an instance of ServerConsole 
    ServerConsole console = new ServerConsole(sv);

   
    try {
        console.start();  
    } catch (IOException e) {
        System.err.println("Error while processing the console input: " + e.getMessage());
    }
}


  //new code
  @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected: " + client);
    }

    @Override
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client.getInfo("loginId"));
    }

    public void quitServer() {
    try {
        stopListening(); // Stop listening for new clients
        closeAllClients(); // Disconnect all clients
        System.out.println("Server has been shut down.");
    } catch (IOException e) {
        System.err.println("Error while stopping the server: " + e.getMessage());
    }
}
public void closeAllClients() throws IOException {
  Thread[] clientThreads = getClientConnections();


  for (Thread thread : clientThreads) {
      ConnectionToClient client = (ConnectionToClient) thread;
      client.close();  // Close the connection for each client
  }

  System.out.println("All clients have been disconnected.");
}


public void startListening() {
  try {
      listen(); // Start the server listening on the specified port
      System.out.println("Server is now listening for client connections.");
  } catch (IOException e) {
      System.err.println("Error while starting to listen: " + e.getMessage());
  }
}



}
//End of EchoServer class
