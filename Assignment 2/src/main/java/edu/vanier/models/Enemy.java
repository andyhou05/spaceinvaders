/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Enemy extends Sprite {

    static double horizontalMovementSpeed = 10;
    static double velocity;
    StackPane enemy;
    static ArrayList<Bullet> bullets = new ArrayList<>();

    public Enemy(StackPane enemy) {
        this.enemy = enemy;
        String enemyColor;
        StringBuilder filePathBuilder = new StringBuilder("/images/enemies/enemy");

        // Generate random enemy color.
        double random2 = Math.random();
        if (random2 <= (0.3333333333)) {
            enemyColor = "Red";
        } else if (random2 <= (0.6666666666)) {
            enemyColor = "Black";
        } else {
            enemyColor = "Green";
        }
        filePathBuilder.append(enemyColor);

        // Generate random enemy model.
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
        ImageView enemyImage = new ImageView(new Image(filePathBuilder.toString()));
        enemyImage.setPreserveRatio(false);
        enemyImage.setFitWidth(enemy.getPrefWidth());
        enemyImage.setFitHeight(enemy.getPrefHeight());
        enemy.getChildren().add(enemyImage);
    }

    public static double getVelocity() {
        return velocity;
    }

    public static void setVelocity(double velocity) {
        Enemy.velocity = velocity;
    }

    public static double getHorizontalMovementSpeed() {
        return horizontalMovementSpeed;
    }

    public static void setHorizontalMovementSpeed(double horizontalMovementSpeed) {
        Enemy.horizontalMovementSpeed = horizontalMovementSpeed;
    }

    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public StackPane getEnemy() {
        return enemy;
    }

}
