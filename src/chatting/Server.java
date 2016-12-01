package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalLookupService;
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
	HashMap<String, UserServerSide> clientListNames = new HashMap<String, UserServerSide>();
	
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
							Chat newChat = new Chat(message);
							if(chatList.containsKey(message)){
								System.out.println("chat already exists");
							}
							else{	// create new chat and add to user
								chatList.put(newChat.toString(),newChat);
								user.chat = newChat;
								System.out.println("created a chat");
								// now add creator to chat
								message = reader.readLine();	// update message to next line
								user.chat.addMember(message, writer);								
								// add friend to chat
								message = reader.readLine();	// update message to next line
								String friendName = message;
								if(clientListNames.containsKey(friendName)){
									UserServerSide friend = clientListNames.get(friendName);
									friend.setChat(user.chat); 
									user.chat.addMember(friendName, new ClientObserver(friend.socket.getOutputStream()));		
									user.chat.welcomeMessage();
								}											
							}							
							
						}
						else{
							System.out.println("name is empty");
						}
						
					}
					else if (message.equals("sendMsg")){
						message = reader.readLine();	// update message to next line
						if (user.chat != null)
							user.chat.sendMessage(user,message);
					}
					else if (message.equals("joinChat")){
						String chatName = reader.readLine();	// update message to next line
						if (chatList.containsKey(chatName)){	// check if chat exists
							user.setChat(chatList.get(chatName));	// change user's chat
							chatList.get(chatName).addMember(user.name, new ClientObserver(user.socket.getOutputStream()));
						}
						else{
							System.out.println("chat does not exist");
						}						
					}
					
					else if (message.equals("leaveChat")){
						if (user.chat!=null){
							System.out.println("gets here");
							user.chat.removeMember(user.name, writer);
							user.chat = null;
						}
					}
					
					else if (message.equals("changePass")){
						String newPass = reader.readLine();	// update message to next line
						System.out.println("new password!!" + newPass);
						user.password = newPass;		
					}
					
					else if (message.equals("newUser")){
						String userName = reader.readLine();	// update message to next line
						if(clientListNames.containsKey(userName)){
							System.out.println("user name taken");
						}
						else{
							user.name = userName;
							clientListNames.put(userName, user);	// add user to list
							String password= reader.readLine();	// update message to next line
							user.password = password; 
						}
					}
			
					else if (message.equals("login")){
						System.out.println("login attempt server");
						String userName = reader.readLine();	// update message to next line
						if(clientListNames.containsKey(userName)){
							user = clientListNames.get(userName);	// user found
							String password= reader.readLine();	// update message to next line
							writer.println("loginResponse");
							writer.flush();
							System.out.println(user.password);
							System.out.println("input password " +password);
							if (user.password.equals(password)){ // check password
								writer.println("loginSuccess");
								writer.flush();
								System.out.println("here1");
							}
							else{
								writer.println("loginFailed");
								writer.flush();
								System.out.println("here2");
							}
						}
						else{
							System.out.println("yoooo");
							writer.println("loginResponse");
							writer.flush();
							writer.println("loginFailed");
							writer.flush();
						}
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
