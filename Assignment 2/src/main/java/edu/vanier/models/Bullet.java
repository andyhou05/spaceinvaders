/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import static edu.vanier.models.Spaceship.pane;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author 2276884
 */
public class Bullet extends Spaceship {

    static double bulletSpeed = 3;

    public Bullet(double layoutX, double layoutY, Image image) {
        getShipImage().setImage(image);
        getStack().setPrefWidth(4);
        getStack().setPrefHeight(20);
        getStack().setLayoutX(layoutX);
        getStack().setLayoutY(layoutY);
    }

    public static Bullet singleShot(Spaceship shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getStack().getWidth() / 2, layoutY, image);
        pane.getChildren().add(bullet.getStack());
        return bullet;
    }
    
    public static void removeBullet(Bullet bullet){
        pane.getChildren().remove(bullet.getStack());
    }

    public static void moveBullets(List<Bullet> bullets, boolean isEnemy) {
        double direction;
        if (isEnemy) {
            direction = 1;
        } else {
            direction = -1;
        }
        for (Bullet b : bullets) {
            b.getStack().setLayoutY(b.getStack().getLayoutY() + direction*bulletSpeed);
        }
    }

}
