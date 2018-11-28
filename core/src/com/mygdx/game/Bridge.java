package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Bridge {
    private int resistance;
    private RectangleMapObject rectangleObject;
    private TiledMapTileLayer healthy;
    private TiledMapTileLayer broken;

    public Bridge(RectangleMapObject object, int givenResistance, TiledMapTileLayer healthySprite, TiledMapTileLayer brokenSprite){
        resistance = givenResistance;
        rectangleObject = object;
        healthy = healthySprite;
        broken = brokenSprite;
    }

    public void setBrokenVisible() {
        broken.setVisible(true);
        healthy.setVisible(false);
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public RectangleMapObject getRectangleObject() {
        return rectangleObject;
    }

    public void setRectangleObject(RectangleMapObject rectangleObject) {
        this.rectangleObject = rectangleObject;
    }

    public void weakenBridge(){
        this.resistance = Math.max(this.resistance-1, 0);
    }
}
