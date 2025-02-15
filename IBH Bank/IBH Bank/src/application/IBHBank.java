package application;

import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class IBHBank extends Application {
    
	//Strings
    public static String txtUserName = "";
    public static String txtPassword = "";
    public static String amount = "";
    protected String firstName;
    protected String lastName;
    public static String filePath = "accounts.txt";
    
    //Boolean
    public static boolean ready = false;
  	public static boolean withdrawFromCh = false;
  	public static boolean withdrawFromSa = false;
  	public static boolean depositeToCh = false;
  	public static boolean depositeToSa = false;
  	public static boolean cToS = false;
  	public static boolean sToC = false;

    //Integers
    protected int checking;
    protected int saving;	
    public static int count = 2;
    public static int maxWi = 200;
	public static int maxDep = 1000;
	public static int maxTr = 500;
	
	//other classes
	public static checkingAccount acc = new checkingAccount();
	public static savingAccount acs = new savingAccount();
	public static readFile x = new readFile();
    
    //to enter only numbers
    UnaryOperator<Change> filter = change -> {
        String text = change.getText();

        if (text.matches("[0-9]*")) {
            return change;
        }

        return null;
    };
    
    @Override
    public void start(Stage primaryStage) {
    	
    	ready = false;
    	
    	//making first scene to log in
        BorderPane pane = new BorderPane();

        //text to enter user name 
        TextField textField = new TextField();
        textField.setPrefSize(200, 30);
        textField.setText(txtUserName);;
        
        //password text 
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(200, 30);
        
        //button to log in
        Button btnLogIn = new Button("Log In");
        
        VBox vBox = new VBox(20);
        
        vBox.getChildren().addAll(new Label("Username: "), textField, new Label("Password: "), passwordField, btnLogIn);
        vBox.setMaxWidth(200);
        vBox.setAlignment(Pos.CENTER);
        pane.setCenter(vBox);
        
        //creating and setting scene
        Scene scene = new Scene(pane, 350, 350);
        primaryStage.setTitle("IBH Bank");
        primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
    	primaryStage.show();
        
        //making second scene to tran/ dep/ with money
        BorderPane mPane = new BorderPane();
        
        //text fields
        TextField txtAmount = new TextField();
        txtAmount.setPrefWidth(30	);
        
        //labels to show info
        Label lblUserName = new Label();
        lblUserName.setFont(Font.font(20));
        
        Label lblChecking = new Label();
        lblChecking.setFont(Font.font(20));
        
        Label lblSaving = new Label();
        lblSaving.setFont(Font.font(20));
        
        //button to finish
        Button btnEnter =  new Button("Enter");
        
        //comboBox to chose tran/ dep/ with 
        ComboBox<String> mTypeBox = new ComboBox<String>();
        
        //adding items for first combo box
        mTypeBox.getItems().add("Withdraw");
        mTypeBox.getItems().add("Deposite");
        mTypeBox.getItems().add("Transfer");
        
        ComboBox<String> mBox = new ComboBox<String>();
        
        TextFormatter<String> number = new TextFormatter<>(filter);

        String s = "					    	";
        
        //first VBox for info
        VBox infoBox = new VBox(20);
        infoBox.getChildren().addAll(lblUserName, lblChecking, lblSaving, new Label(s));
        infoBox.setMaxWidth(200);
        infoBox.setAlignment(Pos.TOP_LEFT);
        mPane.setTop(infoBox);
        
        //second VBox for tran/ dep/ with money
        VBox sBox = new VBox(20);
        sBox.getChildren().addAll(new Label("Chose what you want."), mTypeBox, mBox);
        sBox.setAlignment(Pos.TOP_RIGHT);
        
        //HBox to add the infoBox and the sBox in one line
        HBox hBox = new HBox();
        hBox.getChildren().addAll(infoBox, new Label(s) , sBox);
        mPane.setTop(hBox);
        
        //third VBox to enter the amount
        VBox tBox = new VBox(20);
        tBox.getChildren().addAll(new Label("Enter your amount: "), txtAmount, btnEnter, new Label(s));
        tBox.setMaxWidth(200);
        txtAmount.setTextFormatter(number);
        tBox.setAlignment(Pos.CENTER);
        mPane.setCenter(tBox);
        
        //second scene
        Scene mScene = new Scene(mPane, 650, 350);
        
        //check if user entered username and password right
        btnLogIn.setOnAction(e -> {
        	txtUserName = textField.getText().toLowerCase();
            txtPassword = passwordField.getText();
				
            if(txtUserName.matches("iahmad") && txtPassword.matches("Ibrahim123")){
            	accounFinder();
            	ready = true;
            }
            else if(txtUserName.matches("lstar") && txtPassword.matches("Lisa123")){
            	accounFinder();
            	ready = true;
            }
            else if(txtUserName.matches("mluffy") && txtPassword.matches("Luffy123")){
            	accounFinder();
            	ready = true;
            }
            else if(txtUserName.matches("lmessi") && txtPassword.matches("Messi123")){
            	accounFinder();
            	ready = true;
            }
            else if(txtUserName.matches("ainiesta") && txtPassword.matches("In123@#")){
            	accounFinder();
            	ready = true;
            }
            else{
            	//checking if the user entered wrong username or password less than 3 time
                if(count >= 1){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Waning");
                    alert.setHeaderText("Warning");
                    alert.setContentText("You entered a wrong username or password");
                    alert.showAndWait().ifPresent(rs -> {
                        count--;
                    });
                }
                else{
                	//if user entered username or password wrong 3 times the program close
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("ERROR");
                    alert.setContentText("You entered a wrong username or password");
                    alert.showAndWait().ifPresent(rs -> {
                        Platform.exit();
                    });
                }
            }
            if(ready) {
            	primaryStage.setScene(mScene);
            	primaryStage.setTitle("IBH Bank");
            	primaryStage.setResizable(false);
                primaryStage.show();
                lblUserName.setText("Name: " + firstName + " " + lastName);
                lblChecking.setText("Checking: $" + checking);
                lblSaving.setText("Saving: $" + saving);
            }
        });
        
      //check what he chose
        mTypeBox.setOnAction(e ->  {
        	if(mTypeBox.getValue() == "Withdraw"){
        		amount = "";
        		txtAmount.setText(amount);
        		mBox.getItems().clear();
        		mBox.getItems().add("From Checking");
        		mBox.getItems().add("From Saving");
        	}
        	else if(mTypeBox.getValue() == "Deposite"){
        		amount = "";
        		txtAmount.setText(amount);
        		mBox.getItems().clear();
        		mBox.getItems().add("To Checking");
        		mBox.getItems().add("To Saving");
        	}
        	else if(mTypeBox.getValue() == "Transfer"){
        		amount = "";
        		txtAmount.setText(amount);
        		mBox.getItems().clear();
        		mBox.getItems().add("From Saving to Checking");
        		mBox.getItems().add("From Checking to Saving");
        	}
        	else{
        		mBox.getItems().clear();
        	}	
        });
        
        //check what the user chose
        mBox.setOnAction(e -> {
        	
        	if(mBox.getValue() == "From Checking"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = true;
				withdrawFromSa = false;
				depositeToCh = false;
				depositeToSa = false;
				cToS = false;
				sToC = false;
			}
			else if(mBox.getValue() == "From Saving"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = false;
				withdrawFromSa = true;
				depositeToCh = false;
				depositeToSa = false;
				cToS = false;
				sToC = false;
			}
			else if(mBox.getValue() == "To Checking"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = false;
				withdrawFromSa = false;
				depositeToCh = true;
				depositeToSa = false;
				cToS = false;
				sToC = false;
			}
			else if(mBox.getValue() == "To Saving"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = false;
				withdrawFromSa = false;
				depositeToCh = false;
				depositeToSa = true;
				cToS = false;
				sToC = false;
			}
			else if(mBox.getValue() == "From Saving to Checking"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = false;
				withdrawFromSa = false;
				depositeToCh = false;
				depositeToSa = false;
				cToS = false;
				sToC = true;
			}
			else if(mBox.getValue() == "From Checking to Saving"){
				amount = "";
				txtAmount.setText(amount);
				withdrawFromCh = false;
				withdrawFromSa = false;
				depositeToCh = false;
				depositeToSa = false;
				cToS = true;
				sToC = false;
			}
			else{
				withdrawFromCh = false;
				withdrawFromSa = false;
				depositeToCh = false;
				depositeToSa = false;
				cToS = false;
				sToC = false;
			}
        });
        
        //event for the enter button
        btnEnter.setOnAction(e -> {
        	amount = txtAmount.getText();
        	if(amount == ""){
        		amount ="0";
				txtAmount.setText(amount);
			}
			//withdraw from checking
			else if(withdrawFromCh){
				//check if there any error
				if(checking < Integer.parseInt(amount) || (maxWi < Integer.parseInt(amount) || maxWi == 0)){
					if(checking < Integer.parseInt(amount)){
						reError("withdraw more then what you hav");
					}
					else if((maxWi < Integer.parseInt(amount) || maxWi == 0)){
						reError("withdraw more than $200 in one day");
					}
				}
				else{
					acc.checking = checking;
					acc.withdrawChecking(Integer.parseInt(amount));
					if(maxWi >= Integer.parseInt(amount)){
						maxWi = maxWi - Integer.parseInt(amount);
						checking = acc.checking;
						lblChecking.setText("Checking: $" + checking);
					}
					else if(maxDep < Integer.parseInt(amount)){
						reError("withdraw more than $200 in one day");
					}
				}
			}
			//withdraw from saving
			else if(withdrawFromSa){
				//check if there any error
				if(saving < Integer.parseInt(amount) || (maxWi < Integer.parseInt(amount) || maxWi == 0)){
					if(saving < Integer.parseInt(amount)){
						reError("withdraw more then what you hav");
					}
					else if((maxWi < Integer.parseInt(amount) || maxWi == 0)){
						reError("withdraw more than $200 in one day");
					}
				}
				else{
					acs.saving = saving;
					acs.withdrawSaving(Integer.parseInt(amount));
					if(maxWi >= Integer.parseInt(amount)){
						maxWi = maxWi - Integer.parseInt(amount);
						saving = acs.saving;
		                lblSaving.setText("Saving: $" + saving);
					}
					else if(maxDep < Integer.parseInt(amount)){
						reError("withdraw more than $200 in one day");
					}
				}
			}
			//deposit from checking
			else if(depositeToCh){
				//check if there any error
				if(Integer.parseInt(amount) > maxDep || maxDep == 0){
					reError("add more than $1000 in one day");
				}
				else{
					acc.checking = checking;
					acc.depositChecking(Integer.parseInt(amount));
					if(maxDep >= Integer.parseInt(amount)){
						maxDep = maxDep - Integer.parseInt(amount);
						checking = acc.checking;
						lblChecking.setText("Checking: $" + checking);
					}
					else if(maxDep < Integer.parseInt(amount)){
						reError("add more than $1000 in one day");
					}
				}
			}
			//deposit from saving
			else if(depositeToSa){
				//check if there any error
				if(Integer.parseInt(amount) > maxDep || maxDep == 0){
					reError("add more than $1000 in one day");
				}
				else{
					acs.saving = saving;
					acs.depositSaving(Integer.parseInt(amount));	
					if(maxDep >= Integer.parseInt(amount)){
						maxDep = maxDep - Integer.parseInt(amount);
						saving = acs.saving;
		                lblSaving.setText("Saving: $" + saving);
					}
					else if(maxDep < Integer.parseInt(amount)){
						reError("add more than $1000 in one day");
					}
				}
			}
			//transfer from checking to saving
			else if(cToS){
				//check if there any error
				if(checking < Integer.parseInt(amount) || (maxTr < Integer.parseInt(amount) || maxTr == 0)){
					if(checking < Integer.parseInt(amount)){
						reError("transfer more than what you have");
					}
					else if((maxWi < Integer.parseInt(amount) || maxWi == 0)){
						reError("transfer more than $500 in one day");
					}
				}
				else{
					acc.checking = checking;
					acc.saving = saving;
					acc.checkingToSaving(Integer.parseInt(amount));
					if(maxTr >= Integer.parseInt(amount)){
						maxTr = maxTr - Integer.parseInt(amount);
						checking = acc.checking;
						saving = acc.saving;
						lblChecking.setText("Checking: $" + checking);
		                lblSaving.setText("Saving: $" + saving);
					}
					else if(maxTr < Integer.parseInt(amount)){
						reError("transfer more than $500 in one day");
					}
				}
			}
			//transfer from saving to checking
			else if(sToC){
				//check if there any error
				if(saving < Integer.parseInt(amount) || (maxTr < Integer.parseInt(amount) || maxTr == 0)){
					if(saving < Integer.parseInt(amount)){
						reError("transfer more than what you have");
					}
					else if((maxWi < Integer.parseInt(amount) || maxWi == 0)){
						reError("transfer more than $500 in one day");
					}
				}
				else{
					acs.saving = saving;
					acs.checking = checking;
					acs.savingToChecking(Integer.parseInt(amount));
					if(maxTr >= Integer.parseInt(amount)){
						maxTr = maxTr - Integer.parseInt(amount);
						saving = acs.saving;
						checking = acs.checking;
						lblChecking.setText("Checking: $" + checking);
		                lblSaving.setText("Saving: $" + saving);
					}
					else if(maxTr < Integer.parseInt(amount)){
						reError("transfer more than $500 in one day");
					}
				}
			}
			x.addInfo(filePath, firstName, lastName, checking, saving);
			x.closeFile();
        });
    }
    
    //error method if the user is withdraw or add or transfer more than he has or the limit
    public void reError(String error) {
    	 Alert alert = new Alert(AlertType.ERROR);
         alert.setTitle("ERROR");
         alert.setHeaderText("ERROR");
         alert.setContentText("You can not " + error);
         alert.showAndWait().ifPresent(rs -> {
        	 
         });
    }
    
    //account finder method
    @SuppressWarnings("static-access")
	public void accounFinder(){  
        if(txtUserName.matches("iahmad") && txtPassword.matches("Ibrahim123")){
            x.openFile();
            x.readFile();
            firstName = x.firstName[0];
            lastName = x.lastName[0];
            checking = x.checking[0];
            saving = x.saving[0];
            x.closeFile();
        }
        else if(txtUserName.matches("lstar") && txtPassword.matches("Lisa123")){
            x.openFile();
            x.readFile();
            firstName = x.firstName[1];
            lastName = x.lastName[1];
            checking = x.checking[1];
            saving = x.saving[1];
            x.closeFile();
        }
        else if(txtUserName.matches("mluffy") && txtPassword.matches("Luffy123")){
            x.openFile();
            x.readFile();
            firstName = x.firstName[2];
            lastName = x.lastName[2];
            checking = x.checking[2];
            saving = x.saving[2];
            x.closeFile();
        }
        else if(txtUserName.matches("lmessi") && txtPassword.matches("Messi123")){
            x.openFile();
            x.readFile();
            firstName = x.firstName[3];
            lastName = x.lastName[3];
            checking = x.checking[3];
            saving = x.saving[3];
            x.closeFile();
        }
        else if(txtUserName.matches("ainiesta") && txtPassword.matches("In123@#")){
            x.openFile();
            x.readFile();
            firstName = x.firstName[4];
            lastName = x.lastName[4];
            checking = x.checking[4];
            saving = x.saving[4];
            x.closeFile();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
