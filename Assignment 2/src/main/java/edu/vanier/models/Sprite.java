/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class Sprite {

    static Pane pane;
    ImageView spriteImage = new ImageView();
    StackPane spriteStack = new StackPane(spriteImage);

    public StackPane getSpriteStack() {
        return spriteStack;
    }

    public void setSpriteStack(StackPane spriteStack) {
        this.spriteStack = spriteStack;
    }

    public ImageView getSpriteImage() {
        return spriteImage;
    }

    public void setSpriteImage(ImageView spriteImage) {
        this.spriteImage = spriteImage;
    }

    public static void setPane(Pane pane) {
        Sprite.pane = pane;
    }

    public static void removeEntity(Sprite entity) {
        pane.getChildren().remove(entity.getSpriteStack());
    }
}
