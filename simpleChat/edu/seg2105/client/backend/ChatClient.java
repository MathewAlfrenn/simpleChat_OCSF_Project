// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String loginId;


  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port,String loginId, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginId = loginId;
    this.clientUI = clientUI;
    openConnection();
    sendLoginMessage();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  public void handleCommand(String message)
  {
    String[] tokens = message.split(" ", 2); // split message for command that needs 2 words
    switch (tokens[0]) //check the first string
    {
      case "#quit":
        quit();
        break;

      case "#logoff":
        try {
          closeConnection();
        } catch (IOException e) {
          clientUI.display("Error logging off: " + e.getMessage());
        }
        break;

      case "#sethost":
        if (!isConnected()) {
          setHost(tokens[1]);
          clientUI.display("Host set to: " + getHost());
        } else {
          clientUI.display("Error: You must log off before setting the host.");
        }
        break;

      case "#setport":
        if (!isConnected()) {
          try {
            setPort(Integer.parseInt(tokens[1]));
            clientUI.display("Port set to: " + getPort());
          } catch (NumberFormatException e) {
            clientUI.display("Error: Invalid port number.");
          }
        } else {
          clientUI.display("Error: You must log off before setting the port.");
        }
        break;

      case "#login":
        if (!isConnected()) {
          try {
            openConnection();
            sendLoginMessage();
            clientUI.display("Connected to server.");
          } catch (IOException e) {
            clientUI.display("Error logging in: " + e.getMessage());
          }
        } else {
          clientUI.display("Error: Already connected.");
        }
        break;

      case "#gethost":
        clientUI.display("Current host: " + getHost());
        break;

      case "#getport":
        clientUI.display("Current port: " + getPort());
        break;

      default:
        clientUI.display("Please verify that you entered a valid command.");
        break;
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  //new code

  protected void connectionClosed() {
    clientUI.display("Connection closed");
    
    
    
	}
  protected void connectionException(Exception exception) {
    // Check if the exception is not null and has a message
    if (exception != null && exception.getMessage() != null) {
        clientUI.display("Connection exception: " + exception.getMessage());
    } else {
        clientUI.display("Connection exception occurred: The server has shut down");
    }
}
private void sendLoginMessage() 
  {
    try 
    {
      // Send the login message to the server
      sendToServer("#login " + loginId);
    } 
    catch (IOException e) 
    {
      clientUI.display("Error sending login message to server: " + e.getMessage());
    }
  }
  

}
//End of ChatClient class
