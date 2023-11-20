/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2276884
 */
public class Bullet extends Sprite {
    
    public Bullet(double layoutX, double layoutY, Image image) {
        setWidth(4);
        setHeight(20);
        setLayoutX(layoutX);
        setLayoutY(layoutY);
        getChildren().addAll(new Rectangle(getWidth(), getHeight()), new ImageView());
        ((ImageView)getChildren().get(1)).setImage(image);
    }
    
}
