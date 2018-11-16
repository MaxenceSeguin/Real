package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

public class Bridge {
    private int resistance;
    private RectangleMapObject rectangleObject;

    public Bridge(RectangleMapObject object, int givenResistance){
        resistance = givenResistance;
        rectangleObject = object;
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
