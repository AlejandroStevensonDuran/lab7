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
	ArrayList<Chat> chatList = new ArrayList<Chat>();
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
		Chat myChat;
		public ClientHandler(Socket clientSocket, ClientObserver writer) {
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
							myChat = new Chat(reader, writer, message);
							chatList.add(myChat);
							System.out.println("created a chat");
						}
						else{
							System.out.println("name is empty");
						}
						// now add creator to chat
						if (!(message = reader.readLine()).equals("")){	// update message to next line
							String myName = message;
							myChat.addMember(myName, new Socket());
						}
						else{
							System.out.println("creator name is empty");
						}
						// add friend to chat
						if (!(message = reader.readLine()).equals("")){	// update message to next line
							String friendName = message;
							myChat.addMember(friendName, new Socket());
						}
						else{
							System.out.println("friends name is empty");
						}
					}
					else if (message.equals("sendMsg")){
						myChat.sendMessage();
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
