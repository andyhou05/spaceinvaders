/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Enemy extends GameObject {
    
    static double speed;
    static double velocity;
    AudioClip enemyShootAudio = new AudioClip(getClass().getResource("/sounds/sfx_laser2.wav").toExternalForm());
    static ArrayList<Bullet> bullets = new ArrayList<>();

    public Enemy(double layoutX, double layoutY) {
        ImageView enemyImage = getObjectImage();
        String enemyColor;
        StringBuilder filePathBuilder = new StringBuilder("/images/enemies/enemy");

        // Generate random enemyStack color.
        double random2 = Math.random();
        if (random2 <= (0.3333333333)) {
            enemyColor = "Red";
        } else if (random2 <= (0.6666666666)) {
            enemyColor = "Black";
        } else {
            enemyColor = "Green";
        }
        filePathBuilder.append(enemyColor);

        // Generate random enemyStack model.
        double random1 = Math.random();
        if (random1 <= 0.2) {
            filePathBuilder.append('1');
        } else if (random1 <= 0.4) {
            filePathBuilder.append('2');
        } else if (random1 <= 0.6) {
            filePathBuilder.append('3');
        } else if (random1 <= 0.8) {
            filePathBuilder.append('4');
        } else {
            filePathBuilder.append('5');
        }
        filePathBuilder.append(".png");
        enemyImage.setImage(new Image(filePathBuilder.toString()));
        enemyImage.setPreserveRatio(false);
        enemyImage.setFitWidth(60);
        enemyImage.setFitHeight(45);
        enemyImage.setLayoutX(layoutX);
        enemyImage.setLayoutY(layoutY);
    }

    public static double getVelocity() {
        return velocity;
    }

    public static void setVelocity(double velocity) {
        Enemy.velocity = velocity;
    }

    public static double getSpeed() {
        return speed;
    }

    public static void setSpeed(double speed) {
        Enemy.speed = speed;
    }

    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public AudioClip getEnemyShootAudio() {
        return enemyShootAudio;
    }

}
