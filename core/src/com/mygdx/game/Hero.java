package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Hero extends Image {
    private int dx, dy;
    private Sprite sprite;
    private boolean isOnBridge=false;
    private boolean isOnTeleporter = true;

    public boolean isOnBridge() {
        return isOnBridge;
    }

    public void setOnBridge(boolean onBridge) {
        isOnBridge = onBridge;
    }

    public boolean isOnTeleporter() {
        return isOnTeleporter;
    }

    public void setOnTeleporter(boolean onTeleporter) {
        isOnTeleporter = onTeleporter;
    }

    public Hero(String path, int x, int y){

        Texture texture = new Texture(Gdx.files.internal(path));
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);

    }

    public void resetDirection(){
        dx = 0;
        dy = 0;
    }



    public boolean isMoving() {
        return (Math.abs(dx)+Math.abs(dy))!=0;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
