/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Sprite extends StackPane{
    static Pane pane;
    

    public Sprite() {

    }
    public static Bullet shoot(Sprite shooter, double layoutX, double layoutY, Image image){
        Bullet bullet = new Bullet(layoutX + shooter.getWidth()/2, layoutY, image);
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
