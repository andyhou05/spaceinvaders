/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Enemy extends Sprite{
    static double horizontalMovementSpeed = 10;
    static double velocity;
    Rectangle enemyBody;
    StackPane enemy;
    static ArrayList<Bullet> bullets = new ArrayList<>();

    public Enemy(StackPane enemy) {
        this.enemy = enemy;
        enemyBody = (Rectangle)enemy.getChildren().get(0);
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

    public Rectangle getEnemyBody() {
        return enemyBody;
    }

    public void setEnemyBody(Rectangle enemyBody) {
        this.enemyBody = enemyBody;
    }

    public StackPane getEnemy() {
        return enemy;
    }
    
}
