package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
public class MovableObject {
    private int dx,dy;
    private Sprite sprite;



    public MovableObject(String path, int x, int y){
        Texture texture = new Texture(Gdx.files.internal(path));
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }

    public boolean isMoving() {
        return (Math.abs(dx)+Math.abs(dy))!=0;
    }


    public Sprite getSprite() {
        return sprite;
    }



    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void resetDirection(){
        dx = 0;
        dy = 0;
    }
}