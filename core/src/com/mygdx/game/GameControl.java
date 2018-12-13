package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public abstract class GameControl {
    static public Image control = new Image(new Texture(Gdx.files.internal("GameControl.png")));
    static public boolean show;

    static public void display(SpriteBatch sb, float x, float y){
        control.setPosition(x,y);
        if (show){
            control.draw(sb, 1);
        }
    }


}
