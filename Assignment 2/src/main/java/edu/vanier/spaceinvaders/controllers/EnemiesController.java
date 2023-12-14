/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.main.MainApp;
import edu.vanier.spaceinvaders.models.Bullet;
import edu.vanier.spaceinvaders.models.Enemy;
import edu.vanier.spaceinvaders.models.User;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 * @author andyhou
 */
public class EnemiesController {

    static ArrayList<Bullet> spaceshipBullets;
    static Pane pane;
    static Pane enemiesPane = new Pane();
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Image enemyBulletImage;
    static double enemyXdistance = 185;
    static double enemyYdistance = 100;

    public static AnimationTimer enemyAnimation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            moveEnemies();
            enemiesShoot();
            // Check for collisions.
            checkBulletCollision();
        }
    };

    public EnemiesController(Pane pane, Image enemyBulletImage) {
        enemiesPane.setPrefWidth(800);
        enemiesPane.setPrefHeight(400);
        spaceshipBullets = User.getBullets();
        this.pane = pane;
        this.enemyBulletImage = enemyBulletImage;
    }

    public static void spawn(int enemyNumber) {
        Pane newEnemiesPane = new Pane();
        newEnemiesPane.setPrefWidth(800);
        newEnemiesPane.setPrefHeight(400);
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
            newEnemiesPane.getChildren().add(currentEnemy.getObjectImage());
            enemies.add(currentEnemy);
        }
        newEnemiesPane.setLayoutX(0);
        newEnemiesPane.setLayoutY(0);
        pane.getChildren().add(newEnemiesPane);
        enemiesPane = newEnemiesPane;
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
        // Make enemies able to singleShotBullet.
        for (Enemy currentEnemy : enemies) {
            if (Math.random() < 0.0005) {
                Bullet currentBullet = Bullet.singleShotBullet(currentEnemy,
                        currentEnemy.getObjectImage().getLayoutX() + enemiesPane.getLayoutX(),
                        currentEnemy.getObjectImage().getLayoutY() + enemiesPane.getLayoutY() + currentEnemy.getObjectImage().getFitHeight(),
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
        for (int i = 0; i < enemies.size(); i++) {
            Enemy currentEnemy = enemies.get(i);
            for (Bullet b : spaceshipBullets) {
                // turns the local bounds of enemiesPane to the bounds of the pane,
                // turns the local bounds of the enemy to the bounds of the enemiesPane.
                if (pane.localToScene(enemiesPane.localToScene(currentEnemy.getObjectImage().getBoundsInParent())).intersects(b.getObjectImage().getBoundsInParent())) {
                    // kill the space invader
                    currentEnemy.killAnimation(enemiesPane);
                    enemies.remove(enemies.get(i));
                    Bullet.removeBullet(b);
                    spaceshipBullets.remove(b);
                    // update the score
                    MainApp.controller.updateScoreLabel(10);
                    break;
                }
            }
        }
    }

    public static void clearEnemies() {
        enemiesPane.getChildren().removeAll(enemiesPane.getChildren());
        enemies.removeAll(enemies);

    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
