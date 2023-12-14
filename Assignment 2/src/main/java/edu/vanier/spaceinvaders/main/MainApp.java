package edu.vanier.spaceinvaders.main;

import edu.vanier.spaceinvaders.controllers.EnemiesController;
import edu.vanier.spaceinvaders.controllers.FXMLUserLevelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is a JavaFX project template to be used for creating GUI applications.
 * JavaFX 20.0.2 is already linked to this project in the build.gradle file.
 * @link: https://openjfx.io/javadoc/20/
 * @see: Build Scripts/build.gradle
 * @author Sleiman Rabah.
 */
public class MainApp extends Application {

    public static FXMLUserLevelController controller = new FXMLUserLevelController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set its controller.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/space_invaders_layout.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        // Set the scene graph to the scene.
        Scene scene = new Scene(root);
        // Set the scene to the primary stage.
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        // Start the game.
        controller.startGame();
    }
    
    @Override
    public void stop(){
        // Stop any running animations.
        controller.animation.stop();
        EnemiesController.enemyAnimation.stop();
    }
}
