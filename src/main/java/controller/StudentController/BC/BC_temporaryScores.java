package controller.StudentController.BC;


import back.course.Course;
import back.persons.Student;
import back.persons.Teacher;
import controller.StudentController.CW.publicPages.CW;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;

public class BC_temporaryScores implements Initializable {
    public static final Logger log = LogManager.getLogger(BC_temporaryScores.class);


    Student student;
    ClientNetwork clientNetwork;

    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Label timeShowLabel;

    @FXML
    Button HomeButton;

    @FXML
    Pane pane;

    @FXML
    Label NameLabel;

    @FXML
    Label CollegeLabel;

    @FXML
    Label GradeLabel;

    @FXML
    Label ScoreLabel;

    @FXML
    Label ProtestLabel;

    @FXML
    Label AnswerLabel;

    @FXML
    Button SubmitButton;

    Timeline timeline;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;



    LinkedList<TextField> textFieldsForProtest = new LinkedList<>();
    LinkedList<String> IDForTextFields = new LinkedList<>();

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));

        student.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForStudent(student.getStudentNumber(), s);

        log.info("change background");


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);
    }

    public void setStudent(Student student, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.student = student;

        Color color = student.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        SubmitButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);



        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;
            int size = 0;

            @Override
            public void handle(ActionEvent event) {

                LinkedList<String> ids = clientNetwork.ShowableCoursesOfStudentTemporaryScores(student.getId());
                LinkedList<Course> courses  = clientNetwork.GiveCoursesOfAStudent(ids);
                if(courses.size() != size){
                    size = courses.size();
                    PrintForPane(courses);
                }

                if(clientNetwork.isConnected && timesForFirst == 0){
                    connectionStatus.setVisible(true);
                    reconnectionButton.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                    Image image2 = new Image(file.toURI().toString());
                    connectionStatus.setImage(image2);
                    timesForFirst++;
                    timesForSecond = 0;
                }
                else if(!clientNetwork.isConnected && timesForSecond == 0){
                    reconnectionButton.setVisible(true);
                    connectionStatus.setVisible(false);
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "reconnectionButton.png");
                    Image image2 = new Image(file.toURI().toString());
                    reconnectionButton.setGraphic(new ImageView(image2));
                    timesForSecond++;
                    timesForFirst = 0;

                    reconnectionButton.setOnAction(e -> {
                        clientNetwork.attemptToMakeNewConnection();
                        if(clientNetwork.isConnected){
                            reconnectionButton.setVisible(false);
                            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "connected.png");
                            Image image3 = new Image(file1.toURI().toString());
                            connectionStatus.setImage(image3);
                        }
                    });

                }

            }




        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void PrintForPane(LinkedList<Course> courses){

        IDForTextFields.clear();
        textFieldsForProtest.clear();

        pane.getChildren().clear();

        pane.getChildren().add(NameLabel);
        pane.getChildren().add(CollegeLabel);
        pane.getChildren().add(GradeLabel);
        pane.getChildren().add(ScoreLabel);
        pane.getChildren().add(AnswerLabel);
        pane.getChildren().add(ProtestLabel);


        pane.setPrefHeight(courses.size() * 200 + 30);
        int counter = 0;

        for (int i = 0; i < courses.size(); i++) {
            HelpPrintStudent(counter, courses.get(i));
            counter++;
        }



    }

    public void HelpPrintStudent(int counter, Course course){

        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 20);

        Label label1 = new Label(course.getName());
        label1.prefHeight(142);
        label1.prefWidth(57);
        label1.setLayoutX(14);
        label1.setLayoutY(85 + 200 * counter);
        label1.setFont(font);
        pane.getChildren().add(label1);

        Label label2 = new Label(student.getCollege() + "");
        label2.prefHeight(142);
        label2.prefWidth(57);
        label2.setLayoutX(217);
        label2.setLayoutY(85 + 200 * counter);
        label2.setFont(font);
        pane.getChildren().add(label2);

        Label label3 = new Label(student.getGrade() + "");
        label3.prefHeight(142);
        label3.prefWidth(57);
        label3.setLayoutX(402);
        label3.setLayoutY(85 + 200 * counter);
        label3.setFont(font);
        pane.getChildren().add(label3);


        double score = clientNetwork.GetScore(course.getId(), student.getId());

        Label label4 = new Label(score + "");
        label4.prefHeight(142);
        label4.prefWidth(57);
        label4.setLayoutX(605);
        label4.setLayoutY(85 + 200 * counter);
        label4.setFont(font);
        pane.getChildren().add(label4);


        TextField textFieldForProtest = new TextField();
        textFieldForProtest.setPrefHeight(100);
        textFieldForProtest.setPrefWidth(200);
        textFieldForProtest.setLayoutX(750);
        textFieldForProtest.setLayoutY(60 + 200 * counter);

        TextField textFieldForAnswer = new TextField();
        textFieldForAnswer.setPrefHeight(100);
        textFieldForAnswer.setPrefWidth(200);
        textFieldForAnswer.setLayoutX(1050);
        textFieldForAnswer.setLayoutY(60 + 200 * counter);
        textFieldForAnswer.setDisable(true);


        int index = clientNetwork.FindIndexCourseFormAStudentFromId(student.getId(), course.getId());
        if(index == -1){

        }
        else if(clientNetwork.getScoreForCourseStudent(student.getId(), index) == -1){

        }
        else{

            textFieldForProtest.setText(clientNetwork.getProtestForCourseStudent(student.getId(), index));
            textFieldForAnswer.setText(clientNetwork.getAnswerForCourseStudent(student.getId(), index));
        }

        pane.getChildren().add(textFieldForProtest);
        pane.getChildren().add(textFieldForAnswer);


        textFieldsForProtest.add(textFieldForProtest);
        IDForTextFields.add(course.getId());

    }

    public boolean validInputForProtestAndAnswer(String s){
        for (int i = 0; i < s.length() - 3; i++) {
            if(s.charAt(i) == ':' & s.charAt(i + 1) == ':' & s.charAt(i + 2) == ':'){
                return false;
            }
        }
        return true;
    }

    public void SubmitButtonFunction(ActionEvent e) {
        Submit();
    }

    public void Submit(){
        for (int i = 0; i < textFieldsForProtest.size(); i++) {
            int index = clientNetwork.FindIndexCourseFormAStudentFromId(student.getId(), IDForTextFields.get(i));
            if(index == -1){

            }
            else{
                boolean b = validInputForProtestAndAnswer(textFieldsForProtest.get(i).getText());
                if(b){
                    if(textFieldsForProtest.get(i).getText().equals("")){
                        textFieldsForProtest.get(i).setText(" ");
                    }
                    clientNetwork.changeProtestForAStudent(student.getId(), index, textFieldsForProtest.get(i).getText());
                    student.getProtestForCourseStudent().set(index , textFieldsForProtest.get(i).getText());
                    log.info("Submitted successfully");
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Your answer shouldn't have :::");
                    alert.showAndWait();

                    log.error("invalid format");

                    return;
                }
            }
        }
    }


    public void HomeButtonFunction(ActionEvent e) {
        timeline.stop();
        
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BCFirstPage.fxml"));
            Parent root = loader.load();
            BCFirstPage BCFirstPage = loader.getController();
            BCFirstPage.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        timeline.stop();
        
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Public\\LoginPage.fxml"));
            Parent root = loader.load();
            LoginPage LoginPage = loader.getController();
            LoginPage.setClientNetwork(clientNetwork);
            LoginPage.setImageView();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            LocalDateTime localDateTime = LocalDateTime.now();
            clientNetwork.changeLastLoginStudent(student.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());




        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowCourseListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowCourseListMenu BC_ShowCourseListMenu = loader.getController();
            BC_ShowCourseListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void TeacherListMenuFunction(ActionEvent event) {
        timeline.stop();
        


        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            BC_ShowTeacherListMenu BC_ShowTeacherListMenu = loader.getController();
            BC_ShowTeacherListMenu.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void ScheduleFunction(ActionEvent event) {
        timeline.stop();
        


        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            BC_ShowWeeklySchedule BC_ShowWeeklySchedule = loader.getController();
            BC_ShowWeeklySchedule.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ProfileFunction(ActionEvent event) {
        timeline.stop();
        


        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowProfile.fxml"));
            Parent root = loader.load();
            BC_ShowProfile BC_ShowProfile = loader.getController();
            BC_ShowProfile.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void ExamFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_ShowExams.fxml"));
            Parent root = loader.load();
            BC_ShowExams BC_ShowExams = loader.getController();
            BC_ShowExams.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void certificateStudentFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_CertificateStudent.fxml"));
            Parent root = loader.load();
            BC_CertificateStudent BC_CertificateStudent = loader.getController();
            BC_CertificateStudent.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void MinorFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_Minor.fxml"));
            Parent root = loader.load();
            BC_Minor BC_Minor = loader.getController();
            BC_Minor.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_withdrawalFromEducation.fxml"));
            Parent root = loader.load();
            BC_withdrawalFromEducation BC_withdrawalFromEducation = loader.getController();
            BC_withdrawalFromEducation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void RecommendationFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_RequestForRecommendation.fxml"));
            Parent root = loader.load();
            BC_RequestForRecommendation BC_RequestForRecommendation = loader.getController();
            BC_RequestForRecommendation.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_temporaryScores.fxml"));
            Parent root = loader.load();
            BC_temporaryScores BC_temporaryScores = loader.getController();
            BC_temporaryScores.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void StatusFunction(ActionEvent event) {
        timeline.stop();
        

        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\BC\\BC_StudentEducationalStatus.fxml"));
            Parent root = loader.load();
            BC_StudentEducationalStatus BC_StudentEducationalStatus = loader.getController();
            BC_StudentEducationalStatus.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void cwFunction(ActionEvent event) {
        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\CW\\publicPages\\CW.fxml"));
            Parent root = loader.load();
            CW cw = loader.getController();
            cw.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
