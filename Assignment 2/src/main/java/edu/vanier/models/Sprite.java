/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Sprite extends Rectangle{
    static Pane pane;

    public Sprite(Rectangle sprite) {
        sprite.localToScene(sprite.getLayoutX(), sprite.getLayoutY());
        this.setWidth(sprite.getWidth());
        this.setHeight(sprite.getHeight());
        this.setLayoutX(sprite.getLayoutX());
        this.setLayoutY(sprite.getLayoutY());
    }
    public static Rectangle shoot(Rectangle shooter, double layoutX, double layoutY){
        Rectangle bullet = new Rectangle(layoutX + shooter.getWidth()/2, layoutY, 4, 20);
        pane.getChildren().add(bullet);
        return bullet;
    }

    public static void setPane(Pane pane) {
        Sprite.pane = pane;
    }
    
    public static void removeEntity(Node entity){
        pane.getChildren().remove(entity);
    }
}
