package edu.virginia.cs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.stat.Statistics;

import java.io.IOException;

public class CourseReviewController {
    protected static BusinessLayer processor;
    @FXML
    private AnchorPane Login, Main_menu;
    @FXML
    private Button RegisterButton,LoginButton, SubmitRegister, SubmitLogin;
    @FXML
    private GridPane RegisterGrid, LoginGrid;
    @FXML
    private Label userName, pw, NewuserName, Newpw, ConfirmNewpw;
    @FXML
    private TextField userTextField, NewuserTextField;
    @FXML
    private PasswordField pwBox, NewpwBox, ConfirmNewpwBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Button MainMenu;
    protected static Student username;

//    public CourseReviewController() {
//        System.out.println("processor~~~~~~");
//            processor = new BusinessLayer();
//    }
    public void initialize(){
        processor = new BusinessLayer();
        initializeGrid_button();
        handleLoginButton();
        handleRegisterButton();
//        handletry();
    }
    public void initializeGrid_button(){
        Font font = Font.font("Arial", FontWeight.NORMAL, 16);
//        System.out.println("initializeGrid_button~~~~~~");
        userName = new Label("User Name:");
        userName.setFont(font);
        LoginGrid.add(userName, 0, 0);
        userTextField = new TextField();
        userTextField.setFont(font);
        LoginGrid.add(userTextField, 1, 0);
        pw = new Label("Password:");
        pw.setFont(font);
        LoginGrid.add(pw, 0, 1);
        pwBox = new PasswordField();
        LoginGrid.add(pwBox, 1, 1);
        LoginGrid.setVisible(false);

        NewuserName = new Label("User Name:");
        NewuserName.setFont(font);
        RegisterGrid.add(NewuserName, 0, 0);
        NewuserTextField = new TextField();
        NewuserTextField.setFont(font);
        RegisterGrid.add(NewuserTextField, 1, 0);
        Newpw = new Label("Password:");
        Newpw.setFont(font);
        RegisterGrid.add(Newpw, 0, 1);
        NewpwBox = new PasswordField();
        RegisterGrid.add(NewpwBox, 1, 1);
        ConfirmNewpw = new Label("Confirm Password:");
        ConfirmNewpw.setFont(font);
        RegisterGrid.add(ConfirmNewpw, 0, 2);
        ConfirmNewpwBox = new PasswordField();
        RegisterGrid.add(ConfirmNewpwBox, 1, 2);
        RegisterGrid.setVisible(false);

        LoginGrid.setHgap(2);
        LoginGrid.setVgap(5);
        RegisterGrid.setHgap(2);
        RegisterGrid.setVgap(5);

        SubmitLogin.setVisible(false);
        SubmitRegister.setVisible(false);
    }
    public void handleRegisterButton(){
        RegisterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegisterButton.setVisible(false);
                LoginButton.setVisible(false);
                RegisterGrid.setVisible(true);
                SubmitRegister.setVisible(true);
                handleSubmitRegisterButton();
            }
        });
    }
    public void backtoLogin(){
        RegisterButton.setVisible(true);
        LoginButton.setVisible(true);
        LoginGrid.setVisible(false);
        RegisterGrid.setVisible(false);
        SubmitRegister.setVisible(false);
        SubmitLogin.setVisible(false);
        userTextField.setText("");
        NewuserTextField.setText("");
        pwBox.setText("");
        NewpwBox.setText("");
        ConfirmNewpwBox.setText("");
    }
    public void handleSubmitRegisterButton(){
        errorLabel.setText("");
        SubmitRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String inputname = NewuserTextField.getText();
                inputname = inputname.trim();
                String inputpw = NewpwBox.getText();
                String inputConfirmpw = ConfirmNewpwBox.getText();
                if (inputname.equals("")){
                    errorLabel.setText("Username can't be empty");
                    backtoLogin();
                }
                else if(inputpw.equals("")){
                    errorLabel.setText("Password can't be empty");
                    backtoLogin();
                }
                else if(!inputpw.equals(inputConfirmpw)) {
                    errorLabel.setText("password not match, try again");
                    backtoLogin();
                }
                else{
                    try {
                        username = processor.createUser(inputname, inputpw);
                        open_main_menu(event);
                        errorLabel.setText("registered");
                    }
                    catch (IllegalStateException existed){
                        errorLabel.setText("The username is already existed.");
                        backtoLogin();
                    }
                }
            }
        });

    }

    public void handleLoginButton(){
        LoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegisterButton.setVisible(false);
                LoginButton.setVisible(false);
                LoginGrid.setVisible(true);
                SubmitLogin.setVisible(true);
                handleSubmitLoginButton();
            }
        });
    }
    public void handleSubmitLoginButton(){
        errorLabel.setText("");
        SubmitLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String inputname = userTextField.getText();
                inputname = inputname.trim();
                String inputpw = pwBox.getText();
                try {
                    username = processor.loginExistingUser(inputname,inputpw);
//                    System.out.println("username: ");
//                    System.out.println(username);
                    open_main_menu(event);
                }catch (IllegalStateException wrongname){
                    errorLabel.setText("Error! Incorrect username or password. Please try again");
                    backtoLogin();
                }catch (IllegalArgumentException wrongpw){
                    errorLabel.setText("Error! Incorrect username or password. Please try again");
                    backtoLogin();
                }catch (Exception e){
                    errorLabel.setText("Error! Incorrect username or password. Please try again");
                    backtoLogin();
                }
            }
        });
    }
    public void open_main_menu(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(CourseReviewApplication.class.getResource("/MainMenu.fxml"));
//            MainMenuController controller = new MainMenuController();
//            loader.setController(controller);

            // show the main menu page
            Stage mainMenuStage = new Stage();
            mainMenuStage.setScene(new Scene(loader.load()));
            mainMenuStage.show();
            // close the login page
            ((Node) (event.getSource())).getScene().getWindow().hide();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void handletry(){
//        MainMenu.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                    open_main_menu(event);
//            }
//        });
//    }


}

