/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.controllers;

import edu.vanier.models.Enemy;
import edu.vanier.models.Sprite;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class EnemiesController {

    static ArrayList<Rectangle> spaceshipBullets = new ArrayList<>();
    Rectangle enemy;
    Pane pane;
    Pane enemiesPane;
    ArrayList<Enemy> enemies = new ArrayList<>();
    double movementDuration;

    public EnemiesController(Rectangle enemy, Pane pane, Pane enemiesPane, double movementDuration) {
        this.enemy = enemy;
        this.pane = pane;
        this.enemiesPane = enemiesPane;
        this.movementDuration = movementDuration;
        for (Node currentEnemyBody : enemiesPane.getChildren()) {
            enemies.add(new Enemy((Rectangle) currentEnemyBody));
        }
    }

    public void moveEnemies() throws InterruptedException {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(movementDuration), new EventHandler<ActionEvent>() {
            double xSpacing = enemiesPane.getLayoutX();
            double ySpacing = enemiesPane.getLayoutY();

            @Override
            public void handle(ActionEvent event) {
                double y = enemy.getHeight();

                if (((enemiesPane.getLayoutY() - ySpacing) / y) % 2 == 0) {
                    Enemy.setVelocity(Enemy.getHorizontalMovementSpeed());
                } else {
                    Enemy.setVelocity(-Enemy.getHorizontalMovementSpeed());
                }

                enemiesPane.setLayoutX(enemiesPane.getLayoutX() + Enemy.getVelocity());
                if (enemiesPane.getLayoutX() + enemiesPane.getWidth() >= pane.getWidth() - xSpacing || enemiesPane.getLayoutX() <= xSpacing) {
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
                        Rectangle currentBullet = Sprite.shoot(currentEnemy.getEnemyBody(),
                                currentEnemy.getEnemyBody().getLayoutX() + enemiesPane.getLayoutX(),
                                currentEnemy.getEnemyBody().getLayoutY() + enemiesPane.getLayoutY() + currentEnemy.getEnemyBody().getHeight());
                        Enemy.getBullets().add(currentBullet);
                        SpaceshipController.enemyBullets.add(currentBullet);
                    }
                }
            }
        }));
        enemyShoot.setCycleCount(Timeline.INDEFINITE);
        enemyShoot.play();

        AnimationTimer bulletMove = new AnimationTimer() {
            @Override
            public void handle(long n) {
                for (Rectangle bullet : Enemy.getBullets()) {
                    bullet.setLayoutY(bullet.getLayoutY() + 3);
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
                    for (Rectangle b : spaceshipBullets) {
                        // turns the local bounds of enemiesPane to the bounds of the pane,
                        // turns the local bounds of the enemy to the bounds of the enemiesPane.
                        if (pane.localToScene(enemiesPane.localToScene(enemies.get(i).getEnemyBody().getBoundsInParent())).intersects(b.getBoundsInParent())) {
                            // kill the space invader
                            enemiesPane.getChildren().remove(enemies.get(i).getEnemyBody());
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
