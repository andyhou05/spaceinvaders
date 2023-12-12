/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class GameObject {

    static Pane pane;
    static double width = 60;
    static double height = 45;
    ImageView objectImage = new ImageView();
    AudioClip explosionAudio = new AudioClip(getClass().getResource("/sounds/8bit_bomb_explosion.wav").toExternalForm());

    public GameObject() {
        objectImage.setFitWidth(width);
        objectImage.setFitWidth(height);
    }

    public void killAnimation(Pane spaceshipPane) {
        // we must create a new ImageView and add this to the main pane because
        // if the spaceship to kill is an enemy, simply changing the image of
        // the enemy will make it so that the explosion gif will move with the
        // enemiesPane which is undesired.
        ImageView explosion = new ImageView(new Image("/images/Explosion.gif"));
        explosion.setLayoutX(spaceshipPane.getLayoutX() + this.getObjectImage().getLayoutX());
        explosion.setLayoutY(spaceshipPane.getLayoutY() + this.getObjectImage().getLayoutY());
        pane.getChildren().add(explosion);
        this.getObjectImage().setVisible(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            pane.getChildren().remove(explosion);
        });
        pause.setCycleCount(1);
        pause.play();
        explosionAudio.play();
    }

    public ImageView getObjectImage() {
        return objectImage;
    }

    public void setObjectImage(ImageView objectImage) {
        this.objectImage = objectImage;
    }

    public static void setPane(Pane pane) {
        GameObject.pane = pane;
    }

    public AudioClip getExplosionAudio() {
        return explosionAudio;
    }

}
