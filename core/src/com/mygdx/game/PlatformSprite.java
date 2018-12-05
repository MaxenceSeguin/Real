package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlatformSprite {
    private Sprite platformS;
    private boolean[] movingAway;
    private boolean[] platformDirection;

    public PlatformSprite(String path, int x, int y){

        Texture texture = new Texture(Gdx.files.internal(path));
        platformS = new Sprite(texture);
        platformS.setPosition(x, y);
    }


    public boolean[] getPlatformDirection() {
        return platformDirection;
    }

    public void setPlatformDirection(boolean[] platformDirection) {
        this.platformDirection = platformDirection;
    }


    public Sprite getPlatformSprite() {
        return platformS;
    }

    public void setPlatformSprite(Sprite platformSprite) {
        this.platformS = platformSprite;
    }
}
