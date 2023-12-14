/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.spaceinvaders.models;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

/**
 * Parent class of all components in the game.
 *
 * @author andyhou
 */
public class GameObject {

    static Pane mainPane;
    ImageView objectImageView = new ImageView();
    AudioClip explosionAudio = new AudioClip(getClass().getResource("/sounds/8bit_bomb_explosion.wav").toExternalForm());

    /**
     *
     * @return ImageView of the object.
     */
    public ImageView getObjectImageView() {
        return objectImageView;
    }

    /**
     * Set the ImageView of the object.
     *
     * @param objectImageView
     */
    public void setObjectImageView(ImageView objectImageView) {
        this.objectImageView = objectImageView;
    }

    /**
     * Set the mainPane of this class.
     *
     * @param mainPane
     */
    public static void setMainPane(Pane mainPane) {
        GameObject.mainPane = mainPane;
    }

    /**
     *
     * @return Explosion audio.
     */
    public AudioClip getExplosionAudio() {
        return explosionAudio;
    }

}
