package chatting;

import java.io.*; 
import java.net.*;

import day23network.observer.ChatClient;
//import day23network.observer.ChatClient.IncomingReader;
import javafx.application.Application; 
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Label; 
import javafx.scene.control.ScrollPane; 
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage; 


public class Client extends Application {
	BufferedReader reader;
	PrintWriter writer;
	TextField incoming;
	
	
	

	public static void main(String[] args) {
        launch(args);
		try {
			new Client().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FlowPane paneForTextField = new FlowPane(); 
		paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
		paneForTextField.setStyle("-fx-border-color: green"); 
		//paneForTextField.a
		incoming = new TextField();
		incoming.setText("yop");
		paneForTextField.getChildren().add(incoming);
        primaryStage.setTitle("Hola!");
	    Scene scene = new Scene(paneForTextField, 300, 300);
	    primaryStage.setScene(scene);
	    primaryStage.show();    
	}
	
	public void run()throws Exception{
	// start will already be called automatically
       // launch(args);
	setUpNetworking();
	}

	private void setUpNetworking() throws Exception{
		
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
	}
	
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {				
						incoming.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}