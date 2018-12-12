package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem {

    public RectangleMapObject coordinates; /** Coordinates of the image on the map **/
    public Image image;                    /** Image to display **/
    public boolean collected;              /** Whether the item has been collected **/
    public Rectangle rectangle;            /** Bounding rectangle of the item on the map **/
    public String itemName;                /** Name of the item **/

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
