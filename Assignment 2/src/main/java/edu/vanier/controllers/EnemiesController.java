/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Bullet;
import edu.vanier.models.Enemy;
import edu.vanier.models.Sprite;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class EnemiesController {

    static ArrayList<Bullet> spaceshipBullets = new ArrayList<>();
    StackPane enemy;
    Pane pane;
    Pane enemiesPane = new Pane();
    ArrayList<Enemy> enemies = new ArrayList<>();
    double movementDuration;
    Image enemyBulletImage;
    double enemyXdistance = 185;
    double enemyYdistance = 100;

    public EnemiesController(Pane pane, double movementDuration, Image enemyBulletImage) {
        enemiesPane.setPrefWidth(800);
        enemiesPane.setPrefHeight(400);
        this.pane = pane;
        this.movementDuration = movementDuration;
        this.enemyBulletImage = enemyBulletImage;
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
            enemiesPane.getChildren().add(currentEnemy.getSpriteStack());
            enemies.add(currentEnemy);

        }
        pane.getChildren().add(enemiesPane);
    }

    public void moveEnemies() throws InterruptedException {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(movementDuration), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                double y = enemyYdistance;

                if ((enemiesPane.getLayoutY() / y) % 2 == 0) {
                    Enemy.setVelocity(Enemy.getHorizontalMovementSpeed());
                } else {
                    Enemy.setVelocity(-Enemy.getHorizontalMovementSpeed());
                }

                enemiesPane.setLayoutX(enemiesPane.getLayoutX() + Enemy.getVelocity());
                if (enemiesPane.getLayoutX() + enemiesPane.getWidth() >= pane.getWidth() || enemiesPane.getLayoutX() <= 0) {
                    enemiesPane.setLayoutY(enemiesPane.getLayoutY() + y);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Make enemies able to shoot.
        enemiesShoot();

        // Check for collisions.
        checkBulletCollision();
    }

    public void enemiesShoot() {
        Timeline enemyShoot = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (Enemy currentEnemy : enemies) {
                    if (Math.random() < 0.15) {
                        Bullet currentBullet = Sprite.shoot(currentEnemy,
                                currentEnemy.getSpriteStack().getLayoutX() + enemiesPane.getLayoutX(),
                                currentEnemy.getSpriteStack().getLayoutY() + enemiesPane.getLayoutY() + currentEnemy.getSpriteStack().getHeight(),
                                enemyBulletImage
                        );
                        Enemy.getBullets().add(currentBullet);
                        SpaceshipController.enemyBullets.add(currentBullet);
                        currentEnemy.getEnemyShootAudio().play();
                    }
                }
            }
        }));
        enemyShoot.setCycleCount(Timeline.INDEFINITE);
        enemyShoot.play();

        AnimationTimer bulletMove = new AnimationTimer() {
            @Override
            public void handle(long n) {
                for (Bullet bullet : Enemy.getBullets()) {
                    bullet.getSpriteStack().setLayoutY(bullet.getSpriteStack().getLayoutY() + 3);
                }
            }
        };
        bulletMove.start();

    }

    public void checkBulletCollision() {
        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long n) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy currentEnemy = enemies.get(i);
                    for (Bullet b : spaceshipBullets) {
                        // turns the local bounds of enemiesPane to the bounds of the pane,
                        // turns the local bounds of the enemy to the bounds of the enemiesPane.
                        if (pane.localToScene(enemiesPane.localToScene(currentEnemy.getSpriteStack().getBoundsInParent())).intersects(b.getSpriteStack().getBoundsInParent())) {
                            // kill the space invader
                            currentEnemy.getEnemyImage().setImage(new Image("/images/Explosion.gif"));
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                            pause.setOnFinished((event) -> {
                                enemiesPane.getChildren().remove(currentEnemy.getSpriteStack());
                            });
                            pause.setCycleCount(1);
                            pause.play();
                            enemies.get(i).getEnemyExplosionAudio().play();
                            enemies.remove(enemies.get(i));
                            Sprite.removeEntity(b);
                            spaceshipBullets.remove(b);
                            break;
                        }
                    }

                }
            }
        };
        animation.start();
    }
}
