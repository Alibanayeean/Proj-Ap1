package controller.StudentController.PHD;


import back.persons.Student;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class PHD_CertificateStudent implements Initializable {

    public static final Logger log = LogManager.getLogger(PHD_CertificateStudent.class);

    Student student;
    ClientNetwork clientNetwork;

    @FXML
    MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    @FXML
    Button Submit;

    @FXML
    Label timeShowLabel;

    @FXML
    Label LabelText;

    @FXML
    ImageView sharif;

    @FXML
    ImageView Background;

    Timeline timelineForConnected;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    Stage stage;
    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        Submit.setBackground(new Background(new BackgroundFill(color, null, null)));

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
        Submit.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);

        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "Sharif_University_logo.png");
        Image image2 = new Image(file.toURI().toString());
        sharif.setImage(image2);

        File file2 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "White.png");
        Image image3 = new Image(file2.toURI().toString());
        Background.setImage(image3);

        LocalDateTime localDateTime = LocalDateTime.now();

        LabelText.setText("It is certified that Mr. / Mrs. " + student.getLastname() + ", with a student number " + student.getStudentNumber() + ",\n \nis an undergraduate student at " +
                student.getCollege() + " department of sharif university of technology \n \nCertificate validity date: " + (localDateTime.getYear() + 2) + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getDayOfMonth());

        LabelText.setAlignment(Pos.CENTER);


        Font font = Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 25);
        LabelText.setFont(font);

        timelineForConnected = PublicMethods.reconnectionTimeline(reconnectionButton, clientNetwork, connectionStatus);

    }

    @FXML
    public void submitFunction(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submit");
        alert.setHeaderText("Are you Sure");

        if(alert.showAndWait().get() == ButtonType.OK){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Submit");
            alert1.setHeaderText("Submitted successfully");
            alert1.showAndWait();

            log.info("Submit for certificate");
        }

    }


    public void HomeButtonFunction(ActionEvent e) {
        timelineForConnected.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHDFirstPage.fxml"));
            Parent root = loader.load();
            PHDFirstPage PHDFirstPage = loader.getController();
            PHDFirstPage.setStudent(student, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LogOutFunction(ActionEvent event) {
        timelineForConnected.stop();
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
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowCourseListMenu.fxml"));
                Parent root = loader.load();
                PHD_ShowCourseListMenu PHD_ShowCourseListMenu = loader.getController();
                PHD_ShowCourseListMenu.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void TeacherListMenuFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowTeacherListMenu.fxml"));
                Parent root = loader.load();
                PHD_ShowTeacherListMenu PHD_ShowTeacherListMenu = loader.getController();
                PHD_ShowTeacherListMenu.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void ScheduleFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowWeeklySchedule.fxml"));
                Parent root = loader.load();
                PHD_ShowWeeklySchedule PHD_ShowWeeklySchedule = loader.getController();
                PHD_ShowWeeklySchedule.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void ProfileFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowProfile.fxml"));
                Parent root = loader.load();
                PHD_ShowProfile PHD_ShowProfile = loader.getController();
                PHD_ShowProfile.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void ExamFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ShowExams.fxml"));
                Parent root = loader.load();
                PHD_ShowExams PHD_ShowExams = loader.getController();
                PHD_ShowExams.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void certificateStudentFunction(ActionEvent event) {
        timelineForConnected.stop();

        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_CertificateStudent.fxml"));
                Parent root = loader.load();
                PHD_CertificateStudent PHD_CertificateStudent = loader.getController();
                PHD_CertificateStudent.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void WithdrawalFromEducationFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_withdrawalFromEducation.fxml"));
                Parent root = loader.load();
                PHD_withdrawalFromEducation PHD_withdrawalFromEducation = loader.getController();
                PHD_withdrawalFromEducation.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void ThesisDefenceFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_ThesisDefense.fxml"));
                Parent root = loader.load();
                PHD_ThesisDefense PHD_ThesisDefense = loader.getController();
                PHD_ThesisDefense.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void temporaryScoresFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_temporaryScores.fxml"));
                Parent root = loader.load();
                PHD_temporaryScores PHD_temporaryScores = loader.getController();
                PHD_temporaryScores.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    public void StatusFunction(ActionEvent event) {
        timelineForConnected.stop();
        try{
                stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Student\\PHD\\PHD_StudentEducationalStatus.fxml"));
                Parent root = loader.load();
                PHD_StudentEducationalStatus PHD_StudentEducationalStatus = loader.getController();
                PHD_StudentEducationalStatus.setStudent(student, clientNetwork);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

}

