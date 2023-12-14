/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.models.Bullet;
import edu.vanier.spaceinvaders.models.EnemyShip;
import edu.vanier.spaceinvaders.models.UserShip;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * Controller class for the FXML game UI as well as the UserShip controller
 * which depends on the FXML layout.
 *
 * @author andyhou
 */
public class FXMLUserLevelController {

    @FXML
    ImageView userShipImage;

    @FXML
    Pane mainPane;

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

    UserShip userShip;
    int userSpeed = UserShip.getSPEED();
    ArrayList<Bullet> enemyBullets = EnemyShip.getBullets();
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

    static AudioClip spaceshipHitAudio = new AudioClip(FXMLUserLevelController.class.getResource("/sounds/sfx_shieldDown.wav").toExternalForm());
    static AudioClip gameOverAudio = new AudioClip(FXMLUserLevelController.class.getResource("/sounds/gameOver.wav").toExternalForm());
    static AudioClip winAudio = new AudioClip(FXMLUserLevelController.class.getResource("/sounds/win.wav").toExternalForm());

    static Image singleShotIconImage = new Image("/images/shot_icons/singleShotIconImage.png");
    static Image speedShotIconImage = new Image("/images/shot_icons/speedShotIconImage.png");
    static Image spreadShotIconImage = new Image("/images/shot_icons/spreadShotIconImage.png");
    static Image lockedIconImage = new Image("/images/other/locked.png");
    static Image lifeIconImage = new Image("/images/other/heart.png");

    // Listeners to make sure userShip doesn't go beyond the screen
    ChangeListener<? super Number> horizontalBorderListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double leftWall = 0;
            double rightWall = mainPane.getPrefWidth() - userShip.getObjectImageView().getFitWidth();
            // userShip hits the left wall
            if (userShipImage.getLayoutX() <= leftWall) {
                userShipImage.setLayoutX(leftWall);
                setSpaceshipMechanics(userSpeed, 0, -userSpeed, userSpeed);
            } // userShip hits the right wall
            else if (userShipImage.getLayoutX() >= rightWall) {
                userShipImage.setLayoutX(rightWall);
                setSpaceshipMechanics(0, -userSpeed, -userSpeed, userSpeed);
            } else {
                setSpaceshipMechanics(userSpeed, -userSpeed, -userSpeed, userSpeed);
            }
        }
    };

    ChangeListener<? super Number> verticalBorderListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double topWall = 0;
            double bottomWall = mainPane.getPrefHeight() - userShip.getObjectImageView().getFitHeight();
            // userShip hits the top wall
            if (userShipImage.getLayoutY() <= topWall) {
                userShipImage.setLayoutY(topWall);
                setSpaceshipMechanics(userSpeed, -userSpeed, 0, userSpeed);
            } // userShip hits the bottom wall
            else if (userShipImage.getLayoutY() >= bottomWall) {
                userShipImage.setLayoutY(bottomWall);
                setSpaceshipMechanics(userSpeed, -userSpeed, -userSpeed, 0);
            } else {
                setSpaceshipMechanics(userSpeed, -userSpeed, -userSpeed, userSpeed);
            }
        }
    };

    /**
     * The animation of the user.
     */
    public AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long n) {
            // move the spaceship
            moveSpaceship();

            // move the bullets
            Bullet.moveBullets(userShip.getBullets(), false);

            // Check for collisions.
            checkBulletEnemyCollisions();

            // Check for level completion unless all levels have been completed
            if (!allLevelsComplete) {
                // Check if the level is completed
                checkLevelComplete();

                // Check if the userShip is in the portal at the end of the level.
                checkGameOverPortal();
            } else {
                // Display final score when all levels completed.
                lblCongratulations.setText("Total Score: " + score);
                lblCongratulations.setVisible(true);
                EnemiesController.enemyAnimation.stop();
            }
        }
    };

    // Event for restart button when user dies.
    EventHandler<ActionEvent> btnRestartLevelEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            // Reset the score to 0.
            updateScoreLabel(-score);

            // Reset the initial position of the userShip.
            userShipImage.setLayoutX(520);
            userShipImage.setLayoutY(640);
            userShipImage.setVisible(true);

            // Remove any bullets still on the screen.
            Bullet.removeBullet(UserShip.getBullets());
            Bullet.removeBullet(enemyBullets);

            // Restart the level.
            EnemiesController.clearEnemies();
            animation.start();
            startLevel();
            paneGameOver.setVisible(false);
        }
    };

    /**
     * Initializes UI components from FXML file.
     */
    @FXML
    public void initialize() {
        // Instantiate userShip
        userShip = new UserShip(userShipImage, new Image(randomShipSprite()));

        // Add listeners to the userShip
        userShipImage.layoutXProperty().addListener(horizontalBorderListener);
        userShipImage.layoutYProperty().addListener(verticalBorderListener);

        // Set the background.
        backgroundImage.setImage(new Image("/images/background/starfield_alpha.png"));
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitWidth(mainPane.getPrefWidth());
        backgroundImage.setFitHeight(mainPane.getPrefHeight());

        // Set the image of the portal.
        portal.setFill(new ImagePattern(new Image("/images/other/portal.png")));

        // Set the icon image for the first rocket type.
        singleShotIconImageView.setImage(singleShotIconImage);

        // Set the images for the lives left.
        life1.setImage(lifeIconImage);
        life2.setImage(lifeIconImage);
        life3.setImage(lifeIconImage);

        // Set the main mainPane of the game.
        GameObject.setMainPane(mainPane);

        // EventHandler of restart button.
        btnRestartLevel.setOnAction(btnRestartLevelEvent);

    }

    /**
     * Chooses one of three userShip sprite resource paths.
     *
     * @return Resource path of a random spaceship.
     */
    private String randomShipSprite() {
        int number;
        double random = Math.random();
        if (random <= 0.3333333333) {
            number = 1;
        } else if (random <= 0.6666666666) {
            number = 2;
        } else {
            number = 3;
        }

        return "/images/spaceships/playerShip" + Integer.toString(number)
                + "_blue.png";
    }

    /**
     * Starts the game.
     *
     * @throws InterruptedException
     * @throws FileNotFoundException
     */
    public void startGame() throws InterruptedException, FileNotFoundException {
        EnemiesController enemies_Level_One = new EnemiesController(mainPane, new Image("/images/bullets/laserRed05.png"));
        enemies_Level_One.move();
        setSpaceshipMechanics(userSpeed, -userSpeed, -userSpeed, userSpeed);
        startLevel();

    }

    /**
     * Sets the velocity of the user based on the key pressed as well as the
     * rocket type of the user.
     *
     * @param right
     * @param left
     * @param top
     * @param bottom
     */
    private void setSpaceshipMechanics(int right, int left, int top, int bottom) {
        userShip.getObjectImageView().getScene().setOnKeyPressed((e) -> {
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
                    if (userShip.isCanSingleShoot() && userShip.isSingleShotSelected()) {
                        singleShot();
                        delaySingleShoot();

                    } else if (userShip.isCanSpeedShoot() && userShip.isSpeedShotSelected()) {
                        speedShot();
                        delaySpeedShoot();
                    } else if (userShip.isCanSpreadShoot() && userShip.isSpreadShotSelected()) {
                        spreadShot();
                        delaySpreadShoot();
                    }

                    break;
            }
        });
        userShipImage.getScene().setOnKeyReleased((e) -> {
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
        });
    }

    /**
     * Moves the userShip based on its current velocity, which is determined by
     * the key pressed.
     */
    private void moveSpaceship() {
        userShipImage.setLayoutX(userShip.getObjectImageView().getLayoutX() + userShip.getxVelocity());
        userShipImage.setLayoutY(userShip.getObjectImageView().getLayoutY() + userShip.getyVelocity());
    }

    /**
     * Handles everything to do when the level is complete: show portal,
     * congratulations text, play win audio...
     */
    private void checkLevelComplete() {
        // if level is completed
        if (EnemiesController.getEnemies().isEmpty() && !portalSpawned) {
            // Show congratulations and play audio.
            lblCongratulations.setVisible(true);
            winAudio.play();

            // Remove any remaining enemy bullets from the mainPane and stop enemy animation.
            Bullet.removeBullet(enemyBullets);
            EnemiesController.enemyAnimation.stop();

            // Animation to show the portal fade in.
            FadeTransition portalFade = new FadeTransition(Duration.seconds(0.5), portal);
            portalFade.setFromValue(0);
            portalFade.setByValue(1.0);
            portalFade.setCycleCount(1);
            portalFade.setDelay(Duration.seconds(0.5));
            portalFade.play();
            portalSpawned = true;

            // Next level.
            currentLevel++;
        }
    }

    /**
     * Checks if the user enters the end level portal and plays its animation.
     */
    private void checkGameOverPortal() {
        // The user can only enter the portal when the portal fade animation is complete, ie it has 1.0 opacity.
        if (portal.opacityProperty().get() == 1.0
                && userShipImage.getBoundsInParent().intersects(portal.getBoundsInParent())
                && !portalEntered) {
            // We want to stop this check once the portal is entered.
            portalEntered = true;

            // UserShip cannot move once portal is entered.
            setImmobilize(true);
            lblCongratulations.setVisible(false);

            // Animtion to make the portal disappear after teleporting the user to the next level.
            Timeline portalDisappear = new Timeline(
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.opacityProperty(), 0))
            );
            portalDisappear.setCycleCount(1);

            // Animation to appear the user to the next level.
            Timeline appear = new Timeline(
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().rotateProperty(), 360)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().scaleXProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().scaleYProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.radiusProperty(), portal.getRadius())),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().layoutYProperty(), userShip.getObjectImageView().getLayoutY() + 80)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(portal.layoutYProperty(), userShip.getObjectImageView().getLayoutY() + 80))
            );
            appear.setCycleCount(1);
            appear.setDelay(Duration.seconds(0.75));

            // Animation that spins and sucks in the user in the portal.
            Timeline teleport = new Timeline(
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(userShip.getObjectImageView().rotateProperty(), 720)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().scaleXProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().scaleYProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(portal.radiusProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().layoutXProperty(), portal.getLayoutX() - 0.5 * userShip.getObjectImageView().getFitWidth())),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(userShip.getObjectImageView().layoutYProperty(), portal.getLayoutY() - 0.5 * userShip.getObjectImageView().getFitHeight()))
            );
            teleport.setCycleCount(1);

            // Play the animations in order.
            SequentialTransition transition = new SequentialTransition(teleport, appear, portalDisappear);
            transition.setCycleCount(1);
            transition.play();

            // At the end of the animation, start the next level.
            transition.setOnFinished((event) -> {
                startLevel();
            });
        }
    }

    /**
     * Checks for collisions between enemy bullets and enemy spaceships.
     */
    private void checkBulletEnemyCollisions() {
        // Only check for collisions if the user is not invincible (user is invincible for short period of time after being hit)
        if (!userShip.isInvincible()) {
            checkBulletCollision();
            checkEnemyCollision();
        }
    }

    /**
     * Shoots a single fire rocket type.
     */
    private void singleShot() {
        addBullet(Bullet.singleShotBullet(userShip, userShip.getObjectImageView().getLayoutX(), userShip.getObjectImageView().getLayoutY() - userShip.getObjectImageView().getFitHeight(), userShipSingleBulletImage));
        UserShip.getUserShipSingleShootAudio().play();
    }

    /**
     * Shoots a burst of 3 rockets.
     */
    private void speedShot() {
        SequentialTransition speedShotAnimation = new SequentialTransition();
        double height = userShip.getObjectImageView().getFitHeight();
        addBullet(Bullet.speedShotBullet(userShip, userShip.getObjectImageView().getLayoutX(), userShip.getObjectImageView().getLayoutY() - height, userShipSpeedBulletImage));
        userShip.getUserShipSpeedShootAudio().play();
        for (int i = 0; i < 2; i++) {
            PauseTransition bulletPause = new PauseTransition(Duration.seconds(0.10));
            bulletPause.setOnFinished((event) -> {
                addBullet(Bullet.speedShotBullet(userShip, userShip.getObjectImageView().getLayoutX(), userShip.getObjectImageView().getLayoutY() - height, userShipSpeedBulletImage));
                UserShip.getUserShipSpeedShootAudio().play();
            });
            bulletPause.setCycleCount(1);
            speedShotAnimation.getChildren().add(bulletPause);
        }
        speedShotAnimation.play();
    }

    /**
     * Shoots a spread of 3 rockets.
     */
    private void spreadShot() {
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImageView().getLayoutX() - userShip.getObjectImageView().getFitWidth() / 2, userShip.getObjectImageView().getLayoutY() + 20 - userShip.getObjectImageView().getFitHeight(), userShipSpreadBulletImage));
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImageView().getLayoutX() + userShip.getObjectImageView().getFitWidth() / 2, userShip.getObjectImageView().getLayoutY() + 20 - userShip.getObjectImageView().getFitHeight(), userShipSpreadBulletImage));
        addBullet(Bullet.spreadShotBullet(userShip, userShip.getObjectImageView().getLayoutX(), userShip.getObjectImageView().getLayoutY() - userShip.getObjectImageView().getFitHeight(), userShipSpreadBulletImage));
        userShip.getUserShipSpreadShootAudio().play();
    }

    /**
     * Switches between the rocket types.
     */
    private void switchShoot() {
        if (userShip.isSingleShotSelected() && speedShotUnlocked) {
            userShip.setShot(2);
        } else if (userShip.isSpeedShotSelected() && spreadShotUnlocked) {
            userShip.setShot(3);
        } else {
            userShip.setShot(1);
        }
        updateShotIconImages();
    }

    /**
     * Delays the next time another single shot can be fired by 0.2 seconds.
     */
    private void delaySingleShoot() {
        userShip.setCanSingleShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished((event) -> {
            userShip.setCanSingleShoot(true);
        });
        pause.play();
    }

    /**
     * Delays the next time another speed shot can be fired by 0.5 seconds.
     */
    private void delaySpeedShoot() {
        userShip.setCanSpeedShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished((event) -> {
            userShip.setCanSpeedShoot(true);
        });
        pause.play();
    }

    /**
     * Delays the next time another spread shot can be fired by 0.75 seconds.
     *
     */
    private void delaySpreadShoot() {
        userShip.setCanSpreadShoot(false);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
        pause.setOnFinished((event) -> {
            userShip.setCanSpreadShoot(true);
        });
        pause.play();
    }

    /**
     * Checks for collision between the user and any enemy bullet.
     */
    private void checkBulletCollision() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (userShipImage.getBoundsInParent().intersects(enemyBullets.get(i).getObjectImageView().getBoundsInParent())) {
                // Remove the bullet from the mainPane and the list of enemyBullets if user is hit.
                Bullet.removeBullet(enemyBullets.get(i));
                enemyBullets.remove(enemyBullets.get(i));

                spaceshipHit();
            }
        }
    }

    /**
     * Checks for collision between the user and any enemy ship.
     */
    private void checkEnemyCollision() {
        for (EnemyShip currentEnemy : EnemiesController.getEnemies()) {
            // Turns the bounds of the enemiesPane to the bounds of the main mainPane.
            // Turns the bounds of the enemy to the bounds of the enemiesPane.
            if (mainPane.localToScene(EnemiesController.enemiesPane.localToScene(currentEnemy.getObjectImageView().getBoundsInParent()))
                    .intersects(userShipImage.getBoundsInParent())) {
                spaceshipHit();
            }
            // If enemy reaches the very bottom, the user loses.
            if (currentEnemy.getObjectImageView().getLayoutY() + EnemiesController.enemiesPane.getLayoutY() >= mainPane.getPrefHeight()) {
                UserShip.setLives(0);
                updateLifeImage();
                checkLivesRemaining();
            }
        }
    }

    /**
     * Handles everything to do when the user is hit: remove a life, diminish
     * score, make user invincible for short time...
     */
    private void spaceshipHit() {
        // Update user lives.
        userShip.setLives(userShip.getLives() - 1);

        // Diminish score.
        updateScoreLabel(-20);

        // UserShip will be invincible until the hit animation is finished.
        userShip.setInvincible(true);
        spaceshipHitAnimation();

        // Updates the visual lives left and check if there are any left.
        updateLifeImage();
        checkLivesRemaining();
    }

    /**
     * Updates the visual images of the lives left.
     */
    private void updateLifeImage() {
        life3.setVisible(UserShip.getLives() == 3);
        life2.setVisible(UserShip.getLives() >= 2);
        life1.setVisible(UserShip.getLives() >= 1);

    }

    /**
     * Checks if the user is dead and handles the death case: play death
     * animation, game over audio...
     */
    private void checkLivesRemaining() {
        if (userShip.getLives() == 0) {
            // Plays the kill animation.
            userShip.killAnimation(mainPane);

            // Stops the animation of the user and enemies.
            animation.stop();
            EnemiesController.enemyAnimation.stop();

            // Game over audio and text.
            gameOverAudio.play();
            paneGameOver.setVisible(true);
        }
    }

    /**
     * Plays the hit animation.
     */
    private void spaceshipHitAnimation() {
        spaceshipHitAudio.play();

        // The spaceship will flash for 0.1 seconds.
        Timeline spaceshipFlashing = new Timeline(new KeyFrame(Duration.seconds(0.1),
                new KeyValue(userShip.getObjectImageView().opacityProperty(), 0)
        ));
        spaceshipFlashing.setAutoReverse(true);
        spaceshipFlashing.setCycleCount(8);
        spaceshipFlashing.play();

        // The spaceship will be invincible for 1.1 seconds, or 1 second after the flashing is played.
        PauseTransition spaceshipInvincible = new PauseTransition(Duration.seconds(1.1));
        spaceshipInvincible.setOnFinished((event) -> {
            userShip.setInvincible(false);
        });
        spaceshipInvincible.setCycleCount(1);
        spaceshipInvincible.play();
    }

    /**
     * Adds a new bullet to the list of user bullets.
     *
     * @param newBullet
     */
    private void addBullet(Bullet newBullet) {
        userShip.getBullets().add(newBullet);
    }

    /**
     * Method to make the user immobile or not, immobile meaning not being able
     * to move or shoot.
     */
    private void setImmobilize(boolean immobilize) {
        if (immobilize) {
            setSpaceshipMechanics(0, 0, 0, 0);
            userShip.setCanSingleShoot(false);
            userShip.setCanSpeedShoot(false);
            userShip.setCanSpreadShoot(false);
            userSpeed = 0;
            userShip.setxVelocity(0);
            userShip.setyVelocity(0);
            userShip.setCanSingleShoot(false);
        } else {
            userSpeed = UserShip.getSPEED();
            checkAvailableShots();
            userShip.setCanSingleShoot(true);
            userShip.setCanSpeedShoot(speedShotUnlocked);
            userShip.setCanSpreadShoot(spreadShotUnlocked);
        }
    }

    /**
     * Updates the score label.
     *
     * @param scoreIncrement
     */
    public void updateScoreLabel(int scoreIncrement) {
        score += scoreIncrement;
        if (score < 0) { // No negative score.
            score = 0;
        }
        lblScore.setText("Score: " + FXMLUserLevelController.score);
    }

    /**
     * Updates the level label.
     */
    public void updateLevelLabel() {
        lblLevel.setText("Level " + currentLevel);
        if (currentLevel > 3) {
            lblLevel.setText("COMPLETED");
        }
    }

    /**
     * Checks if the new rocket shots are unlocked.
     */
    private static void checkAvailableShots() {
        speedShotUnlocked = (currentLevel > 1);
        spreadShotUnlocked = (currentLevel > 2);
    }

    /**
     * Starts the level.
     */
    private void startLevel() {
        // Bring the portal back to original position.
        portal.setLayoutX(540);
        portal.setLayoutY(470);
        portalSpawned = false;
        portalEntered = false;

        // Reset lives to 3.
        UserShip.setLives(3);
        updateLifeImage();
        updateLevelLabel();
        checkAvailableShots();

        // At level 1, there are 15 enemies and they move at 0.8 speed.
        if (currentLevel == 1) {
            EnemiesController.spawn(15);
            EnemyShip.setSpeed(0.8);
            animation.start();
        } // At level 2, there are 20 enemies and they move at 1.2 speed.
        else if (currentLevel == 2) {
            EnemiesController.spawn(20);
            EnemyShip.setSpeed(1.2);
        } // At level 3, there are 25 enemies and they move at 1.6 speed.
        else if (currentLevel == 3) {
            EnemiesController.spawn(25);
            EnemyShip.setSpeed(1.6);
        } // All levels are completed.
        else {
            allLevelsComplete = true;
        }

        // Move the enemies and allow user to move.
        EnemiesController.enemyAnimation.start();
        setImmobilize(false);
        setSpaceshipMechanics(userSpeed, -userSpeed, -userSpeed, userSpeed);
        updateShotIconImages();
    }

    /**
     * Updates the shot icons to show which one is selected and which ones are
     * available.
     */
    private void updateShotIconImages() {

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
        // Selected rocket type will have 1.0 opacity, others will have 0.6
        if (userShip.isSingleShotSelected()) {
            singleShotIconImageView.setOpacity(1.0);
            speedShotIconImageView.setOpacity(0.6);
            spreadShotIconImageView.setOpacity(0.6);
        } else if (userShip.isSpeedShotSelected()) {
            singleShotIconImageView.setOpacity(0.6);
            speedShotIconImageView.setOpacity(1.0);
            spreadShotIconImageView.setOpacity(0.6);
        } else if (userShip.isSpreadShotSelected()) {
            singleShotIconImageView.setOpacity(0.6);
            speedShotIconImageView.setOpacity(0.6);
            spreadShotIconImageView.setOpacity(1.0);
        }

    }

}
