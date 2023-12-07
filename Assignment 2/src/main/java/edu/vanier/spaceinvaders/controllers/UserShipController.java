/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvadersmodels.Bullet;
import static edu.vanier.spaceinvadersmodels.Bullet.singleShot;
import edu.vanier.spaceinvadersmodels.Enemy;
import edu.vanier.spaceinvadersmodels.User;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class UserShipController {

    ArrayList<Bullet> enemyBullets = Enemy.getBullets();
    static Pane pane;
    static User userShip;
    static Label lblGameOver;
    Image userShipBulletImage;
    int speed = User.getSpeed();
    AudioClip spaceshipHitAudio = new AudioClip(getClass().getResource("/sounds/sfx_shieldDown.wav").toExternalForm());
    static AudioClip gameOverAudio = new AudioClip(UserShipController.class.getResource("/sounds/gameOver.wav").toExternalForm());
    static Label lblCongratulations;
    static AudioClip winAudio = new AudioClip(UserShipController.class.getResource("/sounds/win.wav").toExternalForm());
    static Circle portal;
    static boolean portalSpawned = false;
    static boolean portalEntered = false;
    static int currentLevel;

    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            checkWallCollision(); // this also updates the speed of userShip if wall is encountered, ex: left wall encountered -> pressing "A" won't do anything.
            moveSpaceship();
            // move the bullets
            Bullet.moveBullets(userShip.getBullets(), false);
            // Check for collisions.
            checkBulletEnemyCollisions();
            // Check if the game is over
            checkLevelComplete();
            // Check if the userShip is in the portal at the end of the level.
            checkGameOverPortal();
        }
    };

    public UserShipController(User userShip, Image spaceshipBulletImage, Pane pane, Label lblGameOver, Label lblCongratulations, Circle portal) {
        this.userShip = userShip;
        this.userShipBulletImage = spaceshipBulletImage;
        UserShipController.pane = pane;
        UserShipController.lblGameOver = lblGameOver;
        UserShipController.lblCongratulations = lblCongratulations;
        UserShipController.portal = portal;
        currentLevel = 1;
    }

    private void checkWallCollision() {
        // Border detection, if we reach a border, set the left/right speed to 0.
        double leftWall = 0;
        double rightWall = pane.getPrefWidth() - userShip.getObjectImage().getFitWidth();
        double topWall = 0;
        double bottomWall = pane.getPrefHeight() - userShip.getObjectImage().getFitHeight();
        int currentLeft = -speed;
        int currentRight = speed;
        int currentTop = -speed;
        int currentBottom = speed;

        // userShip hits the left wall
        if (userShip.getObjectImage().getLayoutX() <= leftWall) {
            userShip.getObjectImage().setLayoutX(leftWall);
            currentLeft = 0;
            currentRight = speed;
        } // userShip hits the right wall
        else if (userShip.getObjectImage().getLayoutX() >= rightWall) {
            userShip.getObjectImage().setLayoutX(rightWall);
            currentRight = 0;
            currentLeft = -speed;
        }
        // userShip hits the top wall
        if (userShip.getObjectImage().getLayoutY() <= topWall) {
            userShip.getObjectImage().setLayoutY(topWall);
            currentTop = 0;
            currentBottom = speed;
        } // userShip hits the bottom wall
        else if (userShip.getObjectImage().getLayoutY() >= bottomWall) {
            userShip.getObjectImage().setLayoutY(bottomWall);
            currentBottom = 0;
            currentTop = -speed;

        }
        setSpaceshipMechanics(currentRight, currentLeft, currentTop, currentBottom);
    }

    public void setSpaceshipMechanics(int right, int left, int top, int bottom) {
        userShip.getObjectImage().getScene().setOnKeyPressed((e) -> {
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
                        addBullet(Bullet.singleShot(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - userShip.getObjectImage().getFitHeight(), userShipBulletImage));
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
        userShip.getObjectImage().getScene().setOnKeyReleased((e) -> {
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
        });
    }

    private void moveSpaceship() {
        userShip.getObjectImage().setLayoutX(userShip.getObjectImage().getLayoutX() + userShip.getxVelocity());
        userShip.getObjectImage().setLayoutY(userShip.getObjectImage().getLayoutY() + userShip.getyVelocity());
    }

    private void checkLevelComplete() {
        // if level is completed
        if (EnemiesController.getEnemies().isEmpty() && !portalSpawned) {
            lblCongratulations.setVisible(true);
            winAudio.play();
            Bullet.removeBullet(enemyBullets);
            EnemiesController.enemyAnimation.stop();
            FadeTransition portalFade = new FadeTransition(Duration.seconds(0.5), portal);
            portalFade.setFromValue(0);
            portalFade.setByValue(1.0);
            portalFade.setCycleCount(1);
            portalFade.setDelay(Duration.seconds(0.5));
            portalFade.play();
            portalSpawned = true;
        }
    }

    private void checkGameOverPortal() {
        if (portal.opacityProperty().get() == 1.0
                && userShip.getObjectImage().getBoundsInParent().intersects(portal.getBoundsInParent())
                && !portalEntered) {
            portalEntered = true;
            setImmobilize(true);
            lblCongratulations.setVisible(false);

            Timeline portalDisappear = new Timeline(
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.opacityProperty(), 0))
            );
            portalDisappear.setCycleCount(1);
            Timeline appear = new Timeline(
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().rotateProperty(), 360)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().scaleXProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().scaleYProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.radiusProperty(), portal.getRadius())),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().layoutYProperty(), userShip.getObjectImage().getLayoutY() + 80)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.layoutYProperty(), userShip.getObjectImage().getLayoutY() + 80))
            );
            appear.setCycleCount(1);
            appear.setDelay(Duration.seconds(0.75));
            Timeline teleport = new Timeline(
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(userShip.getObjectImage().rotateProperty(), 720)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().scaleXProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().scaleYProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(portal.radiusProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().layoutXProperty(), portal.getLayoutX() - 0.5 * userShip.getObjectImage().getFitWidth())),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImage().layoutYProperty(), portal.getLayoutY() - 0.5 * userShip.getObjectImage().getFitHeight()))
            );
            teleport.setCycleCount(1);

            SequentialTransition transition = new SequentialTransition(teleport, appear, portalDisappear);
            transition.setCycleCount(1);
            transition.play();
            transition.setOnFinished((event) -> {
                startNextLevel();
            });
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
        double height = shooter.getObjectImage().getFitHeight();
        addBullet(singleShot(shooter, shooter.getObjectImage().getLayoutX(), shooter.getObjectImage().getLayoutY() - height, userShipBulletImage));
        userShip.getSpaceshipShootAudio().play();
        for (int i = 0; i < 2; i++) {
            PauseTransition bulletPause = new PauseTransition(Duration.seconds(0.25));
            bulletPause.setOnFinished((event) -> {
                addBullet(singleShot(shooter, shooter.getObjectImage().getLayoutX(), shooter.getObjectImage().getLayoutY() - height, userShipBulletImage));
                userShip.getSpaceshipShootAudio().play();
            });
            bulletPause.setCycleCount(1);
            speedShotAnimation.getChildren().add(bulletPause);
        }
        speedShotAnimation.play();
    }

    public void spreadShot() {
        addBullet(Bullet.singleShot(userShip, userShip.getObjectImage().getLayoutX() - userShip.getObjectImage().getFitWidth() / 2, userShip.getObjectImage().getLayoutY() + 20 - userShip.getObjectImage().getFitHeight(), userShipBulletImage));
        addBullet(Bullet.singleShot(userShip, userShip.getObjectImage().getLayoutX() + userShip.getObjectImage().getFitWidth() / 2, userShip.getObjectImage().getLayoutY() + 20 - userShip.getObjectImage().getFitHeight(), userShipBulletImage));
        addBullet(Bullet.singleShot(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - userShip.getObjectImage().getFitHeight(), userShipBulletImage));
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
            if (userShip.getObjectImage().getBoundsInParent().intersects(enemyBullets.get(i).getObjectImage().getBoundsInParent())) {
                Bullet.removeBullet(enemyBullets.get(i));
                spaceshipHit();
                enemyBullets.remove(enemyBullets.get(i));
            }
        }
    }

    public void checkEnemyCollision() {
        for (Enemy currentEnemy : EnemiesController.getEnemies()) {
            if (pane.localToScene(EnemiesController.enemiesPane.localToScene(currentEnemy.getObjectImage().getBoundsInParent()))
                    .intersects(userShip.getObjectImage().getBoundsInParent())) {
                spaceshipHit();
            }
            if(currentEnemy.getObjectImage().getLayoutY() + EnemiesController.enemiesPane.getLayoutY() >= pane.getPrefHeight()){
                User.setLives(0);
                checkLivesRemaining();
            }
        }
    }

    public void spaceshipHit() {
        userShip.setInvincible(true);
        spaceshipHitAnimation();
        userShip.setLives(userShip.getLives() - 1);
        checkLivesRemaining();
    }
    
    public void checkLivesRemaining(){
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
                new KeyValue(userShip.getObjectImage().opacityProperty(), 0)
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
    }

    public void move() {
        animation.start();
    }

    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    private void setImmobilize(boolean immobilize) {
        if (immobilize) {
            setSpaceshipMechanics(0, 0, 0, 0);
            speed = 0;
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
            userShip.setCanShoot(false);
        }else{
            speed = User.getSpeed();
            userShip.setCanShoot(true);
        }
    }

    private void startNextLevel() {
        currentLevel++;
        portalSpawned = false;
        portalEntered = false;
        User.setLives(3);
        EnemiesController.enemyAnimation.start();
        if (currentLevel == 2) {
            EnemiesController.spawn(20);
            Enemy.setSpeed(1);
        } else if (currentLevel == 3) {
            EnemiesController.spawn(25);
            Enemy.setSpeed(1.2);
        }
        setImmobilize(false);
    }

}