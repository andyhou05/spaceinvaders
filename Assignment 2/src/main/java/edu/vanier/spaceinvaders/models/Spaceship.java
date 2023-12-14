/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import static edu.vanier.spaceinvaders.models.GameObject.mainPane;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Parent class of all Spaceships in the game.
 *
 * @author Andy
 */
public abstract class Spaceship extends GameObject {

    // All spaceships have same dimensions.
    final static double width = 60;
    final static double height = 45;

    /**
     * Plays an animation when a spaceship is dead.
     *
     * @param spaceshipPane The pane in which the spaceship lives.
     */
    public void killAnimation(Pane spaceshipPane) {
        // we must create a new ImageView and add this to the main mainPane because
        // if the spaceship to kill is an enemy, simply changing the image of
        // the enemy will make it so that the explosion gif will move with the
        // enemiesPane which is undesired.
        ImageView explosion = new ImageView(new Image("/images/other/Explosion.gif"));
        explosion.setLayoutX(spaceshipPane.getLayoutX() + this.getObjectImageView().getLayoutX());
        explosion.setLayoutY(spaceshipPane.getLayoutY() + this.getObjectImageView().getLayoutY());
        mainPane.getChildren().add(explosion);

        // Hide the image instead of removing it from the spaceshipPane.
        // If the spaceship killed is the user and we restart the level,
        // all we have to do it set it to visible instead of creating a new ImageView.
        this.getObjectImageView().setVisible(false);

        // Show the explostion for 0.5 seconds.
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            mainPane.getChildren().remove(explosion);
        });
        pause.setCycleCount(1);
        pause.play();
        explosionAudio.play();
    }

    /**
     *
     * @return Width of Spaceship.
     */
    public static double getWidth() {
        return width;
    }

    /**
     *
     * @return Height of Spaceship.
     */
    public static double getHeight() {
        return height;
    }

}
