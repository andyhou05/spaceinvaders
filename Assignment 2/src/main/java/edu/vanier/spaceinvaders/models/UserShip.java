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
public class UserShip extends Spaceship {

    int xVelocity;
    int yVelocity;
    final static int SPEED = 5;
    static int lives = 3;
    static ArrayList<Bullet> bullets = new ArrayList<>();

    static AudioClip userShipSingleShootAudio = new AudioClip(UserShip.class.getResource("/sounds/sfx_laser1.wav").toExternalForm());
    static AudioClip userShipSpeedShootAudio = new AudioClip(UserShip.class.getResource("/sounds/speedShot.wav").toExternalForm());
    static AudioClip userShipSpreadShootAudio = new AudioClip(UserShip.class.getResource("/sounds/spreadShot.wav").toExternalForm());

    boolean invincible = false;
    boolean canSingleShoot = true;
    boolean canSpeedShoot = true;
    boolean canSpreadShoot = true;
    boolean singleShotSelected = true;
    boolean speedShotSelected = false;
    boolean spreadShotSelected = false;

    /**
     * Default Constructor.
     */
    public UserShip() {
    }

    /**
     * Creates user with specified userShipImageView loaded from FXML file and
     * specified Image to be put onto the userShipImageView.
     *
     * @param userShipImageView
     * @param image
     */
    public UserShip(ImageView userShipImageView, Image image) {
        userShipImageView.setPreserveRatio(false);
        setObjectImageView(userShipImageView);
        getObjectImageView().setImage(image);
    }

    /**
     *
     * @return xVelolcity of the UserShip.
     */
    public int getxVelocity() {
        return xVelocity;
    }

    /**
     * Sets the xVelocity of the UserShip, velocity is the accounts for both SPEED
 and magnitude.
     *
     * @param xVelocity
     */
    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     *
     * @return Lives remaining.
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Sets the amount of lives remaining.
     *
     * @param lives
     */
    public static void setLives(int lives) {
        UserShip.lives = lives;
    }

    /**
     *
     * @return yVelolcity of the UserShip.
     */
    public int getyVelocity() {
        return yVelocity;
    }

    /**
     * Sets the yVelocity of the UserShip, velocity is the accounts for both SPEED
 and magnitude.
     *
     * @param yVelocity
     */
    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    /**
     *
     * @return Speed of the UserShip.
     */
    public static int getSPEED() {
        return SPEED;
    }

    /**
     *
     * @return List of UserShip bullets.
     */
    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Sets the list of UserShip bullets.
     *
     * @param bullets
     */
    public static void setBullets(ArrayList<Bullet> bullets) {
        UserShip.bullets = bullets;
    }

    /**
     *
     * @return Invincibility status of the UserShip.
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Sets the invincible status of the UserShip.
     *
     * @param invincible
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        // If the user is invincible, cannot shoot, vice-versa.
        this.canSingleShoot = !invincible;
    }

    /**
     * Sets canSingleShoot parameter, tells us if the user can shoot or if is on
     * cooldown.
     *
     * @param canSingleShoot
     */
    public void setCanSingleShoot(boolean canSingleShoot) {
        this.canSingleShoot = canSingleShoot;
    }

    /**
     *
     * @return canSingleShoot parameter.
     */
    public boolean isCanSingleShoot() {
        return canSingleShoot;
    }

    /**
     *
     * @return canSpeedShoot parameter.
     */
    public boolean isCanSpeedShoot() {
        return canSpeedShoot;
    }

    /**
     * Sets canSpeedShoot parameter, tells us if the user can SpeedShoot or if
     * is on cooldown.
     *
     * @param canSpeedShoot
     */
    public void setCanSpeedShoot(boolean canSpeedShoot) {
        this.canSpeedShoot = canSpeedShoot;
    }

    /**
     *
     * @return canSpreadShoot parameter.
     */
    public boolean isCanSpreadShoot() {
        return canSpreadShoot;
    }

    /**
     * Sets canSpradShoot parameter, tells us if the user can SpreadShoot or if
     * is on cooldown.
     *
     * @param canSpeedShoot
     */
    public void setCanSpreadShoot(boolean canSpreadShoot) {
        this.canSpreadShoot = canSpreadShoot;
    }

    /**
     *
     * @return singleShoot audio.
     */
    public static AudioClip getUserShipSingleShootAudio() {
        return userShipSingleShootAudio;
    }

    /**
     *
     * @return speedShoot audio.
     */
    public static AudioClip getUserShipSpeedShootAudio() {
        return userShipSpeedShootAudio;
    }

    /**
     *
     * @return spreadShoot audio.
     */
    public static AudioClip getUserShipSpreadShootAudio() {
        return userShipSpreadShootAudio;
    }

    /**
     *
     * @return canSpreadShoot parameter.
     */
    public boolean isSingleShotSelected() {
        return singleShotSelected;
    }

    /**
     * Sets the singleShotSelected parameter, tells us if the singleShot is
     * selected or not.
     *
     * @param singleShotSelected
     */
    public void setSingleShotSelected(boolean singleShotSelected) {
        this.singleShotSelected = singleShotSelected;
    }

    /**
     *
     * @return speedShootSelected parameter.
     */
    public boolean isSpeedShotSelected() {
        return speedShotSelected;
    }

    /**
     * Sets the speedShotSelected parameter, tells us if the speedShot is
     * selected or not.
     *
     * @param speedShotSelected
     */
    public void setSpeedShotSelected(boolean speedShotSelected) {
        this.speedShotSelected = speedShotSelected;
    }

    /**
     *
     * @return spreadShotSelected parameter.
     */
    public boolean isSpreadShotSelected() {
        return spreadShotSelected;
    }

    /**
     * Sets the spredShotSelected parameter, tells us if the spreadShot is
     * selected or not.
     *
     * @param spreadShotSelected
     */
    public void setSpreadShotSelected(boolean spreadShotSelected) {
        this.spreadShotSelected = spreadShotSelected;
    }

    /**
     * Sets the desired shot type, shotChoice must be between 1 and 3, 1 is
     * singleShot, 2 is speedShot, and 3 is spreadShot.
     *
     * @param shotChoice
     */
    public void setShot(int shotChoice) {
        setSingleShotSelected(shotChoice == 1);
        setSpeedShotSelected(shotChoice == 2);
        setSpreadShotSelected(shotChoice == 3);
        if (shotChoice > 3 || shotChoice < 1) {
            throw new ArithmeticException("Shot choice must be between 1 and 3.");
        }
    }

}
