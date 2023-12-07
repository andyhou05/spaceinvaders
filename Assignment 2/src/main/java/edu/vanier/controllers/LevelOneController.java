/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.User;
import edu.vanier.models.Spaceship;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author andyhou
 */
public class LevelOneController {

    @FXML
    StackPane spaceship;
    
    @FXML
    Pane pane;
    
    @FXML
    ImageView backgroundImage;
    
    @FXML
    Label lblGameOver;
    
    @FXML
    Label lblCongratulations;
    
    @FXML
    Circle portal;

    @FXML
    public void initialize() {
        
    }
    
    public void startLevelOne() throws InterruptedException, FileNotFoundException{
        backgroundImage.setImage(new Image("/images/background/starfield_alpha.png"));
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitWidth(pane.getPrefWidth());
        backgroundImage.setFitHeight(pane.getPrefHeight());
        portal.setFill(new ImagePattern(new Image("/images/portal.png")));
        Spaceship.setPane(pane);
        
        EnemiesController enemies_Level_One = new EnemiesController(pane, new Image("/images/bullets/laserRed05.png"), lblCongratulations, portal, 0.8);
        enemies_Level_One.spawn(1);
        enemies_Level_One.move();
        SpaceshipController spaceship_Level_One = new SpaceshipController(new User(spaceship, 
                new Image("/images/spaceships/playerShip2_blue.png")), new Image("/images/bullets/laserBlue05.png"),
                pane,
                lblGameOver
        );
        spaceship_Level_One.move();
        
    }

    public Pane getPane() {
        return pane;
    }
    

}
