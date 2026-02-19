package MediaLab.Assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import Models.Category;
import Models.Notification;
import Models.NotificationType;
import Models.Status;
import Models.Task;
import Services.NotificationHistory;
import Services.TaskList;
import Utils.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	stage.setTitle("MediaLab Assistant");
    	stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.IMAGES.getPath() + "ntua_logo.png")));
    	stage.setMaximized(true);
    	
        scene = new Scene(loadFXML("Dashboard"));
        scene.getStylesheets().add(getClass().getResource(Path.DASHBOARD.getPath()).toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	Decoder.decodeDataFromJson();
    	launch();
    	Encoder.encodeDataToJson();
    }

}