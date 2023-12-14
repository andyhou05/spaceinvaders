/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import java.util.List;
import javafx.scene.image.Image;
import static edu.vanier.spaceinvaders.models.GameObject.mainPane;

/**
 * Class for bullets in the game.
 *
 * @author 2276884
 */
public class Bullet extends GameObject {

    static double bulletSpeed = 3;
    boolean bulletSpin; // SpreadShot rocket type shoots a spinning rocket.

    /**
     * Creates a new Bullet with specified layoutX, layoutY, spaceshipWidth,
     * spaceshipHeight, image and spin.
     *
     * @param layoutX
     * @param layoutY
     * @param width
     * @param height
     * @param image
     * @param bulletSpin
     */
    public Bullet(double layoutX, double layoutY, double width, double height, Image image, boolean bulletSpin) {
        getObjectImageView().setImage(image);
        getObjectImageView().setFitWidth(width);
        getObjectImageView().setFitHeight(height);
        getObjectImageView().setLayoutX(layoutX);
        getObjectImageView().setLayoutY(layoutY);
        this.bulletSpin = bulletSpin;
    }

    /**
     * Returns a singleShot bullet.
     *
     * @param shooter
     * @param layoutX
     * @param layoutY
     * @param image
     * @return
     */
    public static Bullet singleShotBullet(GameObject shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getObjectImageView().getFitWidth() / 2, layoutY, 6, 35, image, false);
        mainPane.getChildren().add(bullet.getObjectImageView());
        return bullet;
    }

    /**
     * Returns a single speedShot bullet.
     *
     * @param shooter
     * @param layoutX
     * @param layoutY
     * @param image
     * @return
     */
    public static Bullet speedShotBullet(GameObject shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getObjectImageView().getFitWidth() / 2, layoutY, 4, 25, image, false);
        mainPane.getChildren().add(bullet.getObjectImageView());
        return bullet;
    }

    /**
     * Returns a single spreadShot bullet.
     *
     * @param shooter
     * @param layoutX
     * @param layoutY
     * @param image
     * @return
     */
    public static Bullet spreadShotBullet(GameObject shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getObjectImageView().getFitWidth() / 2, layoutY, 20, 20, image, true);
        mainPane.getChildren().add(bullet.getObjectImageView());
        return bullet;
    }

    /**
     * Removes the specified bullet from the main mainPane.
     *
     * @param bullet
     */
    public static void removeBullet(Bullet bullet) {
        mainPane.getChildren().remove(bullet.getObjectImageView());
    }

    /**
     * Removes a specified list of bullets from the main mainPane and clears the
     * list.
     *
     * @param bullets
     */
    public static void removeBullet(List<Bullet> bullets) {
        for (Bullet b : bullets) {
            mainPane.getChildren().remove(b.getObjectImageView());
        }
        bullets.removeAll(bullets);
    }

    /**
     * Moves the bullet in correct direction.
     *
     * @param bullets List of bullets to move.
     * @param isEnemy Determines if the list of bullets is from enemies or the
     * user
     */
    public static void moveBullets(List<Bullet> bullets, boolean isEnemy) {
        double direction;
        // Bullet needs to go downwards if it is an enemy.
        if (isEnemy) {
            direction = 1;
        } // Bullet needs to go downwards if it is an enemy. 
        else {
            direction = -1;
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet currentBullet = bullets.get(i);
            currentBullet.getObjectImageView().setLayoutY(currentBullet.getObjectImageView().getLayoutY() + direction * bulletSpeed);

            // Rotate the bullet if it hsa spin.
            if (currentBullet.bulletSpin) {
                currentBullet.getObjectImageView().setRotate(currentBullet.getObjectImageView().getRotate() + 15);
            }
            // If the bullet goes outside of the mainPane, remove it from the mainPane and from the list it belongs.
            if (currentBullet.getObjectImageView().getLayoutY() < -currentBullet.getObjectImageView().getFitHeight() || currentBullet.getObjectImageView().getLayoutY() > mainPane.getPrefHeight()) {
                // Remove from mainPane.
                removeBullet(currentBullet);
                // Remove from list.
                bullets.remove(currentBullet);
            }
        }
    }
}
