package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


/**
 * This class manages all the items/collectible in game and the interface (health bar).
 */
public class GameInterface {
    public Hero hero;
    public SpriteBatch sb;
    public GameOrthoCamera camera;
    public Image heart;
    public TiledMapPlus tiledMap;

    public GameInterface(Hero hero, SpriteBatch sb, GameOrthoCamera camera, TiledMapPlus tiledMap){
        this.hero = hero;
        this.sb = sb;
        this.camera = camera;
        heart = new Image(new Texture(Gdx.files.internal("heart.png")));
        this.tiledMap = tiledMap;
    }

    /**
     * Called to refresh the interface in the game renderer.
     */
    public void refresh(){
        refreshHeart();
        itemCollectionListener();
        refreshInventoryItems();

    }

    /**
     * Renders the heart health indicator accordingly to the hero health.
     */
    private void refreshHeart(){
        for (int i = 1; i < hero.health + 1; i++){
            heart.setPosition((float)camera.upperRightCorner.getX() - i * heart.getImageWidth(),
                    (float)camera.upperRightCorner.getY() - heart.getImageHeight());
            heart.draw(sb, 1);
        }

    }

    /**
     * If an item has been collected (and is not a heart), its image will be located on the
     * bottom left corner of the screen.
     */
    private void refreshInventoryItems(){
        for (InventoryItem item : tiledMap.items){
            if (item.collected) {
                if (item.itemName == "heart") {
                    item.coordinates.getRectangle().setPosition(tiledMap.width, tiledMap.height);
                } else {
                    item.coordinates.getRectangle().setPosition((float) camera.bottomLeftCorner.getX(),
                            (float) camera.bottomLeftCorner.getY());
                }
            }
            item.renderOnMap();
            item.image.draw(sb, 1);
        }
    }

    /**
     * Whenever an item is walked on by the hero, checks if it already got used, otherwise, the item
     * will be marked as 'collected', and an effect will be triggered according to the item.
     */
    private void itemCollectionListener() {
        for (InventoryItem item : tiledMap.items){
            if (!item.collected && Intersector.overlaps(hero.getRectangle(), item.rectangle)) {
                item.collected = true;
                itemCollected(item);
            }
        }
    }

    /**
     * If the item collected is a heart, adds 1 health point to the hero.
     */
    private void itemCollected(InventoryItem item){
        if (item.itemName == "heart"){
            hero.health ++;
        }
    }

}
