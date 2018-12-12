package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.geom.Point2D;

/**
 * This class manage the camera used in game.
 */
public class GameOrthoCamera extends OrthographicCamera {
    public Sprite heroSprite;
    public TiledMapPlus tiledMap;
    /**
     * These attributes hold the coordinates of the corners of the camera.
     */
    public Point2D upperRightCorner;
    public Point2D bottomLeftCorner;
    public Point2D upperLeftCorner;

    public GameOrthoCamera(Sprite heroSprite, TiledMapPlus tiledMap){
        super();
        this.setToOrtho(false, 800, 450);
        this.update();
        this.heroSprite = heroSprite;
        this.tiledMap = tiledMap;
        this.upperRightCorner = new Point2D.Float(this.position.x + this.viewportWidth/2,
                this.position.y + this.viewportHeight/2);
        this.bottomLeftCorner = new Point2D.Float(this.position.x - this.viewportWidth/2,
                this.position.y - this.viewportHeight/2);
        this.upperLeftCorner = new Point2D.Float(this.position.x - this.viewportWidth/2,
                this.position.y + this.viewportHeight/2);

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
        this.upperLeftCorner = new Point2D.Float(this.position.x - this.viewportWidth/2,
                this.position.y + this.viewportHeight/2);
    }

    /**
     * Displays the given dialog box when the hero is overlapping the 'dialog triggering' area.
     */
    public void showDialog(String filePath, SpriteBatch sb){
        for (Rectangle object : tiledMap.dialogTriggeringBoxes){
            if (Intersector.overlaps(object, heroSprite.getBoundingRectangle())){
                Image image = new Image(new Texture(Gdx.files.internal(filePath)));
                image.setPosition((float)this.upperLeftCorner.getX()+12,
                        (float)this.upperLeftCorner.getY() -12 -image.getHeight());
                image.draw(sb, 1);
            }
        }
    }

}
