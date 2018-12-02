package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem {

    public RectangleMapObject coordinates;
    public Image image;
    public boolean collected;
    public Rectangle rectangle;
    public String itemName;

    public InventoryItem(RectangleMapObject coordinates, Image sprite, boolean collected,
                         String itemName){
        this.coordinates = coordinates;
        this.collected = collected;
        this.image = sprite;
        this.rectangle = new Rectangle(coordinates.getRectangle().getX(),
                coordinates.getRectangle().getY(), sprite.getWidth(), sprite.getHeight());
        this.itemName = itemName;
    }

    /**
     * This method sets the image coordinate accordingly.
     */
    public void renderOnMap(){
        image.setPosition(coordinates.getRectangle().getX(), coordinates.getRectangle().getY());
    }


}
