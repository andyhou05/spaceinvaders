/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author andyhou
 */
public class Spaceship extends Sprite {
    int xVelocity;
    int yVelocity;
    static int speed = 5;
    StackPane spaceship;
    Rectangle spaceshipBody;
    ArrayList<Bullet> bullet = new ArrayList<>();
    AudioClip spaceshipShootAudio = new AudioClip(getClass().getResource("/audio/sfx_laser1.wav").toExternalForm());
    boolean invincible = false;
    boolean canShoot = true;

    public Spaceship(StackPane spaceship, Image image) {
        this.spaceship = spaceship;
        spaceshipBody = (Rectangle)spaceship.getChildren().get(0);
        ((ImageView)spaceship.getChildren().get(1)).setImage(image);
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
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

    public AudioClip getSpaceshipShootAudio() {
        return spaceshipShootAudio;
    }

}
