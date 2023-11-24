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
    ArrayList<Bullet> bullet = new ArrayList<>();
    AudioClip spaceshipShootAudio = new AudioClip(getClass().getResource("/sounds/sfx_laser1.wav").toExternalForm());
    boolean invincible = false;
    boolean canShoot = true;

    public Spaceship(StackPane spaceshipStack, Image image) {
        setSpriteStack(spaceshipStack);
        setSpriteImage((ImageView)spaceshipStack.getChildren().get(0));
        getSpriteImage().setImage(image);
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

    public ArrayList<Bullet> getBullet() {
        return bullet;
    }

    public void setBullet(ArrayList<Bullet> bullet) {
        this.bullet = bullet;
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
