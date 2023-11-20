/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Spaceship;
import edu.vanier.models.Sprite;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class LevelOneController {

    @FXML
    Rectangle spaceship;
    
    @FXML
    Rectangle enemy;

    @FXML
    Pane pane;
    
    @FXML
    Pane enemiesPane;

    @FXML
    public void initialize() {
        
    }
    
    public void startLevelOne() throws InterruptedException{
        Sprite.setPane(pane);
        
        SpaceshipController spaceship_Level_One = new SpaceshipController(new Spaceship(spaceship));
        spaceship_Level_One.move();
        
        EnemiesController enemies_Level_One = new EnemiesController(enemy, pane, enemiesPane, 0.2);
        enemies_Level_One.moveEnemies();
        
    }

    public Pane getPane() {
        return pane;
    }
    

}
