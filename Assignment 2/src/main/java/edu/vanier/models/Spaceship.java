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
    ArrayList<Bullet> bullets = new ArrayList<>();
    AudioClip spaceshipShootAudio = new AudioClip(getClass().getResource("/sounds/sfx_laser1.wav").toExternalForm());
    boolean invincible = false;
    boolean canShoot = true;
    boolean singleShot = true;
    boolean speedShot = false;
    boolean spreadShot = false;

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

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
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

    public boolean isSingleShot() {
        return singleShot;
    }

    public void setSingleShot(boolean singleShot) {
        this.singleShot = singleShot;
    }

    public boolean isSpeedShot() {
        return speedShot;
    }

    public void setSpeedShot(boolean speedShot) {
        this.speedShot = speedShot;
    }

    public boolean isSpreadShot() {
        return spreadShot;
    }

    public void setSpreadShot(boolean spreadShot) {
        this.spreadShot = spreadShot;
    }
    
    public void setShot(int shotChoice){ // must enter either 1,2 or 3.
        if(shotChoice == 1){
            setSingleShot(true);
            setSpeedShot(false);
            setSpreadShot(false);
        } else if (shotChoice == 2){
            setSingleShot(false);
            setSpeedShot(true);
            setSpreadShot(false);
        } else if (shotChoice == 3){
            setSingleShot(false);
            setSpeedShot(false);
            setSpreadShot(true);
        }
    }

}
