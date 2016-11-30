package chatting;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
 

public class Login extends Application {
 
 String user = "Client1";
 String pw = "password";
 String checkUser, checkPw;
 
    public static void main(String[] args) {
        launch(args);
    }
     
    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login page");
        
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,50,50,50));
        
        HBox hb = new HBox();
        hb.setPadding(new Insets(20,20,20,30));
        

        
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(40,40,40,40));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        

        
        Label lblUserName = new Label("Username");
        final TextField txtUserName = new TextField();
        Label lblPassword = new Label("Password");
        final PasswordField pf = new PasswordField();
        Button btnLogin = new Button("Login");
        final Label lblMessage = new Label();
        

        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnLogin, 2, 1);
        gridPane.add(lblMessage, 1, 2, 2, 1);
        
        
        Text text = new Text("Hola!");
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
        
        
        //Adding text to HBox
        hb.getChildren().add(text);
                          
        //Add ID's to Nodes
        bp.setId("bp");
        gridPane.setId("root");
        btnLogin.setId("btnLogin");
        text.setId("text");
                
        //Action for btnLogin
        btnLogin.setOnAction(new EventHandler() {
        	
         public void handle(ActionEvent event) {
          checkUser = txtUserName.getText().toString();
          checkPw = pf.getText().toString();
          if(checkUser.equals(user) && checkPw.equals(pw)){
           lblMessage.setText("Correct credentials");
           lblMessage.setTextFill(Color.GREEN);
          }
          else{
           lblMessage.setText("Sorry :( Incorrect user or password.");
           lblMessage.setTextFill(Color.RED);
          }
          txtUserName.setText("");
          pf.setText("");
         }

		@Override
		public void handle(Event event) {
			ActionEvent Aevent = (ActionEvent)event;
			handle(Aevent);
		}

         });
       
        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);  
        
        //Adding BorderPane to the scene and loading CSS
     Scene scene = new Scene(bp);
     //scene.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
     primaryStage.setScene(scene);
     primaryStage.show();
    }
}
