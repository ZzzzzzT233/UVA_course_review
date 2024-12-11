package edu.virginia.cs;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseReviewApplication extends Application {
    private Stage primaryStage;
    private CourseReviewController loginController;
    private MainMenuController mainMenuController;
    @Override
    public void start(Stage stage) throws IOException {

        this.primaryStage = stage;

        // Load the Login FXML file and create its controller
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/CourseReviewPage.fxml"));
        Parent loginRoot = loginLoader.load();
        loginController = loginLoader.getController();

        // Load the Main Menu FXML file and create its controller
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Parent mainMenuRoot = mainMenuLoader.load();
        mainMenuController = mainMenuLoader.getController();
//        mainMenuController.setLoginController(loginController);

        // Set the Login scene as the primary stage scene
        Scene loginScene = new Scene(loginRoot);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}

