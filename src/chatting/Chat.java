package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import assignment7.Server.ClientHandler;
import javafx.scene.control.TextField;

public class Chat extends Observable{
	ArrayList<String> members = new ArrayList<String>();
	ArrayList<String> history = new ArrayList<String>();	//chat history
	String chatName;
	
	@Override
	public String toString(){
		return chatName;
	}
	
	public Chat(String name) {
		chatName = name;
		System.out.println("Created new chat instance named:" + chatName);
	}
	
	public synchronized void addMember(String newMember, ClientObserver writer) throws IOException{
		members.add(newMember);
		this.addObserver(writer);		
		System.out.println("added: " + newMember);
	}
	
	public synchronized void removeMember(String member, ClientObserver writer) throws IOException{
		//TODO remove member
		System.out.println("removed: " + member);
	}
	
	public synchronized void welcomeMessage(){
		setChanged();
		notifyObservers("Welcome to chat "+ chatName + "!");
	}
	
	public synchronized void sendMessage(UserServerSide user, String message){
		System.out.println("recieved Message: "+message + "from" + user.toString());
		setChanged();
		notifyObservers(user.toString() + ": " + message);
		history.add(user.toString() + ": " + message);	// add message to history
	}
	
	public void sendUserNames(){	//sends user names of people in chat to all participants. invoked when user added/removed
		
	}
		
}
