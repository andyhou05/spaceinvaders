/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import edu.vanier.controllers.LevelOneController;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class Spaceship {

    static int x = 0;
    static Rectangle spaceship;
    static ArrayList<Rectangle> bullet = new ArrayList<>();
    static AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            setSpaceshipSpeed();
        }
    };

    public static void setSpaceship(Rectangle spaceship) {
        Spaceship.spaceship = spaceship;
    }

    public static void setSpaceshipSpeed() {
        // Border detection, if we reach a border, set the left/right speed to 0.
        double leftWall = 0;
        double rightWall = ((Pane) spaceship.getParent()).getPrefWidth() - spaceship.getWidth();

        // spaceship hits the left wall
        if (spaceship.getLayoutX() <= leftWall) {
            spaceship.setLayoutX(leftWall);
            setSpaceshipMechanics(5, 0);
        } // spaceship hits the right wall
        else if (spaceship.getLayoutX() >= rightWall) {
            spaceship.setLayoutX(rightWall);
            setSpaceshipMechanics(0, -5);
        } // no walls hit
        else {
            setSpaceshipMechanics(5, -5);
        }
        Enemies.checkBulletCollision();

        // move the spaceship
        spaceship.setLayoutX(spaceship.getLayoutX() + x);

        for (Rectangle b : bullet) {
            b.setLayoutY(b.getLayoutY() - 3);

        }

    }

    public static void setSpaceshipMechanics(int right, int left) {
        spaceship.getScene().setOnKeyPressed((e) -> {
            switch (e.getCode()) {
                case D:
                    x = right;
                    break;
                case A:
                    x = left;
                    break;
                case SPACE:
                    bullet.add(Sprite.shoot(spaceship));
                    break;
            }
        });
        spaceship.getScene().setOnKeyReleased((e) -> {
            x = 0;
        });
    }

    public static void move() {
        animation.start();
    }

}
