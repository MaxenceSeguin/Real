package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Overgrowth {
    public Rectangle sprite;
    public TiledMapTileLayer layer;

    public Overgrowth(Rectangle sprite, TiledMapTileLayer layer){
        this.sprite = sprite;
        this.layer = layer;
    }

    public void hide(){
        this.layer.setVisible(false);
    }
}
