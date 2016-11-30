package chatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import javafx.application.Application;

public class Server{
	//List<String> clientList = new ArrayList<Socket>();	

	List<Socket> clientList = new ArrayList<Socket>();	
	HashMap<String, Chat> chatList = new HashMap<String, Chat>();
	ArrayList<String> clientListNames = new ArrayList<String>();
	HashMap clientListMap = new HashMap();
	
	public static void main(String[] args) {
		try {
			new Server().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket, writer));
			t.start();
			//this.addObserver(writer);
			this.clientList.add(clientSocket);
			System.out.println("got a connection");
		}
	}
	
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private ClientObserver writer;
		UserServerSide user;		
		public ClientHandler(Socket clientSocket, ClientObserver writer) {
			user = new UserServerSide("user1", clientSocket);
			Socket sock = clientSocket;
			this.writer = writer; 
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while (reader!=null && (message = reader.readLine()) != null) {
					if (message.equals("createChat")){
						if (!(message = reader.readLine()).equals("")){	// update message to next line
							Chat newChat = new Chat(reader, writer, message);
							if(chatList.containsKey(message)){
								System.out.println("chat already exists");
							}
							else{	// create new chat and add to user
								chatList.put(newChat.toString(),newChat);
								user.chat = newChat;
								System.out.println("created a chat");
								// now add creator to chat
								message = reader.readLine();	// update message to next line
								user.chat.addMember(message, new Socket());								
								// add friend to chat
								message = reader.readLine();	// update message to next line
								String friendName = message;
								//TODO find if friends exists, if he does, then retrieve his socket 
								user.chat.addMember(friendName, new Socket());		
								user.chat.welcomeMessage();
							}							
							
						}
						else{
							System.out.println("name is empty");
						}
						
					}
					else if (message.equals("sendMsg")){
						if (user.chat != null)
							user.chat.sendMessage(user);
					}
					else if (message.equals("joinChat")){
						message = reader.readLine();	// update message to next line
						//TODO 
						
					}
					

//					System.out.println("server read "+message);
//					writer.println("got your message budy");
//					writer.flush();
					//setChanged();
					//notifyObservers(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
