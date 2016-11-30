package chatting;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets; 
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
	ArrayList<Chat> chatList = new ArrayList<Chat>();
	boolean hasCreated = false;

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
		setUpNetworking();
		FlowPane paneForTextField = new FlowPane();
		ListView<String> listView = new ListView<String>();
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<String> selectedItems =  listView.getSelectionModel().getSelectedItems();
            }
        });
		
		Button newGroupBtn = new Button("Create New chat");
		newGroupBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//if (hasCreated==false){
            		writer.println("createChat");
            		writer.flush();
            		hasCreated = true;
            	//}
            	writer.println(outgoing.getText());
            	writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
            }
        });
		
//		paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
//		paneForTextField.setStyle("-fx-border-color: green"); 
//		outgoing.setOnAction(e -> {
//				writer.println(outgoing.getText());
//				writer.flush();
//				outgoing.setText("");
//				outgoing.requestFocus(); 
//			}); 
		
		paneForTextField.getChildren().add(incoming);
		paneForTextField.getChildren().add(outgoing);
		paneForTextField.getChildren().add(newGroupBtn);
		paneForTextField.getChildren().add(listView);

        primaryStage.setTitle("Hola!");
	    Scene scene = new Scene(paneForTextField, 300, 300);
	    primaryStage.setScene(scene);
	    primaryStage.show();    
	}
	
	public void run()throws Exception{

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
					incoming.setText("");
					incoming.appendText(message + "\n");	
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}