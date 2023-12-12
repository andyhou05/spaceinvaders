/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.models.Bullet;
import edu.vanier.spaceinvaders.models.Enemy;
import edu.vanier.spaceinvaders.models.User;
import edu.vanier.spaceinvaders.models.GameObject;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.E;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class UserLevelController {

    @FXML
    ImageView userShipImage;

    @FXML
    Pane pane;

    @FXML
    ImageView backgroundImage;

    @FXML
    Label lblCongratulations;

    @FXML
    Circle portal;

    @FXML
    Label lblScore;

    @FXML
    Label lblLevel;

    @FXML
    ImageView life1;

    @FXML
    ImageView life2;

    @FXML
    ImageView life3;

    @FXML
    ImageView singleShotIconImageView;

    @FXML
    ImageView speedShotIconImageView;

    @FXML
    ImageView spreadShotIconImageView;

    @FXML
    Pane paneGameOver;

    @FXML
    Button btnRestartLevel;

    User userShip;
    int userSpeed = User.getSpeed();
    ArrayList<Bullet> enemyBullets = Enemy.getBullets();
    static int currentLevel = 1;
    static int score = 0;

    static boolean speedShotUnlocked = false;
    static boolean spreadShotUnlocked = false;
    static boolean portalSpawned = false;
    static boolean portalEntered = false;
    static boolean allLevelsComplete = false;

    Image userShipSingleBulletImage = new Image("/images/bullets/laserBlue05.png");
    Image userShipSpeedBulletImage = new Image("/images/bullets/laserBlue01.png");
    Image userShipSpreadBulletImage = new Image("images/bullets/laserBlue10.png");

    static AudioClip spaceshipHitAudio = new AudioClip(UserLevelController.class.getResource("/sounds/sfx_shieldDown.wav").toExternalForm());
    static AudioClip gameOverAudio = new AudioClip(UserLevelController.class.getResource("/sounds/gameOver.wav").toExternalForm());
    static AudioClip winAudio = new AudioClip(UserLevelController.class.getResource("/sounds/win.wav").toExternalForm());

    static Image speedShotIconImage = new Image("/images/speedShotImage.png");
    static Image spreadShotIconImage = new Image("/images/spreadShotImage.png");
    static Image lockedIconImage = new Image("/images/locked.png");
    static Image lifeIconImage = new Image("/images/heart.png");

    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            checkWallCollision(); // this also updates the userSpeed of userShip if wall is encountered, ex: left wall encountered -> pressing "A" won't do anything.
            moveSpaceship();
            // move the bullets
            Bullet.moveBullets(userShip.getBullets(), false);
            // Check for collisions.
            checkBulletEnemyCollisions();
            // Check for level completion unless all levels have been completed
            if (!allLevelsComplete) {
                // Check if the game is over
                checkLevelComplete();
                // Check if the userShip is in the portal at the end of the level.
                checkGameOverPortal();
            } else {
                lblCongratulations.setText("Total Score: " + score);
                lblCongratulations.setVisible(true);
                EnemiesController.enemyAnimation.stop();
            }
        }
    };

    EventHandler<ActionEvent> btnRestrtLevelEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            updateScoreLabel(-score);
            userShipImage.setLayoutX(520);
            userShipImage.setLayoutY(640);
            userShipImage.setVisible(true);
            animation.start();
            EnemiesController.clearEnemies();
            startLevel();
            paneGameOver.setVisible(false);
        }
    };

    @FXML
    public void initialize() {
        userShip = new User(userShipImage, new Image("/images/spaceships/playerShip2_blue.png"));

        backgroundImage.setImage(new Image("/images/background/starfield_alpha.png"));
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitWidth(pane.getPrefWidth());
        backgroundImage.setFitHeight(pane.getPrefHeight());

        portal.setFill(new ImagePattern(new Image("/images/portal.png")));

        singleShotIconImageView.setImage(new Image("/images/singleShotImage.png"));

        life1.setImage(lifeIconImage);
        life2.setImage(lifeIconImage);
        life3.setImage(lifeIconImage);

        GameObject.setPane(pane);
        btnRestartLevel.setOnAction(btnRestrtLevelEvent);

    }

    public void startGame() throws InterruptedException, FileNotFoundException {
        EnemiesController enemies_Level_One = new EnemiesController(pane, new Image("/images/bullets/laserRed05.png"), 0.8);
        enemies_Level_One.move();

        startLevel();

    }

    private void checkWallCollision() {
        // Border detection, if we reach a border, set the left/right userSpeed to 0.
        double leftWall = 0;
        double rightWall = pane.getPrefWidth() - userShip.getObjectImage().getFitWidth();
        double topWall = 0;
        double bottomWall = pane.getPrefHeight() - userShip.getObjectImage().getFitHeight();
        int currentLeft = -userSpeed;
        int currentRight = userSpeed;
        int currentTop = -userSpeed;
        int currentBottom = userSpeed;

        // userShip hits the left wall
        if (userShip.getObjectImage().getLayoutX() <= leftWall) {
            userShip.getObjectImage().setLayoutX(leftWall);
            currentLeft = 0;
            currentRight = userSpeed;
        } // userShip hits the right wall
        else if (userShip.getObjectImage().getLayoutX() >= rightWall) {
            userShip.getObjectImage().setLayoutX(rightWall);
            currentRight = 0;
            currentLeft = -userSpeed;
        }
        // userShip hits the top wall
        if (userShip.getObjectImage().getLayoutY() <= topWall) {
            userShip.getObjectImage().setLayoutY(topWall);
            currentTop = 0;
            currentBottom = userSpeed;
        } // userShip hits the bottom wall
        else if (userShip.getObjectImage().getLayoutY() >= bottomWall) {
            userShip.getObjectImage().setLayoutY(bottomWall);
            currentBottom = 0;
            currentTop = -userSpeed;

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
                        singleShot();
                        delaySingleShoot();

                    } else if (userShip.isCanSpeedShoot() && userShip.isSpeedShot()) {
                        speedShot();
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
            currentLevel++;
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
                startLevel();
            });
        }
    }

    private void checkBulletEnemyCollisions() {
        if (!userShip.isInvincible()) {
            checkBulletCollision();
            checkEnemyCollision();
        }
    }

    public void singleShot() {
        addBullet(Bullet.singleShotBullet(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - userShip.getObjectImage().getFitHeight(), userShipSingleBulletImage));
        User.getUserShipSingleShootAudio().play();
    }

    public void speedShot() {
        SequentialTransition speedShotAnimation = new SequentialTransition();
        double height = userShip.getObjectImage().getFitHeight();
        addBullet(Bullet.speedShotBullet(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - height, userShipSpeedBulletImage));
        userShip.getUserShipSpeedShootAudio().play();
        for (int i = 0; i < 2; i++) {
            PauseTransition bulletPause = new PauseTransition(Duration.seconds(0.10));
            bulletPause.setOnFinished((event) -> {
                addBullet(Bullet.speedShotBullet(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - height, userShipSpeedBulletImage));
                User.getUserShipSpeedShootAudio().play();
            });
            bulletPause.setCycleCount(1);
            speedShotAnimation.getChildren().add(bulletPause);
        }
        speedShotAnimation.play();
    }

    public void spreadShot() {
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImage().getLayoutX() - userShip.getObjectImage().getFitWidth() / 2, userShip.getObjectImage().getLayoutY() + 20 - userShip.getObjectImage().getFitHeight(), userShipSpreadBulletImage));
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImage().getLayoutX() + userShip.getObjectImage().getFitWidth() / 2, userShip.getObjectImage().getLayoutY() + 20 - userShip.getObjectImage().getFitHeight(), userShipSpreadBulletImage));
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImage().getLayoutX(), userShip.getObjectImage().getLayoutY() - userShip.getObjectImage().getFitHeight(), userShipSpreadBulletImage));
        userShip.getUserShipSpreadShootAudio().play();
    }

    public void switchShoot() {
        if (userShip.isSingleShot() && speedShotUnlocked) {
            userShip.setShot(2);
        } else if (userShip.isSpeedShot() && spreadShotUnlocked) {
            userShip.setShot(3);
        } else {
            userShip.setShot(1);
        }
        updateShotIconImages();
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
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            userShip.setCanSpeedShoot(true);
        });
        pause.play();
    }

    public void delaySpreadShoot() {
        userShip.setCanSpreadShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
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
            if (currentEnemy.getObjectImage().getLayoutY() + EnemiesController.enemiesPane.getLayoutY() >= pane.getPrefHeight()) {
                User.setLives(0);
                checkLivesRemaining();
            }
        }
    }

    public void spaceshipHit() {
        userShip.setLives(userShip.getLives() - 1);
        updateScoreLabel(-20);
        userShip.setInvincible(true);
        spaceshipHitAnimation();
        updateLifeImage();
        checkLivesRemaining();
    }

    public void updateLifeImage() {
        life3.setVisible(User.getLives() == 3);
        life2.setVisible(User.getLives() >= 2);
        life1.setVisible(User.getLives() >= 1);

    }

    public void checkLivesRemaining() {
        if (userShip.getLives() == 0) {
            userShip.killAnimation(pane);
            animation.stop();
            setImmobilize(true);
            EnemiesController.enemyAnimation.stop();
            gameOverAudio.play();
            paneGameOver.setVisible(true);
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

    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    private void setImmobilize(boolean immobilize) {
        if (immobilize) {
            setSpaceshipMechanics(0, 0, 0, 0);
            userSpeed = 0;
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
            userShip.setCanShoot(false);
        } else {
            userSpeed = User.getSpeed();
            userShip.setCanShoot(true);
        }
    }

    public void updateScoreLabel(int scoreIncrement) {
        score += scoreIncrement;
        if (score < 0) {
            score = 0;
        }
        lblScore.setText("Score: " + UserLevelController.score);
    }

    public void updateLevelLabel() {
        lblLevel.setText("Level " + currentLevel);
        if (currentLevel > 3) {
            lblLevel.setText("COMPLETED");
        }
    }

    public static void checkAvailableShots() {
        speedShotUnlocked = (currentLevel > 1);
        spreadShotUnlocked = (currentLevel > 2);
    }

    private void startLevel() {
        // bring the portal back to original position
        portal.setLayoutX(540);
        portal.setLayoutY(470);
        portalSpawned = false;
        portalEntered = false;
        User.setLives(3);
        updateLifeImage();
        updateLevelLabel();
        checkAvailableShots();
        if (currentLevel == 1) {
            EnemiesController.spawn(15);
            animation.start();
        } else if (currentLevel == 2) {
            EnemiesController.spawn(20);
            Enemy.setSpeed(1.2);
        } else if (currentLevel == 3) {
            EnemiesController.spawn(25);
            Enemy.setSpeed(1.6);
        } else {
            allLevelsComplete = true;
        }
        EnemiesController.enemyAnimation.start();
        setImmobilize(false);
        updateShotIconImages();
    }

    public void updateShotIconImages() {

        if (speedShotUnlocked) {
            speedShotIconImageView.setImage(speedShotIconImage);
        } else {
            speedShotIconImageView.setImage(lockedIconImage);
        }
        if (spreadShotUnlocked) {
            spreadShotIconImageView.setImage(spreadShotIconImage);
        } else {
            spreadShotIconImageView.setImage(lockedIconImage);
        }
        if (userShip.isSingleShot()) {
            singleShotIconImageView.setOpacity(1.0);
            speedShotIconImageView.setOpacity(0.6);
            spreadShotIconImageView.setOpacity(0.6);
        } else if (userShip.isSpeedShot()) {
            singleShotIconImageView.setOpacity(0.6);
            speedShotIconImageView.setOpacity(1.0);
            spreadShotIconImageView.setOpacity(0.6);
        } else if (userShip.isSpreadShot()) {
            singleShotIconImageView.setOpacity(0.6);
            speedShotIconImageView.setOpacity(0.6);
            spreadShotIconImageView.setOpacity(1.0);
        }

    }

    public Pane getPane() {
        return pane;
    }

}
