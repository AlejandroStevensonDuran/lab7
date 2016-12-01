package assignment7;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage; 

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Client2 extends Application {
	BufferedReader reader;
	PrintWriter writer;
	TextField incoming = new TextField();
	TextField outgoing = new TextField();
	TextField newChatName = new TextField();
	TextField joinChatName = new TextField();
	TextField friendName = new TextField();
	TextField statusBar = new TextField();
	ArrayList<Chat> chatList = new ArrayList<Chat>();
	String name="user1";
	boolean connected=false;
	int Width = 200;
	
	
	public static void main(String[] args) {
        launch(args);
		try {
			new Client2().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		if (connected == false){	// set connection - one time 
			setUpNetworking();
			connected = true;
		}
		//set preferences for textboxes
		outgoing.setPromptText("Enter chat stuff");
		incoming.setPromptText("chat history");
		incoming.setPrefSize(Width, 200);
		newChatName.setPromptText("Enter new chat name");
		joinChatName.setPromptText("Enter chat name to join ");
		friendName.setPromptText("Enter friend's name ");
		statusBar.setPrefSize(Width,20);
		
		FlowPane paneForTextField = new FlowPane();
		paneForTextField.setOrientation(Orientation.VERTICAL);
		
//		ListView<String> listView = new ListView<String>();
//		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        listView.setOnMouseClicked(new EventHandler<Event>() {
//            @Override
//            public void handle(Event event) {
//                ObservableList<String> selectedItems =  listView.getSelectionModel().getSelectedItems();
//            }
//        });
		
		Button newChatBtn = new Button("Create New chat");
		newChatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if (!newChatName.getText().equals("") && !friendName.getText().equals("")){
            	writer.println("createChat");
            	writer.flush();
            	writer.println(newChatName.getText());
            	writer.flush();
            	writer.println(name);
            	writer.flush();
            	writer.println(friendName.getText());
            	writer.flush();
            	outgoing.setText("");
				outgoing.requestFocus();
            	}
            	else {
            		outgoing.setText("please fill the required fields");
            	}
            }
        });
		
		HBox newChatBox= new HBox();
		newChatBox.getChildren().addAll(newChatName,friendName,newChatBtn);
		
		//Send Message Button
		Button sendMsgBtn = new Button("Send Message");
		sendMsgBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	writer.println("sendMsg");
            	writer.flush();
            	writer.println(outgoing.getText());
            	writer.flush();
            	writer.println("endMsg");
            	outgoing.setText("");
				outgoing.requestFocus();
            }
        });
		HBox sendMsgBox = new HBox();
		sendMsgBox.getChildren().addAll(outgoing,sendMsgBtn);
		
		//Join Chat Button and text
		Button joinChatBtn = new Button("Join Chat");
		joinChatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	writer.println("joinChat");
            	writer.flush();
            	writer.println(joinChatName.getText());
            	writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
            }
        });
		HBox joinChatBox= new HBox();
		joinChatBox.getChildren().addAll(joinChatName,joinChatBtn);

		
//		paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
//		paneForTextField.setStyle("-fx-border-color: green"); 
//		outgoing.setOnAction(e -> {
//				writer.println(outgoing.getText());
//				writer.flush();
//				outgoing.setText("");
//				outgoing.requestFocus(); 
//			}); 
		
		paneForTextField.getChildren().add(incoming);
		paneForTextField.getChildren().add(sendMsgBox);
		paneForTextField.getChildren().add(newChatBox);
		paneForTextField.getChildren().add(joinChatBox);
		paneForTextField.getChildren().add(statusBar);

        primaryStage.setTitle("Hola!");
	    Scene scene = new Scene(paneForTextField, 450, 450);
	    primaryStage.setScene(scene);
	    primaryStage.show();    
	}
	
	public void run()throws Exception{
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
//					incoming.setText("");
					incoming.appendText(message + "\n");	
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}