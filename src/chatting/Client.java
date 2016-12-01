package assignment7;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Client extends Application {
	BufferedReader reader;
	PrintWriter writer;
	TextArea incoming = new TextArea();
	TextField outgoing = new TextField();
	TextField outgoing2 = new TextField();
	TextField newChatName = new TextField();
	TextField joinChatName = new TextField();
	TextField friendName = new TextField();
	TextField statusBar = new TextField();
	ArrayList<Chat> chatList = new ArrayList<Chat>();
	String name="user1";
	boolean connected=false;
	int Width = 200;
	static String checkUser;
	static String checkPw;
	String response="X";
	Object lock = new Object();

	public static void main(String[] args) {
		launch(args);
		try {
			new Client().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		

		primaryStage.setTitle("Login page");

		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10,50,50,50));

		HBox hb = new HBox();
		hb.setPadding(new Insets(20,20,20,30));
		
		FlowPane extraPane = new FlowPane();  // used for extra scene

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(40,40,40,40));
		gridPane.setHgap(5);
		gridPane.setVgap(5);

		Label lblUserName = new Label("Username");
		final TextField txtUserName = new TextField();
		Label lblPassword = new Label("Password");
		final PasswordField pf = new PasswordField();
		Button btnLogin = new Button("Login");
		Button newUserBtn = new Button("New User?");
		final Label lblMessage = new Label();
		btnLogin.setDisable(true);   // disable buttons until connected to server
		newUserBtn.setDisable(true);
		final TextField serverName = new TextField("localhost");
		serverName.setPromptText("Enter server name");
		Button connectSrvBtn = new Button("Connect");
		connectSrvBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String input = serverName.getText(); 
				try {
					setUpNetworking(input);
					btnLogin.setDisable(false);
					newUserBtn.setDisable(false);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("invalid server");
				}
			}
		});
		

		

		HBox connectBox= new HBox();
		connectBox.getChildren().addAll(serverName, connectSrvBtn);


		gridPane.add(lblUserName, 0, 0);
		gridPane.add(txtUserName, 1, 0);
		gridPane.add(lblPassword, 0, 1);
		gridPane.add(pf, 1, 1);
		gridPane.add(btnLogin, 2, 1);
		gridPane.add(newUserBtn, 2, 0);
		gridPane.add(lblMessage, 1, 2, 2, 1);
		gridPane.add(connectBox, 1, 9, 5, 1);


		Text text = new Text("Hola!");
		text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));


		hb.getChildren().add(text);

		bp.setId("bp");
		gridPane.setId("root");
		btnLogin.setId("btnLogin");
		text.setId("text");
		bp.setTop(hb);
		bp.setCenter(gridPane);  
		Scene Loginscene = new Scene(bp, 600, 600);
		primaryStage.setScene(Loginscene);


		Scene extraScene = new Scene(extraPane, 600, 600);


		//set preferences for textboxes
		outgoing.setPromptText("Enter chat stuff");
		incoming.setPromptText("chat history");
		incoming.setPrefSize(Width, 200);
		incoming.setEditable(false);
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
					statusBar.setText("please fill the required fields");
				}
			}
		});

		HBox newChatBox= new HBox();
		newChatBox.getChildren().addAll(newChatName,friendName,newChatBtn);
		
		// leave chat
		Button leaveChatBtn = new Button("Leave Chat");
		leaveChatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				writer.println("leaveChat");
				writer.flush();
				statusBar.setText("You have left the chat");
				incoming.setText("");	// clear chat history
			}
		});

		
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
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
				statusBar.setText("Message sent successfully");
			}
		});
		HBox sendMsgBox = new HBox();
		sendMsgBox.getChildren().addAll(outgoing,sendMsgBtn);

		//extra button will include password change and friend list
		Button extra = new Button("Extra");
		extra.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(extraScene);
			}
		});		
		
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
				incoming.setText("");
				// check if success
				statusBar.setText("Joined chat successfully");
			}
		});
		HBox joinChatBox= new HBox();
		joinChatBox.getChildren().addAll(joinChatName,joinChatBtn);
		
		//Clear History Button
				Button clearHisBtn = new Button("Clear History");
				clearHisBtn .setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						incoming.setText("");
						statusBar.setText("Chat history cleared");
					}
				});

		
		HBox lowerBox = new HBox();
		lowerBox.getChildren().addAll(clearHisBtn,leaveChatBtn);
		
		paneForTextField.getChildren().add(incoming);
		paneForTextField.getChildren().add(sendMsgBox);
		paneForTextField.getChildren().add(newChatBox);
		paneForTextField.getChildren().add(joinChatBox);
		paneForTextField.getChildren().add(statusBar);
		paneForTextField.getChildren().add(lowerBox);
		paneForTextField.getChildren().add(extra);
		Scene menuScene = new Scene(paneForTextField, 600, 600);

		btnLogin.setOnAction(new EventHandler() {
			public void handle(ActionEvent event) throws IOException {
				String userName = txtUserName.getText();
				String userPass = pf.getText().toString();
				writer.println("login");	// command login
				writer.flush();
				writer.println(userName);	// send user name
				writer.flush();
				writer.println(userPass);	// send password
				writer.flush();
				//while(response.equals("X")){};


				while (true) {
					if(!response.equals("X")){
						System.out.println("seconddd");
						break;
					}
				}

				System.out.println("yayy");
				if(response.equals("loginSuccess")){					
					primaryStage.setScene(menuScene);
					primaryStage.setTitle("Hola!");
				}
				else{
					lblMessage.setText("Sorry :( Incorrect user or password.");
					lblMessage.setTextFill(Color.RED);
					primaryStage.setScene(Loginscene);
				}
				txtUserName.setText("");
				pf.setText("");	
			}

			@Override
			public void handle(Event event) {
				ActionEvent Aevent = (ActionEvent)event;
				try {
					handle(Aevent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		newUserBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(menuScene);
				String userName = txtUserName.getText();
				String userPass = pf.getText().toString();
				writer.println("newUser");
				writer.flush();
				writer.println(userName);
				writer.flush();
				writer.println(userPass);
				writer.flush();
			}
		});
		
		Button changePassBtn = new Button("Change Password");
		changePassBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				writer.println("changePass");
				writer.flush();
				writer.println(outgoing2.toString()); // send new password
				writer.flush();
			}
		});
		HBox changePassBox = new HBox();
		changePassBox.getChildren().addAll(outgoing2,changePassBtn);
		
		Button back = new Button("Back");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(menuScene);
			}
		});
		
		extraPane.getChildren().add(changePassBox);
		extraPane.getChildren().add(back);
		
		
		
		primaryStage.show();    
	}

	public void run()throws Exception{
	}

	private void setUpNetworking(String serverName) throws Exception{
		//@SuppressWarnings("resource")
		InetAddress address = InetAddress.getByName(serverName);
		Socket sock = new Socket(address.getHostAddress(), 4242);
		//Socket sock = new Socket("127.0.0.1", 4242);
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
						if (message.equals("loginResponse")){
							response = reader.readLine();
						}
						else{
							incoming.appendText(message + "\n");
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			
		}
	}

}
