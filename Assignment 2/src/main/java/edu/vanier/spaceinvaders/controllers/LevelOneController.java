/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvadersmodels.User;
import edu.vanier.spaceinvadersmodels.GameObject;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author andyhou
 */
public class LevelOneController {
    
    static int score;

    @FXML
    ImageView userShipImage;
    
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
    Label lblScore;
    
    @FXML
    Label lblLevel;
    
    @FXML
    ImageView life1;
    
    @FXML
    ImageView life2;
    
    @FXML
    ImageView life3;
    
    List<ImageView> lifeImages = new ArrayList<>();

    @FXML
    public void initialize() {
        backgroundImage.setImage(new Image("/images/background/starfield_alpha.png"));
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitWidth(pane.getPrefWidth());
        backgroundImage.setFitHeight(pane.getPrefHeight());
        lifeImages.add(life1);
        lifeImages.add(life2);
        lifeImages.add(life3);
        for(ImageView life:lifeImages){
            life.setImage(new Image("/images/heart.png"));
        }
        portal.setFill(new ImagePattern(new Image("/images/portal.png")));
        GameObject.setPane(pane);
    }
    
    public void startLevelOne() throws InterruptedException, FileNotFoundException{
        
        EnemiesController enemies_Level_One = new EnemiesController(pane, new Image("/images/bullets/laserRed05.png"),0.8);
        enemies_Level_One.move();
        UserShipController spaceship_Level_One = new UserShipController(new User(userShipImage, 
                new Image("/images/spaceships/playerShip2_blue.png")), new Image("/images/bullets/laserBlue05.png"),
                pane, lblGameOver, lblCongratulations, portal, lblScore, lblLevel, lifeImages
        );
        spaceship_Level_One.move();
        
    }

    public Pane getPane() {
        return pane;
    }
    

}
