package controller.TeacherController.EducationalAssistant;


import back.persons.Teacher;
import clientNetwork.ClientNetwork;
import config.ConfigIdentifier;
import config.ReadPropertyFile;
import controller.publicController.LoginPage;
import controller.publicMethods.PublicMethods;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class EducationalAssistant_ChatRoom implements Initializable {

    public static final Logger log = LogManager.getLogger(EducationalAssistant_ChatRoom.class);

    Teacher teacher;
    ClientNetwork clientNetwork;

    @FXML
    javafx.scene.control.MenuBar MenuBar;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button LogOutButton;

    @FXML
    Button HomeButton;

    @FXML
    Label timeShowLabel;

    Stage stage;

    Timeline timeline;

    @FXML
    Button reconnectionButton;

    @FXML
    ImageView connectionStatus;

    @FXML
    ListView<String> listView;

    @FXML
    ListView<String> listViewForSendFile;

    @FXML
    Button backButton;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Pane pane;

    @FXML
    Button addFileForPanePage;

    @FXML
    ImageView imageViewForPanePage;

    @FXML
    Label labelNameForPanePage;

    @FXML
    Pane leftPane;

    String name = "";

    @FXML
    ImageView whatsApp;

    @FXML
    ImageView telegram;

    @FXML
    ImageView sharif;

    @FXML
    ImageView twitter;


    @FXML
    Pane downPane;

    @FXML
    Pane upPane;

    @FXML
    Button sendButton;

    @FXML
    Button sendTextButton;

    @FXML
    TextArea textAreaForText;

    String usernameTo = "";

    boolean isOnPanePage = false;

    LinkedList<String> wantsToSendFileInGroups = new LinkedList<>();

    @FXML
    void ChangingColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        teacher.color = colorPicker.getValue();
        String s = "#" + Integer.toHexString(color.hashCode());
        clientNetwork.changeColorForTeacher(teacher.getUsername(), s);

        updateListCell();

        log.info("change background");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PublicMethods.initImageOfLogOutButton(LogOutButton);

        PublicMethods.initImageOfHomeButton(HomeButton);

        PublicMethods.initClock(timeShowLabel);

        File file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "send.png");
        Image image3 = new Image(file3.toURI().toString());
        ImageView imageView = new ImageView(image3);
        sendTextButton.setGraphic(imageView);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "sharifClipart.png");
        image3 = new Image(file3.toURI().toString());
        sharif.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "telegram.png");
        image3 = new Image(file3.toURI().toString());
        telegram.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "whatsapp.png");
        image3 = new Image(file3.toURI().toString());
        whatsApp.setImage(image3);

        file3 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "twitter.png");
        image3 = new Image(file3.toURI().toString());
        twitter.setImage(image3);

    }

    public void setTeacher(Teacher Teacher, ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
        this.teacher = Teacher;

        Color color = Teacher.color;
        MenuBar.setBackground(new Background(new BackgroundFill(color, null, null)));
        timeShowLabel.setBackground(new Background(new BackgroundFill(color, null, null)));
        HomeButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        backButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        sendTextButton.setBackground(new Background(new BackgroundFill(color, null, null)));
        colorPicker.setValue(color);


        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>(){
            int timesForFirst = 0;
            int timesForSecond = 0;

            @Override
            public void handle(ActionEvent event) {
                updateListCell();
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


        String s = "#" + Integer.toHexString(color.hashCode());
        leftPane.setStyle("-fx-background-color: " + s + ";");
        scrollPane.setVisible(false);


        downPane.setVisible(false);
        upPane.setVisible(false);


        sendButton.setVisible(false);
        listViewForSendFile.setVisible(false);

        updateListCell();


    }


    public void updateListCell(){

        String c = "#" + Integer.toHexString(teacher.getColor().hashCode());

        leftPane.setStyle("-fx-background-color: " + c + ";");

        listView.setStyle("" +
                "-fx-background-color: " + c + ";" +
                "-fx-text-fill: black;");

        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> times = new LinkedList<>();

        if(isOnPanePage){
            sendButton.setVisible(false);
            listViewForSendFile.setVisible(false);
            wantsToSendFileInGroups = new LinkedList<>();

            String s1 = "";
            for (int i = 0; i < name.length(); i++) {
                if(name.charAt(i) == '.'){
                    s1 = name.substring(0, i);
                    break;
                }
            }
            if(s1.equals("")){
                s1 = name;
            }

            int first = name.indexOf('(');
            int second = name.indexOf(')');
            usernameTo = name.substring(first + 1, second);


            String address = clientNetwork.getFileNameOfFromUsername(usernameTo);
            File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "\\" + address);
            if(!file.exists()){
                clientNetwork.sendFileFromServer(address , teacher.getUsername());
            }
            Image image = new Image(file.toURI().toString());
            imageViewForPanePage.setImage(image);
            imageViewForPanePage.setFitWidth(45);
            imageViewForPanePage.setFitHeight(45 / image.getWidth() * image.getHeight());

            addFileForPanePage.setBackground(new Background(new BackgroundFill(teacher.getColor(), null, null)));

            Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);

            labelNameForPanePage.setFont(font);
            labelNameForPanePage.setText(s1);

            file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + "paperHolder.png");
            image = new Image(file.toURI().toString());
            addFileForPanePage.setGraphic(new ImageView(image));

            PrintForPane();
        }
        else{
            if(sendButton.isVisible()){
                listViewForSendFile.setVisible(true);
                String[] s = new String[wantsToSendFileInGroups.size()];
                wantsToSendFileInGroups.toArray(s);
                ObservableList<String> items = FXCollections.observableArrayList(s);
                listViewForSendFile.setItems(items);
            }
            else{
                listViewForSendFile.setVisible(false);
            }

            LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllStudentForABossAndEAInChatRoom(teacher.getId(), teacher.getUsername(), teacher.getCollege() + "");
            if(linkedLists.size() == 2){
                names = linkedLists.get(0);
                times = linkedLists.get(1);
                for (int i = 0; i < names.size(); i++) {
                    names.set(i, names.get(i) + "... ... ..." + times.get(i));
                }
            }

            String[] s = new String[names.size()];
            names.toArray(s);

            ObservableList<String> items = FXCollections.observableArrayList (s);
            listView.setItems(items);

            listView.setCellFactory(param -> {
                ListCell<String> cell = new ListCell<String>(){

                    ImageView imageView;
                    @Override
                    public void updateItem(String name, boolean empty){
                        super.updateItem(name, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }


                        int first = name.indexOf('(');
                        int second = name.indexOf(')');
                        String username = name.substring(first + 1, second);
                        String address =clientNetwork.getFileNameOfFromUsername(username);
                        File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "\\" + address); //TODO
                        if(!file.exists()){
                            clientNetwork.sendFileFromServer(address , teacher.getUsername());
                        }

                        Image image = new Image(file.toURI().toString());
                        imageView = new ImageView();

                        imageView.setImage(image);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50 / image.getWidth() * image.getHeight());


                        Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 20);

                        setFont(font);
                        setText(name);
                        setGraphic(imageView);

                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                    if(!cell.isEmpty()){
                        cell.setCursor(Cursor.HAND);
                    }
                });

                cell.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    if(!cell.isEmpty()){
                        cell.setCursor(Cursor.DEFAULT);
                    }
                });

                cell.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                        name = cell.getItem();
                        scrollPane.setVisible(true);
                        downPane.setVisible(true);
                        upPane.setVisible(true);
                        isOnPanePage = true;
                        listView.setVisible(false);
                        sendButton.setVisible(false);
                        updateListCell();
                    }
                    else if (event.getClickCount() == 1 && (! cell.isEmpty()) ) {
                        name = cell.getItem();
                        sendButton.setVisible(true);
                        int first = name.indexOf('(');
                        int second = name.indexOf(')');
                        String username = name.substring(first + 1, second);
                        boolean b = true;
                        for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                            if(wantsToSendFileInGroups.get(i).equals(username)){
                                b = false;
                            }
                        }
                        if(b){
                            wantsToSendFileInGroups.add(username);
                        }
                        updateListCell();
                    }
                });

                return cell ;

            });

        }
    }

    public void backButtonFunction(ActionEvent event) {
        if(isOnPanePage){
            isOnPanePage = false;
            scrollPane.setVisible(false);
            downPane.setVisible(false);
            upPane.setVisible(false);
            listView.setVisible(true);
            sendButton.setVisible(false);
            listViewForSendFile.setVisible(false);
            wantsToSendFileInGroups = new LinkedList<>();
            backButton.setVisible(false);
            updateListCell();
        }
        else{
            wantsToSendFileInGroups = new LinkedList<>();
            sendButton.setVisible(false);
            listViewForSendFile.setVisible(false);
            backButton.setVisible(false);
            updateListCell();
        }
    }

    public void PrintForPane(){
        pane.getChildren().clear();


        LinkedList<LinkedList<String>> linkedLists = clientNetwork.getAllMessagesFromTwoPersons(teacher.getUsername(), usernameTo);
        if(linkedLists.size() == 0){
            return;
        }
        LinkedList<String> typeFile = linkedLists.get(0);
        LinkedList<String> address = linkedLists.get(1);
        LinkedList<String> whoSent = linkedLists.get(2); // you | front
        LinkedList<String> messageTime = linkedLists.get(3);



        pane.setPrefHeight(messageTime.size() * 120 + 30);


        for (int i = 0; i < messageTime.size(); i++) {
            String fileName = "";
            if(typeFile.get(i).toLowerCase().equals("PICTURE".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("PDF".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("MUSIC".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }
            else if(typeFile.get(i).toLowerCase().equals("VIDEO".toLowerCase())){
                fileName = typeFile.get(i).toLowerCase() + ".png";
            }


            Font font1 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 15);
            Font font2 = Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 10);

            if(whoSent.get(i).equals("you")){
                if(typeFile.get(i).toLowerCase().equals("text")){
                    Label label = new Label(address.get(i));
                    label.setFont(font1);
                    label.setLayoutX(513);
                    label.setLayoutY(i * 120);
                    pane.getChildren().add(label);

                    String s = messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(513 - s.length() * 4);
                    labelTime.setLayoutY(100 + i * 120 + label.getHeight());
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);
                }
                else{
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + fileName);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setLayoutX(513);
                    imageView.setLayoutY(i * 120);
                    pane.getChildren().add(imageView);

                    String s = address.get(i) + " | " + messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(513 - s.length() * 4);
                    labelTime.setLayoutY(70 + i * 120);
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);

                    final int m = i;
                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/" + address.get(m));
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + teacher.getUsername() + "/" + address.get(m));
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(address.get(m), teacher.getUsername());
                        }
                    });

                    imageView.setCursor(Cursor.HAND);
                }
            }
            else{
                if(typeFile.get(i).toLowerCase().equals("text")){
                    Label label = new Label(address.get(i));
                    label.setFont(font1);
                    label.setLayoutX(10);
                    label.setLayoutY(i * 120);
                    pane.getChildren().add(label);

                    String s = messageTime.get(i);
                    Label labelTime = new Label(s);
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(10);
                    labelTime.setLayoutY(100 + i * 120 + label.getHeight());
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);
                }
                else{
                    File file = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + fileName);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setLayoutX(0);
                    imageView.setLayoutY(i * 120);
                    pane.getChildren().add(imageView);

                    Label labelTime = new Label(address.get(i) + " | " + messageTime.get(i));
                    labelTime.prefHeight(40);
                    labelTime.prefWidth(120);
                    labelTime.setLayoutX(0);
                    labelTime.setLayoutY(70 + i * 120);
                    labelTime.setFont(font2);
                    pane.getChildren().add(labelTime);


                    final int m = i;
                    imageView.setOnMouseClicked(event -> {
                        File fileClicked = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + teacher.getUsername() + "/" + address.get(m));
                        if(fileClicked.exists()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("go to this folder: " + ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL)  + teacher.getUsername() + "/" + address.get(m));
                            alert.showAndWait();
                            PublicMethods.openADesktopFolder(fileClicked);
                        }
                        else{
                            clientNetwork.sendFileFromServer(address.get(m), teacher.getUsername());
                        }
                    });

                    imageView.setCursor(Cursor.HAND);
                }
            }
        }

        scrollPane.layout();
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    public void addFileForPanePageFunction(ActionEvent event) {

        stage = ((Stage)(((Scene) backButton.getScene()).getWindow()));
        FileChooser fil_chooser = new FileChooser();

        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg", "*.pdf", "*.mp3", "*.mp4"));
        File file = fil_chooser.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);
            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = time + teacher.getUsername().hashCode() + address;


            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            clientNetwork.sendFileToServer( teacher.getUsername(), address);

            String typeFile = "";
            index = address.lastIndexOf('.');
            String suffix = address.substring(index + 1);

            if(suffix.toLowerCase().equals("png")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("jpg")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("pdf")){
                typeFile = "PDF";
            }
            else if(suffix.toLowerCase().equals("mp3")){
                typeFile = "MUSIC";
            }
            else if(suffix.toLowerCase().equals("mp4")){
                typeFile = "VIDEO";
            }
            clientNetwork.saveMessage(teacher.getUsername(), usernameTo, address, typeFile);
            PrintForPane();
        }


    }

    public void sendTextFunction(ActionEvent event) {
        if(textAreaForText.getText() == null){
            return;
        }
        else if(textAreaForText.getText().equals("")){
            return;
        }
        else{


            String s = "";
            for (int i = 0; i < textAreaForText.getText().length(); i = i + 10) {
                if(i + 10 > textAreaForText.getText().length()){
                    s = s + textAreaForText.getText().substring(i);
                    break;
                }
                s = s + textAreaForText.getText().substring(i, i + 10) + '\n';
            }

            clientNetwork.saveMessage(teacher.getUsername(), usernameTo, s, "TEXT");

            textAreaForText.setText("");
            PrintForPane();
        }
    }

    public void sendFileAction(){
        sendButton.setVisible(false);
        listViewForSendFile.setVisible(false);

        stage = ((Stage)(((Scene) backButton.getScene()).getWindow()));
        FileChooser fil_chooser2 = new FileChooser();
        fil_chooser2.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select your file", "*.png", "*.jpg", "*.pdf", "*.mp3", "*.mp4"));
        File file = fil_chooser2.showOpenDialog(stage);

        if (file != null) {
            String ss = file.getPath();
            int index = ss.lastIndexOf('.');
            String address = ss.substring(index);

            String time = LocalDateTime.now().toString();
            time = time.replace(':', '-');
            time = time.replace('.', '-');
            address = time + teacher.getUsername().hashCode() + address;

            File file1 = new File(ReadPropertyFile.passStringFromConfigFile(ConfigIdentifier.publicURL) + teacher.getUsername() + "/"  +  address);
            try {
                Files.copy(Paths.get(file.getPath()), Paths.get(file1.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientNetwork.sendFileToServer(teacher.getUsername(), address);


            String typeFile = "";
            index = address.lastIndexOf('.');
            String suffix = address.substring(index + 1);
            if(suffix.toLowerCase().equals("png")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("jpg")){
                typeFile = "PICTURE";
            }
            else if(suffix.toLowerCase().equals("pdf")){
                typeFile = "PDF";
            }
            else if(suffix.toLowerCase().equals("mp3")){
                typeFile = "MUSIC";
            }
            else if(suffix.toLowerCase().equals("mp4")){
                typeFile = "VIDEO";
            }

            for (int i = 0; i < wantsToSendFileInGroups.size(); i++) {
                String s1 = "";
                for (int j = 0; j < wantsToSendFileInGroups.get(i).length(); j++) {
                    if(wantsToSendFileInGroups.get(i).charAt(j) == '.'){
                        s1 = wantsToSendFileInGroups.get(i).substring(0, j);
                        break;
                    }
                }

                if(s1.equals("")){
                    s1 = wantsToSendFileInGroups.get(i);
                }

                clientNetwork.saveMessage(teacher.getUsername(), wantsToSendFileInGroups.get(i), address, typeFile);
            }

            wantsToSendFileInGroups = new LinkedList<>();

        }
        else{
            wantsToSendFileInGroups = new LinkedList<>();
        }

    }


    public void HomeButtonFunction(ActionEvent e) {

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistantFirstPage.fxml"));

            Parent root = loader.load();
            EducationalAssistantFirstPage EducationalAssistantFirstPage = loader.getController();
            EducationalAssistantFirstPage.setTeacher(teacher, clientNetwork);
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
            clientNetwork.changeLastLoginTeacher(teacher.getId(), localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(), localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear());


        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void CoursesListMenuFunction(ActionEvent event) {

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowCourseList.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowCourseList EducationalAssistant_ShowCourseList = loader.getController();
            EducationalAssistant_ShowCourseList.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherListMenu.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherListMenu EducationalAssistant_ShowTeacherListMenu = loader.getController();
            EducationalAssistant_ShowTeacherListMenu.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowWeeklySchedule.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowWeeklySchedule EducationalAssistant_ShowWeeklySchedule = loader.getController();
            EducationalAssistant_ShowWeeklySchedule.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowExams.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowExams EducationalAssistant_ShowExams = loader.getController();
            EducationalAssistant_ShowExams.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_Minor.fxml"));
            Parent root = loader.load();
            EducationalAssistant_Minor EducationalAssistant_Minor = loader.getController();
            EducationalAssistant_Minor.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_WithdrawalFromEducation.fxml"));
            Parent root = loader.load();
            EducationalAssistant_WithdrawalFromEducation EducationalAssistant_WithdrawalFromEducation = loader.getController();
            EducationalAssistant_WithdrawalFromEducation.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_GiveRecome.fxml"));
            Parent root = loader.load();
            EducationalAssistant_GiveRecome EducationalAssistant_GiveRecome = loader.getController();
            EducationalAssistant_GiveRecome.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScores.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScores EducationalAssistant_temporaryScores = loader.getController();
            EducationalAssistant_temporaryScores.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_StudentsStatus.fxml"));
            Parent root = loader.load();
            EducationalAssistant_StudentsStatus EducationalAssistant_StudentsStatus = loader.getController();
            EducationalAssistant_StudentsStatus.setTeacher(teacher, clientNetwork);
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_ShowTeacherProfile.fxml"));
            Parent root = loader.load();
            EducationalAssistant_ShowTeacherProfile EducationalAssistant_ShowTeacherProfile = loader.getController();
            EducationalAssistant_ShowTeacherProfile.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void AddNewUserFunction(ActionEvent event) {

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_AddNewUser.fxml"));
            Parent root = loader.load();
            EducationalAssistant_AddNewUser EducationalAssistant_AddNewUser = loader.getController();
            EducationalAssistant_AddNewUser.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void temporaryScoresAllStudentsFunction(ActionEvent event) {

        timeline.stop();
        try{
            stage = ((Stage)(((Scene)timeShowLabel.getScene()).getWindow()));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Fxml\\Teacher\\EducationalAssistant\\EducationalAssistant_temporaryScoresForAllStudents.fxml"));
            Parent root = loader.load();
            EducationalAssistant_temporaryScoresForAllStudents EducationalAssistant_temporaryScoresForAllStudents = loader.getController();
            EducationalAssistant_temporaryScoresForAllStudents.setTeacher(teacher, clientNetwork);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

