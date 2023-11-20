/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Spaceship extends Sprite {
    int velocity;
    static int speed = 5;
    StackPane spaceship;
    Image image;
    Rectangle spaceshipBody;
    ArrayList<Bullet> bullet = new ArrayList<>();
    boolean invincible = false;
    boolean canShoot = true;

    public Spaceship(StackPane spaceship, Image image) {
        this.spaceship = spaceship;
        spaceshipBody = (Rectangle)spaceship.getChildren().get(0);
        ((ImageView)spaceship.getChildren().get(1)).setImage(image);
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        Spaceship.speed = speed;
    }

    public Rectangle getSpaceshipBody() {
        return spaceshipBody;
    }

    public void setSpaceshipBody(Rectangle spaceshipBody) {
        this.spaceshipBody = spaceshipBody;
    }

    public ArrayList<Bullet> getBullet() {
        return bullet;
    }

    public void setBullet(ArrayList<Bullet> bullet) {
        this.bullet = bullet;
    }

    public StackPane getSpaceship() {
        return spaceship;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        this.canShoot = !invincible;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

}
