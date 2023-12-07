package edu.vanier.spaceinvaders.main;

import edu.vanier.spaceinvaders.controllers.LevelOneController;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is a JavaFX project template to be used for creating GUI applications.
 * JavaFX 20.0.2 is already linked to this project in the build.gradle file.
 * @link: https://openjfx.io/javadoc/20/
 * @see: Build Scripts/build.gradle
 * @author Sleiman Rabah.
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/spaceinvaderslevel1.fxml"));
        LevelOneController controller = new LevelOneController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controller.startLevelOne();
        

        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
