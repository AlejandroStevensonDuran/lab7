package chatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import chatting.Server.ClientHandler;
import javafx.scene.control.TextField;

public class Chat extends Observable{
	ArrayList<String> members = new ArrayList<String>();
	ArrayList<String> history = new ArrayList<String>();	//chat history
	BufferedReader reader;
	ClientObserver writer;
	String chatName;
	
	@Override
	public String toString(){
		return chatName;
	}
	
	public Chat(BufferedReader reader, ClientObserver writer, String name) {
		//Thread t = new Thread(new ClientHandler(reader, writer));
		//t.start();
		this.reader = reader;
		this.writer = writer;
		chatName = name;
		System.out.println("Created new chat instance named:" + chatName);
	}
	
	public synchronized void addMember(String newMember, Socket clientSocket) throws IOException{
		members.add(newMember);
		this.addObserver(writer);		
		System.out.println("added: " + newMember);
	}
	
	public synchronized void removeMember(String newMember, Socket clientSocket) throws IOException{
		members.add(newMember);
		this.addObserver(writer);		
		System.out.println("added: " + newMember);
	}
	
	public synchronized void welcomeMessage(){
		setChanged();
		notifyObservers("Welcome to chat "+ chatName + "!");
	}
	
	public synchronized void sendMessage(UserServerSide user){
		String message;
		try {
			while ((message = reader.readLine()) != null && !message.equals("endMsg")) {
				System.out.println("recieved Message: "+message + "from" + user.toString());
				setChanged();
				notifyObservers(user.toString() + ": " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendUserNames(){	//sends user names of people in chat to all participants. invoked when user added/removed
		
	}
	
//	class ClientHandler implements Runnable {
//
//		public void run() {
//			String message;
//			try {
//				while ((message = reader.readLine()) != null) {
//					System.out.println("chat server read "+message);
//					writer.println("got your message budy");
//					writer.flush();
//					//setChanged();
//					//notifyObservers(message);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
}
