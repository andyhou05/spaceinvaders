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
    public static Rectangle shoot(Rectangle shooter){
        Rectangle bullet = new Rectangle(shooter.getLayoutX() + shooter.getWidth()/2, shooter.getLayoutY(), 4, 20);
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
