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
	String chatName;
	
	public Chat(BufferedReader reader, ClientObserver writer, String name) {
		Thread t = new Thread(new ClientHandler(reader, writer));
		t.start();
		this.reader = reader;
		chatName = name;
		System.out.println("Created new chat instance named:" + chatName);
	}
	
	public void addMember(String newMember, Socket clientSocket) throws IOException{
		members.add(newMember);
		ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
		this.addObserver(writer);
	}
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private ClientObserver writer;
		public ClientHandler(BufferedReader reader, ClientObserver writer) {
			this.reader = reader;
			this.writer = writer; 
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("chat server read "+message);
					writer.println("got your message budy");
					writer.flush();
					//setChanged();
					//notifyObservers(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
