/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.controllers;

import edu.vanier.spaceinvaders.main.MainApp;
import edu.vanier.spaceinvaders.models.Bullet;
import edu.vanier.spaceinvaders.models.EnemyShip;
import edu.vanier.spaceinvaders.models.UserShip;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Controller class for the Enemies.
 *
 * @author andyhou
 */
public class EnemiesController {

    static ArrayList<Bullet> spaceshipBullets;
    static Pane mainPane;

    // enemiesPane is a pane inside mainPane that contains all the enemies
    // enemiesPane will move all together instead of moving every EnemyShip object one by one.
    static Pane enemiesPane = new Pane();
    static ArrayList<EnemyShip> enemies = new ArrayList<>();
    static Image enemyBulletImage;

    // Distances between enemies.
    static double enemyXdistance = 185;
    static double enemyYdistance = 100;

    /**
     * EnemyShip animation.
     */
    public static AnimationTimer enemyAnimation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            moveEnemies();
            enemiesShoot();
            // Check for collisions.
            checkBulletCollision();
        }
    };

    /**
     * Default constructor.
     */
    public EnemiesController() {
    }

    /**
     * Creates a new EnemiesController object with its main mainPane and bullet
     * image.
     *
     * @param mainPane Main mainPane of the game
     * @param enemyBulletImage Bullet image of the enemies
     */
    public EnemiesController(Pane mainPane, Image enemyBulletImage) {
        spaceshipBullets = UserShip.getBullets();
        this.mainPane = mainPane;
        this.enemyBulletImage = enemyBulletImage;
        // add enemiesPane to the main pane.
        mainPane.getChildren().add(enemiesPane);
    }

    /**
     * Spawns a specified amount of enemies into enemiesPane.
     *
     * @param enemyNumber Number of enemies to be spawned.
     */
    public static void spawn(int enemyNumber) {
        double currentLayoutX;
        double currentLayoutY = 0;
        // Will add enemies into a row one by one
        // Once an enemy reaches the max layoutX, go to the next row
        for (int i = 0; enemies.size() < enemyNumber; i++) {
            currentLayoutX = enemyXdistance * i;
            if (currentLayoutX + enemyXdistance > mainPane.getPrefWidth()) {
                currentLayoutY += enemyYdistance;
                currentLayoutX = 0;
                i = 0;
            }
            EnemyShip currentEnemy = new EnemyShip(currentLayoutX, currentLayoutY);

            // Add the enemy to the enemiesPane and the list of enemies.
            enemiesPane.getChildren().add(currentEnemy.getObjectImageView());
            enemies.add(currentEnemy);
        }
        // Put the enemiesPane at the top left of the main Pane.
        enemiesPane.setLayoutX(0);
        enemiesPane.setLayoutY(0);
    }

    /**
     * Move the enemiesPane.
     */
    private static void moveEnemies() {
        // y displacement, only move in the y direction once end of pane is met on x-axis.
        double y = enemyYdistance;
        // Checks to see if the enemiesPane needs to move right or left.
        // -- Enemies will move right when the enemiesPane layout is an even multiple of "y"
        // -- Enemies will move left when the enemiesPane layout is an odd multiple of "y"
        if ((enemiesPane.getLayoutY() / y) % 2 == 0) {
            EnemyShip.setVelocity(EnemyShip.getSpeed());
        } else {
            EnemyShip.setVelocity(-EnemyShip.getSpeed());
        }

        enemiesPane.setLayoutX(enemiesPane.getLayoutX() + EnemyShip.getVelocity());
        if (enemiesPane.getLayoutX() + enemiesPane.getWidth() >= mainPane.getWidth() || enemiesPane.getLayoutX() <= 0) {
            enemiesPane.setLayoutY(enemiesPane.getLayoutY() + y);
        }
    }

    /**
     * Allows enemies to shoot at random times.
     */
    private static void enemiesShoot() {
        // Make enemies able to singleShotBullet.
        for (EnemyShip currentEnemy : enemies) {
            // Every frame of the enemyAnimation, each enemy will be assigned a random number 
            // which will be used to check if they will shoot or not
            if (Math.random() < 0.0005) {
                Bullet currentBullet = Bullet.singleShotBullet(currentEnemy,
                        currentEnemy.getObjectImageView().getLayoutX() + enemiesPane.getLayoutX(),
                        currentEnemy.getObjectImageView().getLayoutY() + enemiesPane.getLayoutY() + currentEnemy.getObjectImageView().getFitHeight(),
                        enemyBulletImage
                );

                // Add bullet to pane and list of bullets.
                EnemyShip.getBullets().add(currentBullet);
                currentEnemy.getEnemyShootAudio().play();
            }
        }
        // Move all the enemy bullets in the main pane.
        Bullet.moveBullets(EnemyShip.getBullets(), true);
    }

    /**
     * Starts the animation.
     *
     * @throws InterruptedException
     */
    public void move() throws InterruptedException {
        enemyAnimation.start();
    }

    /**
     * Checks for collision between the enemies and the user bullets.
     */
    public static void checkBulletCollision() {
        for (int i = 0; i < enemies.size(); i++) {
            EnemyShip currentEnemy = enemies.get(i);
            for (Bullet b : spaceshipBullets) {
                // Turns the local bounds of enemiesPane to the bounds of the mainPane,
                // Turns the local bounds of the enemy to the bounds of the enemiesPane.
                if (mainPane.localToScene(enemiesPane.localToScene(currentEnemy.getObjectImageView().getBoundsInParent()))
                        .intersects(b.getObjectImageView().getBoundsInParent())) {
                    // kill the space invader
                    currentEnemy.killAnimation(enemiesPane);
                    
                    // Kill the enemy and remove user bullet from its list and the main pane.
                    enemies.remove(enemies.get(i));
                    Bullet.removeBullet(b);
                    spaceshipBullets.remove(b);
                    
                    // Update the score
                    MainApp.controller.updateScoreLabel(10);
                    break;
                }
            }
        }
    }

    /**
     * Removes all enemies from the pane and enemies list.
     */
    public static void clearEnemies() {
        enemiesPane.getChildren().removeAll(enemiesPane.getChildren());
        enemies.removeAll(enemies);

    }

    /**
     * 
     * @return list of enemies.
     */
    public static ArrayList<EnemyShip> getEnemies() {
        return enemies;
    }
}
