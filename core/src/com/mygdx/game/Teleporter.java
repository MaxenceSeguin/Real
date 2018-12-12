package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import java.awt.Rectangle;

public class Teleporter {
    private RectangleMapObject teleporter1;
    private RectangleMapObject teleporter2;
    private int readyForActivation;

    public Teleporter(RectangleMapObject teleporter1, RectangleMapObject teleporter2){
        this.teleporter1 = teleporter1;
        this.teleporter2 = teleporter2;
        readyForActivation = 0;
    }

    public int getReadyForActivation() {
        return readyForActivation;
    }

    public void setReadyForActivation(int readyForActivation) {
        this.readyForActivation = readyForActivation;
    }

    public RectangleMapObject getTeleporter2() {
        return teleporter2;
    }

    public void setTeleporter2(RectangleMapObject teleporter2) {
        this.teleporter2 = teleporter2;
    }

    public RectangleMapObject getTeleporter1() {
        return teleporter1;
    }

    public void setTeleporter1(RectangleMapObject teleporter1) {
        this.teleporter1 = teleporter1;
    }

    /**
     * Teleports the Sprite given as parameters to the according teleporter location (the second one
     * if we are on the first one and vice versa)
     */
    public void teleportTo(Sprite object){
        if(readyForActivation == 1){
            object.setPosition(teleporter1.getRectangle().getX(), teleporter1.getRectangle().getY());
        } else if (readyForActivation == 2){
            object.setPosition(teleporter2.getRectangle().getX(), teleporter2.getRectangle().getY());
        }

    }

}
