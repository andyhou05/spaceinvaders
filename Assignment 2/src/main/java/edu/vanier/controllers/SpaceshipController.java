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

        // spaceship hits the left wall
        if (spaceship.getSpaceship().getLayoutX() <= leftWall) {
            spaceship.getSpaceship().setLayoutX(leftWall);
            setSpaceshipMechanics(speed, 0, spaceship.isCanShoot());
        } // spaceship hits the right wall
        else if (spaceship.getSpaceship().getLayoutX() >= rightWall) {
            spaceship.getSpaceship().setLayoutX(rightWall);
            setSpaceshipMechanics(0, -speed, spaceship.isCanShoot());
        } // no walls hit
        else {
            setSpaceshipMechanics(speed, -speed, spaceship.isCanShoot());
        }

        // move the spaceship
        spaceship.getSpaceship().setLayoutX(spaceship.getSpaceship().getLayoutX() + spaceship.getVelocity());

        // move the bullets
        for (Bullet b : spaceship.getBullet()) {
            b.setLayoutY(b.getLayoutY() - 3);
        }

        // Check for collisions.
        if (!spaceship.isInvincible()) {
            checkBulletCollision();
        }

    }

    public void setSpaceshipMechanics(int right, int left, boolean canShoot) {
        spaceship.getSpaceship().getScene().setOnKeyPressed((e) -> {
            switch (e.getCode()) {
                case D:
                    spaceship.setVelocity(right);
                    break;
                case A:
                    spaceship.setVelocity(left);
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
            spaceship.setVelocity(0);
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
