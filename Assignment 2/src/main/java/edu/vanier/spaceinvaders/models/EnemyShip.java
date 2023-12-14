/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

/**
 * Class containing all information about EnemyShip.
 *
 * @author andyhou
 */
public class EnemyShip extends Spaceship {

    static double speed;
    static double velocity;
    AudioClip enemyShootAudio = new AudioClip(getClass().getResource("/sounds/sfx_laser2.wav").toExternalForm());
    static ArrayList<Bullet> bullets = new ArrayList<>();

    /**
     * Creates an Enemy with specified layoutX and layoutY.
     *
     * @param layoutX
     * @param layoutY
     */
    public EnemyShip(double layoutX, double layoutY) {
        ImageView enemyImage = getObjectImageView();
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

        // Sets the created resource path as the image of the enemy.
        enemyImage.setImage(new Image(filePathBuilder.toString()));
        enemyImage.setPreserveRatio(false);
        enemyImage.setFitWidth(Spaceship.getWidth());
        enemyImage.setFitHeight(Spaceship.getHeight());
        enemyImage.setLayoutX(layoutX);
        enemyImage.setLayoutY(layoutY);
    }

    /**
     *
     * @return Velocity of the EnemyShip class.
     */
    public static double getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the EnemyShip class, all EnemyShip objects have the same
 velocity since they move in an enemiesPane, velocity accounts for speed
 and direction.
     *
     * @param velocity
     */
    public static void setVelocity(double velocity) {
        EnemyShip.velocity = velocity;
    }

    /**
     *
     * @return Speed of the EnemyShip class.
     */
    public static double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the EnemyShip class, the speed is the absolute value of the
 velocity.
     *
     * @param speed
     */
    public static void setSpeed(double speed) {
        EnemyShip.speed = speed;
    }

    /**
     * 
     * @return list of EnemyShip bullets.
     */
    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * 
     * @return Shooting sound for EnemyShip.
     */
    public AudioClip getEnemyShootAudio() {
        return enemyShootAudio;
    }

}
