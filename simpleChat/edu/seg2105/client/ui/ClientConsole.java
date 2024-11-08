package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginId) 
  {
    try 
    {
      client= new ChatClient(host, port,loginId, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if (message.startsWith("#")) 
        {
          client.handleCommand(message);
        } 
        else{
        client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
{
    String host ="";
    int port = DEFAULT_PORT;
    String loginId = null;
    // Ensure that loginId is provided as the first argument
    try
    {
        loginId = args[0];  // First argument is the loginId
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("No login ID specified");
    }

    // Try to get host from command-line argument (second argument is host)
    try
    {
        host = args[1];  // Second argument is the host
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
        host = "localhost";  
    }

    // Try to get port from command-line argument (third argument is port)
    try
    {
        port = Integer.parseInt(args[2]);  // Third argument is the port
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
        System.out.println("Using default port " + DEFAULT_PORT);  // Default port
    }
    catch (NumberFormatException e)
    {
        System.out.println("Invalid port number. Using default port " + DEFAULT_PORT);
    }

    // Create ClientConsole instance with host, port, and loginId
    if(loginId==null){
      System.out.println("ERROR - No login ID specified. Connection aborted.");
    }
    else{
    	System.out.println("Id : "+ loginId);
    	System.out.println("port: "+ port);
    	System.out.println("host : "+ host);
    ClientConsole chat = new ClientConsole(host, port, loginId);
    chat.accept();  // Wait for console data
  }
}

}
//End of ConsoleChat class
