package edu.virginia.cs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private BusinessLayer processor;
    @FXML
    private Button SubmitReview, SeeReview, logoutButton, enter1, enter2, enter3,enter11,mainmenuOption;
    @FXML
    private GridPane typeCourseGrid, typeMessageGrid, typeRateGrid, selectCourseGrid,printReview;
    @FXML
    private Label title,askCourse, askMessage, askRate, selectCourse, giveReviews, giveRate1, giveRate2;
    @FXML
    private TextField inputCourse, inputMessage, inputRate, provideCourse;
    @FXML
    private AnchorPane MainMenu, submitreview1, submitreview2, SeeReview1, seeReview2;
    @FXML
    private Label errorLabel;
    @FXML
    private TextArea printAllReviews;
    private Student user_name;
    private String coursename, comment;
    private int rate;
    private Course cur_course;

//    public MainMenuController() {
//        System.out.println("processor 222222~~~~~~");
//        processor = new BusinessLayer();
//    }

    public void initialize(){
//        System.out.println("initialize2 222222~~~~~~");
        processor = CourseReviewController.processor;
        MainMenu.setVisible(true);
        title.setVisible(true);
        initializeGrid_submitReview();
        initializeGrid_seeReview();
        backtoMainMenu();
        handleSubmitReviewButton();
        handleSeeReviewButton();
        handleLogOutButton();
    }

    public void initializeGrid_submitReview() {
        Font font = Font.font("Arial", FontWeight.NORMAL, 16);
        askCourse = new Label("Course Name:");
        askCourse.setFont(font);
        typeCourseGrid.add(askCourse, 0, 0);
        inputCourse = new TextField();
        inputCourse.setFont(font);
        typeCourseGrid.add(inputCourse, 1, 0);

        askMessage = new Label("Review the course: ");
        askMessage.setFont(font);
        typeMessageGrid.add(askMessage, 0, 0);
        inputMessage = new TextField();
        inputMessage.setFont(font);
        typeMessageGrid.add(inputMessage, 1, 0);

        askRate = new Label("Rate the course: ");
        askRate.setFont(font);
        typeRateGrid.add(askRate, 0, 0);
        inputRate = new TextField();
        inputRate.setFont(font);
        typeRateGrid.add(inputRate, 1, 0);
    }

    public void initializeGrid_seeReview() {
        Font font = Font.font("Arial", FontWeight.NORMAL, 16);
        selectCourse = new Label("Course Name:");
        selectCourse.setFont(font);
        selectCourseGrid.add(selectCourse, 0, 0);
        provideCourse = new TextField();
        provideCourse.setFont(font);
        selectCourseGrid.add(provideCourse, 1, 0);

        giveReviews = new Label("Reviews:  ");
        giveReviews.setFont(font);
        giveRate1 = new Label("Course Average: ");
        giveRate1.setFont(font);
        printReview.add(giveReviews, 0, 0);
        printReview.add(giveRate1, 0, 1);

        giveRate2 = new Label();
        giveRate2.setFont(font);
        printAllReviews = new TextArea();
        printAllReviews.setFont(font);
        printAllReviews.setEditable(false);
        printReview.add(printAllReviews, 1, 0);
        printReview.add(giveRate2, 1, 1);
    }

    public void backtoMainMenu(){
        title.setVisible(true);
        errorLabel.setText("");
        SubmitReview.setVisible(true);
        SeeReview.setVisible(true);
        logoutButton.setVisible(true);
        submitreview1.setVisible(false);
        SeeReview1.setVisible(false);
        inputCourse.setText("");
        inputMessage.setText("");
        inputRate.setText("");
        provideCourse.setText("");
    }
    public void handleSubmitReviewButton(){
        errorLabel.setText("");
        SubmitReview.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                title.setVisible(false);
                SubmitReview.setVisible(false);
                SeeReview.setVisible(false);
                submitreview1.setVisible(true);
                typeCourseGrid.setVisible(true);
                enter1.setVisible(true);
                submitreview2.setVisible(false);
                handleLogOutButton();
                handleEnter1Button();
            }
        });
    }
    @FXML
    public void handleEnter1Button(){
        handleLogOutButton();
        errorLabel.setText("");
        enter1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                coursename = inputCourse.getText();
                CourseReviewController crcontroller = new CourseReviewController();
//                System.out.println("coursename:" + coursename);
                user_name = crcontroller.username;
//                System.out.println("user_name: "+ user_name);
                try {
                    if (processor.checkcoursenamebyinputname(coursename)) {
                        cur_course = processor.returnCourseByName(coursename);
//                        if (cur_course==null) cur_course = processor.addCourse(coursename);
                        if(cur_course==null || (cur_course!=null &&processor.checkrepeatedcomment(user_name,cur_course))){
                            SubmitReview.setVisible(false);
                            SeeReview.setVisible(false);
                            submitreview1.setVisible(true);
                            submitreview2.setVisible(true);
                            typeCourseGrid.setVisible(false);
                            enter1.setVisible(false);
                            typeRateGrid.setVisible(false);
                            enter2.setVisible(true);
                            typeMessageGrid.setVisible(true);
                            enter3.setVisible(false);
                            SeeReview1.setVisible(false);
                            handleEnter2Button();
                        }
                        else{
                            backtoMainMenu();
                            errorLabel.setText("You can only review a course once!");
                        }

                    } else {
                        backtoMainMenu();
                        errorLabel.setText("Invalid Course Name, please type correctly!");
                    }
                }catch(IllegalStateException invalidCourseName){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly!");
                }catch(NumberFormatException notInt){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly!");
                }catch (Exception e){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly!");
                }

            }
        });
    }
    public void handleEnter2Button(){
//        errorLabel.setText("");
        handleLogOutButton();
        enter2.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                comment = inputMessage.getText();
                if(comment.trim().length()==0){
                    inputMessage.setText("");
                    errorLabel.setText("Please enter a review");
                }
                else {
                    SubmitReview.setVisible(false);
                    SeeReview.setVisible(false);
                    submitreview2.setVisible(true);
                    SeeReview1.setVisible(false);
                    typeMessageGrid.setVisible(false);
                    enter2.setVisible(false);
                    typeRateGrid.setVisible(true);
                    enter3.setVisible(true);
                    errorLabel.setText("Please enter a number 1-5");
                    handleEnter3Button();
                }
            }
        });
    }
    public void handleEnter3Button(){
        handleLogOutButton();
        enter3.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String r = inputRate.getText();
                    rate = Integer.parseInt(r.trim());
                    if(rate<1 || rate>5){
                        errorLabel.setText("Please enter a number 1-5");
                        inputRate.setText("");
                    }
                    else{
                        if (cur_course==null) cur_course = processor.addCourse(coursename);
                        processor.submitReview(user_name,coursename,comment,rate);
                        backtoMainMenu();
                    }
                }catch (NumberFormatException notInt){
                    errorLabel.setText("Please enter a number 1-5");
                    inputRate.setText("");
                }catch (Exception e){
                    errorLabel.setText("Please enter a number 1-5");
                    inputRate.setText("");
                }
            }
        });
    }

    public void handleSeeReviewButton(){
        errorLabel.setText("");
        SeeReview.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                title.setVisible(false);
                SubmitReview.setVisible(false);
                SeeReview.setVisible(false);
                submitreview1.setVisible(false);
                SeeReview1.setVisible(true);
                selectCourseGrid.setVisible(true);
                enter11.setVisible(true);
                seeReview2.setVisible(false);
                handleEnter11Button();
                handleLogOutButton();
                handleMainMenuOptionButton();
            }
        });
    }
    public void handleEnter11Button(){
        handleLogOutButton();
        errorLabel.setText("");
        enter11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String course_name = provideCourse.getText();
                try {
                    if (processor.checkcoursenamebyinputname(course_name)) {
                        if(processor.containCoursebyName(course_name)){
                            SubmitReview.setVisible(false);
                            SeeReview.setVisible(false);
                            submitreview1.setVisible(false);
                            SeeReview1.setVisible(true);
                            seeReview2.setVisible(true);
                            selectCourseGrid.setVisible(false);
                            enter11.setVisible(false);
                            getInfo(course_name);
                        }
                        else{
                            backtoMainMenu();
                            errorLabel.setText("Input course doesn't have any reviews");
                        }
                    } else {
                        backtoMainMenu();
                        errorLabel.setText("Invalid Course Name, please type correctly");
                    }
                }catch(IllegalStateException invalidCourseName){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly");
                }catch (NumberFormatException notInt){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly");
                }catch (Exception e){
                    backtoMainMenu();
                    errorLabel.setText("Invalid Course Name, please type correctly");
                }
            }
        });
    }
    public void handleMainMenuOptionButton(){
        mainmenuOption.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               backtoMainMenu();
            }
        });
    }
    public void getInfo(String course_name){
        processor.seeReviews(course_name);
        StringBuilder sb = new StringBuilder();
        for (String review : processor.printreview) {
            sb.append(review).append("\n");
        }
        printAllReviews.setText(sb.toString());
        giveRate2.setText(processor.rating + "/5");
    }
    public void handleLogOutButton(){
        errorLabel.setText("");
        logoutButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                title.setVisible(false);
//                 Load the Login FXML file
                try {
                    FXMLLoader loginLoader = new FXMLLoader(CourseReviewApplication.class.getResource("/CourseReviewPage.fxml"));
                    Stage loginStage = new Stage();
                    loginStage.setScene(new Scene(loginLoader.load()));
                    loginStage.show();
                    // close the main menu
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
