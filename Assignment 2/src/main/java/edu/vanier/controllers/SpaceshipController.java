/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Bullet;
import static edu.vanier.models.Bullet.singleShot;
import edu.vanier.models.Enemy;
import edu.vanier.models.User;
import edu.vanier.models.Spaceship;
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
import javax.sound.midi.Sequence;

/**
 *
 * @author andyhou
 */
public class SpaceshipController {

    ArrayList<Bullet> enemyBullets = Enemy.getBullets();
    static Pane pane;
    static User userShip;
    static Label lblGameOver;
    Image userShipBulletImage;
    int speed = User.getSpeed();
    AudioClip spaceshipHitAudio = new AudioClip(getClass().getResource("/sounds/sfx_shieldDown.wav").toExternalForm());
    static AudioClip gameOverAudio = new AudioClip(SpaceshipController.class.getResource("/sounds/gameOver.wav").toExternalForm());

    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            checkWallCollision(); // this also updates the speed of userShip if wall is encountered, ex: left wall encountered -> pressing "A" won't do anything.
            moveSpaceship();
            // move the bullets
            Bullet.moveBullets(userShip.getBullets(), false);
            // Check for collisions.
            checkBulletEnemyCollisions();
            // Check if the userShip is in the portal at the end of the level.
            checkGameOverPortal();
        }
    };

    public SpaceshipController(User spaceship, Image spaceshipBulletImage, Pane pane, Label lblGameOver) {
        this.userShip = spaceship;
        this.userShipBulletImage = spaceshipBulletImage;
        SpaceshipController.pane = pane;
        SpaceshipController.lblGameOver = lblGameOver;
    }

    private void checkWallCollision() {
        // Border detection, if we reach a border, set the left/right speed to 0.
        double leftWall = 0;
        double rightWall = pane.getPrefWidth() - userShip.getStack().getWidth();
        double topWall = 0;
        double bottomWall = pane.getPrefHeight() - userShip.getStack().getHeight();
        int currentLeft = -speed;
        int currentRight = speed;
        int currentTop = -speed;
        int currentBottom = speed;

        // userShip hits the left wall
        if (userShip.getStack().getLayoutX() <= leftWall) {
            userShip.getStack().setLayoutX(leftWall);
            currentLeft = 0;
            currentRight = speed;
        } // userShip hits the right wall
        else if (userShip.getStack().getLayoutX() >= rightWall) {
            userShip.getStack().setLayoutX(rightWall);
            currentRight = 0;
            currentLeft = -speed;
        }
        // userShip hits the top wall
        if (userShip.getStack().getLayoutY() <= topWall) {
            userShip.getStack().setLayoutY(topWall);
            currentTop = 0;
            currentBottom = speed;
        } // userShip hits the bottom wall
        else if (userShip.getStack().getLayoutY() >= bottomWall) {
            userShip.getStack().setLayoutY(bottomWall);
            currentBottom = 0;
            currentTop = -speed;

        }
        setSpaceshipMechanics(currentRight, currentLeft, currentTop, currentBottom);
    }

    public void setSpaceshipMechanics(int right, int left, int top, int bottom) {
        userShip.getStack().getScene().setOnKeyPressed((e) -> {
            switch (e.getCode()) {
                case D:
                    userShip.setxVelocity(right);
                    break;
                case A:
                    userShip.setxVelocity(left);
                    break;
                case W:
                    userShip.setyVelocity(top);
                    break;
                case S:
                    userShip.setyVelocity(bottom);
                    break;
                case E:
                    switchShoot();
                    break;
                case SPACE:
                    if (userShip.isCanShoot() && userShip.isSingleShot()) {
                        addBullet(Bullet.singleShot(userShip, userShip.getStack().getLayoutX(), userShip.getStack().getLayoutY() - userShip.getStack().getHeight(), userShipBulletImage));
                        userShip.getSpaceshipShootAudio().play();
                        delaySingleShoot();

                    } else if (userShip.isCanSpeedShoot() && userShip.isSpeedShot()) {
                        speedShot(userShip);
                        delaySpeedShoot();
                    } else if (userShip.isCanSpreadShoot() && userShip.isSpreadShot()) {
                        spreadShot();
                        delaySpreadShoot();
                    }

                    break;
            }
        });
        userShip.getStack().getScene().setOnKeyReleased((e) -> {
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
        });
    }

    private void moveSpaceship() {
        userShip.getStack().setLayoutX(userShip.getStack().getLayoutX() + userShip.getxVelocity());
        userShip.getStack().setLayoutY(userShip.getStack().getLayoutY() + userShip.getyVelocity());
    }

    private void checkGameOverPortal() {
        if (EnemiesController.portal.opacityProperty().get() == 1.0
                && userShip.getStack().getBoundsInParent().intersects(EnemiesController.portal.getBoundsInParent())) {
            setSpaceshipMechanics(0, 0, 0, 0);
            speed = 0;
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
            Timeline teleport = new Timeline(new KeyFrame(Duration.seconds(2), new KeyValue(userShip.getStack().rotateProperty(), 360 * 5)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(userShip.getStack().scaleXProperty(), 0)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(userShip.getStack().scaleYProperty(), 0)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(userShip.getStack().layoutXProperty(), EnemiesController.portal.getLayoutX() - 0.5 * userShip.getStack().getPrefWidth())),
                    new KeyFrame(Duration.seconds(1), new KeyValue(userShip.getStack().layoutYProperty(), EnemiesController.portal.getLayoutY() - 0.5 * userShip.getStack().getPrefHeight()))
            );
            teleport.setCycleCount(1);
            teleport.play();
        }
    }

    private void checkBulletEnemyCollisions() {
        if (!userShip.isInvincible()) {
            checkBulletCollision();
            checkEnemyCollision();
        }
    }

    public void speedShot(User shooter) {
        SequentialTransition speedShotAnimation = new SequentialTransition();
        double height = shooter.getStack().getPrefHeight();
        addBullet(singleShot(shooter, shooter.getStack().getLayoutX(), shooter.getStack().getLayoutY() - height, userShipBulletImage));
        userShip.getSpaceshipShootAudio().play();
        for (int i = 0; i < 2; i++) {
            PauseTransition bulletPause = new PauseTransition(Duration.seconds(0.25));
            bulletPause.setOnFinished((event) -> {
                addBullet(singleShot(shooter, shooter.getStack().getLayoutX(), shooter.getStack().getLayoutY() - height, userShipBulletImage));
                userShip.getSpaceshipShootAudio().play();
            });
            bulletPause.setCycleCount(1);
            speedShotAnimation.getChildren().add(bulletPause);
        }
        speedShotAnimation.play();
    }

    public void spreadShot() {
        addBullet(Bullet.singleShot(userShip, userShip.getStack().getLayoutX() - userShip.getStack().getWidth() / 2, userShip.getStack().getLayoutY() + 20 - userShip.getStack().getHeight(), userShipBulletImage));
        addBullet(Bullet.singleShot(userShip, userShip.getStack().getLayoutX() + userShip.getStack().getWidth() / 2, userShip.getStack().getLayoutY() + 20 - userShip.getStack().getHeight(), userShipBulletImage));
        addBullet(Bullet.singleShot(userShip, userShip.getStack().getLayoutX(), userShip.getStack().getLayoutY() - userShip.getStack().getHeight(), userShipBulletImage));
        userShip.getSpaceshipShootAudio().play();
    }

    public void switchShoot() {
        if (userShip.isSingleShot()) {
            userShip.setShot(2);
        } else if (userShip.isSpeedShot()) {
            userShip.setShot(3);
        } else {
            userShip.setShot(1);
        }
    }

    public void delaySingleShoot() {
        userShip.setCanShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished((event) -> {
            userShip.setCanShoot(true);
        });
        pause.play();
    }

    public void delaySpeedShoot() {
        userShip.setCanSpeedShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished((event) -> {
            userShip.setCanSpeedShoot(true);
        });
        pause.play();
    }

    public void delaySpreadShoot() {
        userShip.setCanSpreadShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished((event) -> {
            userShip.setCanSpreadShoot(true);
        });
        pause.play();
    }

    public void checkBulletCollision() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (userShip.getStack().getBoundsInParent().intersects(enemyBullets.get(i).getStack().getBoundsInParent())) {
                Bullet.removeBullet(enemyBullets.get(i));
                spaceshipHit();
                enemyBullets.remove(enemyBullets.get(i));
            }
        }
    }

    public void checkEnemyCollision() {
        for (Enemy currentEnemy : EnemiesController.getEnemies()) {
            if (pane.localToScene(EnemiesController.enemiesPane.localToScene(currentEnemy.getStack().getBoundsInParent()))
                    .intersects(userShip.getStack().getBoundsInParent())) {
                spaceshipHit();
            }
        }
    }

    public void spaceshipHit() {
        userShip.setInvincible(true);
        spaceshipHitAnimation();
        userShip.setLives(userShip.getLives() - 1);
        if (userShip.getLives() == 0) {
            userShip.killAnimation(pane);
            animation.stop();
            EnemiesController.enemyAnimation.stop();
            gameOverAudio.play();
            lblGameOver.setVisible(true);
        }
    }

    public void spaceshipHitAnimation() {
        spaceshipHitAudio.play();
        Timeline spaceshipFlashing = new Timeline(new KeyFrame(Duration.seconds(0.1),
                new KeyValue(userShip.getStack().opacityProperty(), 0)
        ));
        spaceshipFlashing.setAutoReverse(true);
        spaceshipFlashing.setCycleCount(8);
        spaceshipFlashing.play();

        Timeline spaceshipInvincible = new Timeline(new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userShip.setInvincible(false);
            }
        })
        );
        spaceshipInvincible.setCycleCount(1);
        spaceshipInvincible.play();
    }

    public void addBullet(Bullet newBullet) {
        userShip.getBullets().add(newBullet);
        EnemiesController.spaceshipBullets.add(newBullet);
    }

    public void move() {
        animation.start();
    }

    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

}
