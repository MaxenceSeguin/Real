package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.geom.Point2D;

/**
 * This class manage the camera used in game.
 */
public class GameOrthoCamera extends OrthographicCamera {
    public Sprite heroSprite;
    public TiledMapPlus tiledMap;
    public Point2D upperRightCorner;
    public Point2D bottomLeftCorner;

    public GameOrthoCamera(Sprite heroSprite, TiledMapPlus tiledMap){
        super();
        this.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.update();
        this.heroSprite = heroSprite;
        this.tiledMap = tiledMap;
        this.upperRightCorner = new Point2D.Float(this.position.x + this.viewportWidth/2,
                this.position.y + this.viewportHeight/2);
        this.bottomLeftCorner = new Point2D.Float(this.position.x - this.viewportWidth/2,
                this.position.y - this.viewportHeight/2);

    }

    /**
     * Called in the render function.
     * Centers the camera on the hero (whenever possible) and refreshes the corners coordinates.
     */
    public void updateCamera(){
        this.position.x = Math.max(Math.min(heroSprite.getX(), tiledMap.width - this.viewportWidth/2), this.viewportWidth/2);
        this.position.y = Math.max(Math.min(heroSprite.getY(), tiledMap.height - this.viewportHeight/2), this.viewportHeight/2);
        refreshCornersCoordinates();
        this.update();
    }

    /**
     * As the camera moves, its corners move as well.
     * This function recalculates the corner coordinates.
     */
    private void refreshCornersCoordinates(){
        this.upperRightCorner.setLocation(this.position.x + this.viewportWidth/2,
                this.position.y + this.viewportHeight/2);
        this.bottomLeftCorner.setLocation(this.position.x - this.viewportWidth/2,
                this.position.y - this.viewportHeight/2);
    }

}
