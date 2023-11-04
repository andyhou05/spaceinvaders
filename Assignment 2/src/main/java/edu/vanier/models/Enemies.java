/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author andyhou
 */
public class Enemies {

    static Rectangle enemy;
    static Pane pane;
    static Pane enemiesPane;

    public static void setComponents(Rectangle enemy, Pane pane, Pane enemiesPane) {
        Enemies.enemy = enemy;
        Enemies.pane = pane;
        Enemies.enemiesPane = enemiesPane;
    }

    public static void moveEnemies() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
            double xSpacing = enemiesPane.getLayoutX();
            double ySpacing = enemiesPane.getLayoutY();

            @Override
            public void handle(ActionEvent event) {
                double x;
                double y = enemy.getHeight();

                if (((enemiesPane.getLayoutY() - ySpacing) / y) % 2 == 0) {
                    x = 10;
                } else {
                    x = -10;
                }

                enemiesPane.setLayoutX(enemiesPane.getLayoutX() + x);
                if (enemiesPane.getLayoutX() + enemiesPane.getWidth() >= pane.getWidth() - xSpacing || enemiesPane.getLayoutX() <= xSpacing) {
                    enemiesPane.setLayoutY(enemiesPane.getLayoutY() + y);
                }

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    public static void checkBulletCollision() {
        ArrayList<Rectangle> list = new ArrayList<>();

        for (Node currentEnemy : enemiesPane.getChildren()) {
            list.add((Rectangle) currentEnemy);
        }

        for (Rectangle enemy : list) {
            for (Rectangle b : Spaceship.bullet) {
                // turns the local bounds of enemiesPane to the bounds of the pane,
                // turns the local bounds of the enemy to the bounds of the enemiesPane.
                if (pane.localToScene(enemiesPane.localToScene(enemy.getBoundsInParent())).intersects(b.getBoundsInParent())) {
                    // kill the space invader
                    enemiesPane.getChildren().remove(enemy);
                    Sprite.removeEntity(b);
                    Spaceship.bullet.remove(b);
                    break;
                }
            }

        }

    }
}
