/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Bullet;
import edu.vanier.models.Enemy;
import edu.vanier.models.Spaceship;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class EnemiesController {

    static ArrayList<Bullet> spaceshipBullets = new ArrayList<>();
    static Pane pane;
    static Pane enemiesPane = new Pane();
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Image enemyBulletImage;
    static double enemyXdistance = 185;
    static double enemyYdistance = 100;
    static Label lblCongratulations;
    static AudioClip winAudio = new AudioClip(SpaceshipController.class.getResource("/sounds/win.wav").toExternalForm());
    static Circle portal;

    static AnimationTimer enemyAnimation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            moveEnemies();
            enemiesShoot();
            // Check for collisions.
            checkBulletCollision();
        }
    };

    public EnemiesController(Pane pane, Image enemyBulletImage, Label lblCongratulations, Circle portal, double enemySpeed) {
        enemiesPane.setPrefWidth(800);
        enemiesPane.setPrefHeight(400);
        this.pane = pane;
        this.enemyBulletImage = enemyBulletImage;
        EnemiesController.lblCongratulations = lblCongratulations;
        EnemiesController.portal = portal;
        Enemy.setSpeed(enemySpeed);
    }

    public void spawn(int enemyNumber) {
        double currentLayoutX;
        double currentLayoutY = 0;
        for (int i = 0; enemies.size() < enemyNumber; i++) {
            currentLayoutX = enemyXdistance * i;
            if (currentLayoutX + enemyXdistance > pane.getPrefWidth()) {
                currentLayoutY += enemyYdistance;
                currentLayoutX = 0;
                i = 0;
            }
            Enemy currentEnemy = new Enemy(currentLayoutX, currentLayoutY);
            enemiesPane.getChildren().add(currentEnemy.getStack());
            enemies.add(currentEnemy);
        }
        pane.getChildren().add(enemiesPane);
    }

    private static void moveEnemies() {
        double y = enemyYdistance;
        if ((enemiesPane.getLayoutY() / y) % 2 == 0) {
            Enemy.setVelocity(Enemy.getSpeed());
        } else {
            Enemy.setVelocity(-Enemy.getSpeed());
        }

        enemiesPane.setLayoutX(enemiesPane.getLayoutX() + Enemy.getVelocity());
        if (enemiesPane.getLayoutX() + enemiesPane.getWidth() >= pane.getWidth() || enemiesPane.getLayoutX() <= 0) {
            enemiesPane.setLayoutY(enemiesPane.getLayoutY() + y);
        }
    }

    private static void enemiesShoot() {
        // Make enemies able to singleShot.
        for (Enemy currentEnemy : enemies) {
            if (Math.random() < 0.0015) {
                Bullet currentBullet = Bullet.singleShot(currentEnemy,
                        currentEnemy.getStack().getLayoutX() + enemiesPane.getLayoutX(),
                        currentEnemy.getStack().getLayoutY() + enemiesPane.getLayoutY() + currentEnemy.getStack().getHeight(),
                        enemyBulletImage
                );
                Enemy.getBullets().add(currentBullet);
                currentEnemy.getEnemyShootAudio().play();
            }
        }
        Bullet.moveBullets(Enemy.getBullets(), true);
    }

    public void move() throws InterruptedException {
        enemyAnimation.start();
    }

    public static void checkBulletCollision() {
        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long n) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy currentEnemy = enemies.get(i);
                    for (Bullet b : spaceshipBullets) {
                        // turns the local bounds of enemiesPane to the bounds of the pane,
                        // turns the local bounds of the enemy to the bounds of the enemiesPane.
                        if (pane.localToScene(enemiesPane.localToScene(currentEnemy.getStack().getBoundsInParent())).intersects(b.getStack().getBoundsInParent())) {
                            // kill the space invader
                            currentEnemy.killAnimation(enemiesPane);
                            enemies.remove(enemies.get(i));
                            Bullet.removeBullet(b);
                            spaceshipBullets.remove(b);
                            // if game is over
                            if (enemies.isEmpty()) {
                                lblCongratulations.setVisible(true);
                                winAudio.play();
                                FadeTransition portalFade = new FadeTransition(Duration.seconds(0.5), portal);
                                portalFade.setFromValue(0);
                                portalFade.setByValue(1.0);
                                portalFade.setCycleCount(1);
                                portalFade.setDelay(Duration.seconds(0.5));
                                portalFade.play();
                            }
                            break;
                        }
                    }

                }
            }
        };
        animation.start();
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
