/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvadersmodels;

import static edu.vanier.spaceinvadersmodels.GameObject.pane;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author 2276884
 */
public class Bullet extends GameObject {

    static double bulletSpeed = 3;

    public Bullet(double layoutX, double layoutY, Image image) {
        getObjectImage().setImage(image);
        getObjectImage().setFitWidth(6);
        getObjectImage().setFitHeight(35);
        getObjectImage().setLayoutX(layoutX);
        getObjectImage().setLayoutY(layoutY);
    }

    public static Bullet singleShot(GameObject shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getObjectImage().getFitWidth() / 2, layoutY, image);
        pane.getChildren().add(bullet.getObjectImage());
        return bullet;
    }

    public static void removeBullet(Bullet bullet) {
        pane.getChildren().remove(bullet.getObjectImage());
    }

    public static void removeBullet(List<Bullet> bullets) {
        
        for (Bullet b: bullets) {
            pane.getChildren().remove(b.getObjectImage());
        }
        bullets.removeAll(bullets);
    }

    public static void moveBullets(List<Bullet> bullets, boolean isEnemy) {
        double direction;
        if (isEnemy) {
            direction = 1;
        } else {
            direction = -1;
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet currentBullet = bullets.get(i);
            currentBullet.getObjectImage().setLayoutY(currentBullet.getObjectImage().getLayoutY() + direction * bulletSpeed);
            if (currentBullet.getObjectImage().getLayoutY() < -currentBullet.getObjectImage().getFitHeight() || currentBullet.getObjectImage().getLayoutY() > pane.getPrefHeight()) {
                // remove from pane
                removeBullet(currentBullet);
                // remove from list
                bullets.remove(currentBullet);
            }
        }
    }

}
