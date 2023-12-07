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
    ImageView shipImage = new ImageView();
    StackPane stack = new StackPane(shipImage);
    AudioClip explosionAudio = new AudioClip(getClass().getResource("/sounds/8bit_bomb_explosion.wav").toExternalForm());

    public void killAnimation(Pane spaceshipPane) {
        this.getShipImage().setImage(new Image("/images/Explosion.gif"));
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            spaceshipPane.getChildren().remove(this.getStack());
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
