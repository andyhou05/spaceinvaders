/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Bullet;
import static edu.vanier.models.Bullet.singleShot;
import edu.vanier.models.Enemy;
import edu.vanier.models.Spaceship;
import edu.vanier.models.Sprite;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class SpaceshipController {

    static ArrayList<Bullet> enemyBullets = new ArrayList<>();
    static Pane pane;
    static Spaceship spaceship;
    static Text txtGameOver;
    Image spaceshipBulletImage;
    int speed = Spaceship.getSpeed();
    AudioClip spaceshipHitAudio = new AudioClip(getClass().getResource("/sounds/sfx_shieldDown.wav").toExternalForm());
    static AudioClip gameOverAudio = new AudioClip(SpaceshipController.class.getResource("/sounds/gameOver.wav").toExternalForm());

    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            // *refactor this*
            setSpaceshipAnimation();
        }
    };

    public SpaceshipController(Spaceship spaceship, Image spaceshipBulletImage, Pane pane, Text lblGameOver) {
        this.spaceship = spaceship;
        this.spaceshipBulletImage = spaceshipBulletImage;
        SpaceshipController.pane = pane;
        SpaceshipController.txtGameOver = lblGameOver;
    }

    public void setSpaceshipAnimation() {
        // Border detection, if we reach a border, set the left/right speed to 0.
        double leftWall = 0;
        double rightWall = ((Pane) spaceship.getSpriteStack().getParent()).getPrefWidth() - spaceship.getSpriteStack().getWidth();
        double topWall = 0;
        double bottomWall = ((Pane) spaceship.getSpriteStack().getParent()).getPrefHeight() - spaceship.getSpriteStack().getHeight();
        int currentLeft = -speed;
        int currentRight = speed;
        int currentTop = -speed;
        int currentBottom = speed;

        // spaceship hits the left wall
        if (spaceship.getSpriteStack().getLayoutX() <= leftWall) {
            spaceship.getSpriteStack().setLayoutX(leftWall);
            currentLeft = 0;
            currentRight = speed;
        }
        // spaceship hits the right wall
        else if (spaceship.getSpriteStack().getLayoutX() >= rightWall) {
            spaceship.getSpriteStack().setLayoutX(rightWall);
            currentRight = 0;
            currentLeft = -speed;
        }
        // spaceship hits the top wall
        if (spaceship.getSpriteStack().getLayoutY() <= topWall) {
            spaceship.getSpriteStack().setLayoutY(topWall);
            currentTop = 0;
            currentBottom = speed;
        } // spaceship hits the bottom wall
        else if (spaceship.getSpriteStack().getLayoutY() >= bottomWall) {
            spaceship.getSpriteStack().setLayoutY(bottomWall);
            currentBottom = 0;
            currentTop = -speed;

        }
         setSpaceshipMechanics(currentRight, currentLeft, currentTop, currentBottom, spaceship.isCanShoot());

        // move the spaceship
        spaceship.getSpriteStack().setLayoutX(spaceship.getSpriteStack().getLayoutX() + spaceship.getxVelocity());
        spaceship.getSpriteStack().setLayoutY(spaceship.getSpriteStack().getLayoutY() + spaceship.getyVelocity());

        // move the bullets
        for (Bullet b : spaceship.getBullets()) {
            b.getSpriteStack().setLayoutY(b.getSpriteStack().getLayoutY() - 3);
        }

        // Check for collisions.
        if (!spaceship.isInvincible()) {
            checkBulletCollision();
            checkEnemyCollision();
        }

    }

    public void setSpaceshipMechanics(int right, int left, int top, int bottom, boolean canShoot) {
        spaceship.getSpriteStack().getScene().setOnKeyPressed((e) -> {
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
                case E:
                    switchShoot();
                    break;
                case SPACE:
                    if (canShoot) {
                        if (spaceship.isSingleShot()) {
                            addBullet(Bullet.singleShot(spaceship, spaceship.getSpriteStack().getLayoutX(), spaceship.getSpriteStack().getLayoutY() - spaceship.getSpriteStack().getHeight(), spaceshipBulletImage));
                            spaceship.getSpaceshipShootAudio().play();
                        } else if (spaceship.isSpeedShot()) {
                            speedShot(spaceship, spaceshipBulletImage);
                        } else {
                            System.out.println("spread");
                        }
                    }
                    break;
            }
        });
        spaceship.getSpriteStack().getScene().setOnKeyReleased((e) -> {
            spaceship.setxVelocity(0);
            spaceship.setyVelocity(0);
        });
    }

    public void speedShot(Spaceship shooter, Image image) {
        SequentialTransition speedShotAnimation = new SequentialTransition();
        double height = shooter.getSpriteStack().getPrefHeight();
        addBullet(singleShot(shooter, shooter.getSpriteStack().getLayoutX(), shooter.getSpriteStack().getLayoutY() - height, image));
        spaceship.getSpaceshipShootAudio().play();
        for (int i = 0; i < 2; i++) {
            PauseTransition bulletPause = new PauseTransition(Duration.seconds(0.25));
            bulletPause.setOnFinished((event) -> {
                addBullet(singleShot(shooter, shooter.getSpriteStack().getLayoutX(), shooter.getSpriteStack().getLayoutY() - height, image));
                spaceship.getSpaceshipShootAudio().play();
            });
            bulletPause.setCycleCount(1);
            speedShotAnimation.getChildren().add(bulletPause);
        }
        speedShotAnimation.play();
    }

    public static Bullet[] spreadShot(Sprite shooter, double layoutX, double layoutY, Image image) {
        return null;
    }

    public void switchShoot() {
        if (spaceship.isSingleShot()) {
            spaceship.setShot(2);
        } else if (spaceship.isSpeedShot()) {
            spaceship.setShot(3);
        } else {
            spaceship.setShot(1);
        }
    }

    public void checkBulletCollision() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (spaceship.getSpriteStack().getBoundsInParent().intersects(enemyBullets.get(i).getSpriteStack().getBoundsInParent())) {
                Sprite.removeEntity(enemyBullets.get(i));
                spaceshipHit();
                Enemy.getBullets().remove(enemyBullets.get(i));
                enemyBullets.remove(enemyBullets.get(i));
            }
        }
    }

    public void checkEnemyCollision() {
        for (Enemy currentEnemy : EnemiesController.getEnemies()) {
            if (pane.localToScene(EnemiesController.enemiesPane.localToScene(currentEnemy.getSpriteStack().getBoundsInParent()))
                    .intersects(spaceship.getSpriteStack().getBoundsInParent())) {
                spaceshipHit();
            }
        }
    }

    public void spaceshipHit() {
        spaceship.setInvincible(true);
        spaceshipHitAnimation();
        spaceship.setLives(spaceship.getLives() - 1);
        if (spaceship.getLives() == 0) {
            spaceship.killAnimation(pane);
            animation.stop();
            EnemiesController.enemyAnimation.stop();
            gameOverAudio.play();
            txtGameOver.setVisible(true);
        }
    }

    public void spaceshipHitAnimation() {
        spaceshipHitAudio.play();
        Timeline spaceshipFlashing = new Timeline(new KeyFrame(Duration.seconds(0.1),
                new KeyValue(spaceship.getSpriteStack().opacityProperty(), 0)
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

    public void addBullet(Bullet newBullet) {
        spaceship.getBullets().add(newBullet);
        EnemiesController.spaceshipBullets.add(newBullet);
    }

    public void move() {
        animation.start();
    }

    public static ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

}
