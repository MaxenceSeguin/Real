package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Platform {
    private Sprite platform;

    public Platform(String path, int x, int y){
        Texture texture = new Texture(Gdx.files.internal(path));
        platform = new Sprite(texture);
        platform.setPosition(x, y);
    }

    public Sprite getSprite() {
        return platform;
    }
}