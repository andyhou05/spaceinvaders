/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.controllers.EnemiesController;
import edu.vanier.models.Bullet;
import edu.vanier.models.Enemy;
import edu.vanier.models.Spaceship;
import edu.vanier.models.Sprite;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class SpaceshipController {

    static ArrayList<Bullet> enemyBullets = new ArrayList<>();
    Spaceship spaceship;
    Image spaceshipBulletImage;
    int speed = Spaceship.getSpeed();

    public SpaceshipController(Spaceship spaceship, Image spaceshipBulletImage) {
        this.spaceship = spaceship;
        this.spaceshipBulletImage = spaceshipBulletImage;
    }

    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            setSpaceshipAnimation();
        }
    };

    public void setSpaceshipAnimation() {
        // Border detection, if we reach a border, set the left/right speed to 0.
        double leftWall = 0;
        double rightWall = ((Pane) spaceship.getSpaceship().getParent()).getPrefWidth() - spaceship.getSpaceship().getWidth();
        double topWall = 0;
        double bottomWall = ((Pane) spaceship.getSpaceship().getParent()).getPrefHeight() - spaceship.getSpaceship().getHeight();
        int currentLeft = -speed;
        int currentRight = speed;
        int currentTop = -speed;
        int currentBottom = speed;
        
        // spaceship hits the left wall
        if (spaceship.getSpaceship().getLayoutX() <= leftWall) {
            spaceship.getSpaceship().setLayoutX(leftWall);
            setSpaceshipMechanics(speed, 0, currentTop, currentBottom, spaceship.isCanShoot());
            currentLeft = 0;
            currentRight = speed;
        } // spaceship hits the right wall
        else if (spaceship.getSpaceship().getLayoutX() >= rightWall) {
            spaceship.getSpaceship().setLayoutX(rightWall);
            setSpaceshipMechanics(0, -speed, currentTop, currentBottom, spaceship.isCanShoot());
            currentRight = 0;
            currentLeft = speed;
        } // spaceship hits the top wall
        if (spaceship.getSpaceship().getLayoutY() <= topWall) {
            spaceship.getSpaceship().setLayoutY(topWall);
            setSpaceshipMechanics(currentRight, currentLeft, 0, speed, spaceship.isCanShoot());
            currentTop = 0;
            currentBottom = speed;
        } // spaceship hits the bottom wall
        else if (spaceship.getSpaceship().getLayoutY() >= bottomWall) {
            spaceship.getSpaceship().setLayoutY(bottomWall);
            setSpaceshipMechanics(currentRight, currentLeft, -speed, 0, spaceship.isCanShoot());
            currentBottom = 0;
            currentTop = speed;
            
        } // no walls hit
        else {
            setSpaceshipMechanics(speed, -speed, -speed, speed, spaceship.isCanShoot());
        }

        // move the spaceship
        spaceship.getSpaceship().setLayoutX(spaceship.getSpaceship().getLayoutX() + spaceship.getxVelocity());
        spaceship.getSpaceship().setLayoutY(spaceship.getSpaceship().getLayoutY() + spaceship.getyVelocity());

        // move the bullets
        for (Bullet b : spaceship.getBullet()) {
            b.setLayoutY(b.getLayoutY() - 3);
        }

        // Check for collisions.
        if (!spaceship.isInvincible()) {
            checkBulletCollision();
        }

    }

    public void setSpaceshipMechanics(int right, int left, int top, int bottom, boolean canShoot) {
        spaceship.getSpaceship().getScene().setOnKeyPressed((e) -> {
            switch (e.getCode()) {
                case D:
                    spaceship.setxVelocity(right);
                    break;
                case A:
                    spaceship.setxVelocity(left);
                    break;
                case W:
                    spaceship.setyVelocity(top);
                    break;
                case S:
                    spaceship.setyVelocity(bottom);
                    break;
                case SPACE:
                    if (canShoot) {
                        Bullet newBullet = Sprite.shoot(spaceship, spaceship.getSpaceship().getLayoutX(), spaceship.getSpaceship().getLayoutY() - spaceship.getSpaceship().getHeight(), spaceshipBulletImage);
                        spaceship.getBullet().add(newBullet);
                        EnemiesController.spaceshipBullets.add(newBullet);
                    }
                    break;
            }
        });
        spaceship.getSpaceshipBody().getScene().setOnKeyReleased((e) -> {
            spaceship.setxVelocity(0);
            spaceship.setyVelocity(0);
        });
    }

    public void checkBulletCollision() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (spaceship.getSpaceship().getBoundsInParent().intersects(enemyBullets.get(i).getBoundsInParent())) {
                Sprite.removeEntity(enemyBullets.get(i));
                spaceship.setInvincible(true);
                spaceshipHitAnimation();
                Enemy.getBullets().remove(enemyBullets.get(i));
                enemyBullets.remove(enemyBullets.get(i));
            }
        }
    }

    public void spaceshipHitAnimation() {
        Timeline spaceshipFlashing = new Timeline(new KeyFrame(Duration.seconds(0.1),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        },
                new KeyValue(spaceship.getSpaceship().opacityProperty(), 0)
        ));
        spaceshipFlashing.setAutoReverse(true);
        spaceshipFlashing.setCycleCount(8);
        spaceshipFlashing.play();

        Timeline spaceshipInvincible = new Timeline(new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                spaceship.setInvincible(false);
            }
        })
        );
        spaceshipInvincible.setCycleCount(1);
        spaceshipInvincible.play();
    }

    public void move() {
        animation.start();
    }

    public static ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

}
