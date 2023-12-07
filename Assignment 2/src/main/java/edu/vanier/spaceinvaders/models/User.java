/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

/**
 *
 * @author andyhou
 */
public class User extends GameObject {

    int xVelocity;
    int yVelocity;
    static int speed = 5;
    static int lives = 3;
    static ArrayList<Bullet> bullets = new ArrayList<>();
    static AudioClip userShipSingleShootAudio = new AudioClip(User.class.getResource("/sounds/sfx_laser1.wav").toExternalForm());
    static AudioClip userShipSpeedShootAudio = new AudioClip(User.class.getResource("/sounds/speedShot.wav").toExternalForm());
    static AudioClip userShipSpreadShootAudio = new AudioClip(User.class.getResource("/sounds/spreadShot.wav").toExternalForm());
    boolean invincible = false;
    boolean canShoot = true;
    boolean canSpeedShoot = true;
    boolean canSpreadShoot = true;
    boolean singleShot = true;
    boolean speedShot = false;
    boolean spreadShot = false;

    public User(ImageView userShipImage, Image image) {
        setObjectImage(userShipImage);
        getObjectImage().setImage(image);
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        User.lives = lives;
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
        User.speed = speed;
    }

    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public static void setBullets(ArrayList<Bullet> bullets) {
        User.bullets = bullets;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        this.canShoot = !invincible;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public boolean isCanSpeedShoot() {
        return canSpeedShoot;
    }

    public void setCanSpeedShoot(boolean canSpeedShoot) {
        this.canSpeedShoot = canSpeedShoot;
    }

    public boolean isCanSpreadShoot() {
        return canSpreadShoot;
    }

    public void setCanSpreadShoot(boolean canSpreadShoot) {
        this.canSpreadShoot = canSpreadShoot;
    }

    public static AudioClip getUserShipSingleShootAudio() {
        return userShipSingleShootAudio;
    }

    public static AudioClip getUserShipSpeedShootAudio() {
        return userShipSpeedShootAudio;
    }

    public static AudioClip getUserShipSpreadShootAudio() {
        return userShipSpreadShootAudio;
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

    public void setShot(int shotChoice) { // must enter either 1,2 or 3.
        setSingleShot(shotChoice == 1);
        setSpeedShot(shotChoice == 2);
        setSpreadShot(shotChoice == 3);
    }

}
