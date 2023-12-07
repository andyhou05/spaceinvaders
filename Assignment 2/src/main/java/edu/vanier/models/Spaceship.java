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
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class Spaceship {

    static Pane pane;
    static double width = 60;
    static double height = 45;
    ImageView shipImage = new ImageView();
    StackPane stack = new StackPane(shipImage);
    AudioClip explosionAudio = new AudioClip(getClass().getResource("/sounds/8bit_bomb_explosion.wav").toExternalForm());

    public void killAnimation(Pane spaceshipPane) {
        // we must create a new ImageView and add this to the main pane because
        // if the spaceship to kill is an enemy, simply changing the image of
        // the enemy will make it so that the explosion gif will move with the
        // enemiesPane which is undesired.
        ImageView explosion = new ImageView(new Image("/images/Explosion.gif"));
        explosion.setLayoutX(spaceshipPane.getLayoutX() + this.getStack().getLayoutX());
        explosion.setLayoutY(spaceshipPane.getLayoutY() + this.getStack().getLayoutY());
        pane.getChildren().add(explosion);
        spaceshipPane.getChildren().remove(this.getStack());
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            pane.getChildren().remove(explosion);
        });
        pause.setCycleCount(1);
        pause.play();
        explosionAudio.play();
    }

    public StackPane getStack() {
        return stack;
    }

    public void setStack(StackPane stack) {
        this.stack = stack;
    }

    public ImageView getShipImage() {
        return shipImage;
    }

    public void setShipImage(ImageView shipImage) {
        this.shipImage = shipImage;
    }

    public static void setPane(Pane pane) {
        Spaceship.pane = pane;
    }

    public AudioClip getExplosionAudio() {
        return explosionAudio;
    }

}
