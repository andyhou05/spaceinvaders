/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import static edu.vanier.models.Sprite.pane;
import java.util.ArrayList;
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
public class Bullet extends Sprite {

    public Bullet(double layoutX, double layoutY, Image image) {
        getSpriteImage().setImage(image);
        getSpriteStack().setPrefWidth(4);
        getSpriteStack().setPrefHeight(20);
        getSpriteStack().setLayoutX(layoutX);
        getSpriteStack().setLayoutY(layoutY);
    }

    public static Bullet singleShot(Sprite shooter, double layoutX, double layoutY, Image image) {
        Bullet bullet = new Bullet(layoutX + shooter.getSpriteStack().getWidth() / 2, layoutY, image);
        pane.getChildren().add(bullet.getSpriteStack());
        return bullet;
    }

}
