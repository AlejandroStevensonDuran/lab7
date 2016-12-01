// Slip day used
package chatting;

import java.net.Socket;
import java.util.ArrayList;

public class UserServerSide {
	String name;
	Socket socket;
	Chat chat;
	String password;
	ArrayList<UserServerSide> friendList;
	ClientObserver writer;
	
	public UserServerSide(String name, Socket socket, ClientObserver writer){
		this.name=name;
		this.socket=socket;
		this.writer = writer;
		// password
		// friendList
	}
	
	public void setChat(Chat chat){
		this.chat=chat;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
