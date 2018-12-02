package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.SerializationException;

import java.util.ArrayList;

public class TiledMapPlus {
    private TiledMap tiledMap;
    public int width;
    public int height;
    public OrthogonalTiledMapRenderer tiledMapRenderer;
    public Rectangle[] collisionBoxes;
    public Bridge[] bridges;
    public Teleporter[] teleporters;
    public Rectangle exitArea;
    public float spawnX=0, spawnY=0;
    public ArrayList<InventoryItem> items;

    public TiledMapPlus(String fileLocation, String object[]){
        try {
            this.tiledMap = new TmxMapLoader().load(fileLocation);
        } catch (SerializationException e) {
            System.out.println("Probably wrong file path.");
        }
        this.width = (Integer) tiledMap.getProperties().get("width") * (Integer) tiledMap.getProperties().get("tilewidth");
        this.height = (Integer) tiledMap.getProperties().get("height") * (Integer) tiledMap.getProperties().get("tileheight");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        getBridges();
        getTeleporters();
        getCollisionBoxes();
        setSpawnArea();
        getExitArea();
        getItems(object);

    }

    /**
     * This method gather all the wanted objects of the tiled map.
     * The method looks for object named in the string array given as a parameter.
     * If they are found, then they are considered an InventoryItem and will be given a name,
     * a 'collected' state, an image (the one they'll have in game), and the location they'll be
     * appearing at.
     * All the item created are added in the items ArrayList.
     */
    private void getItems(String itemList[]){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Items");
        MapObjects objects;
        items = new ArrayList<InventoryItem>();
        MapObject object;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e) {
            System.out.println("No 'Items' layer detected in the map. Consider adding one.");
            return;
        }
        for (String itemName : itemList){
            object = objects.get(itemName);
            while (object != null) {
                items.add(new InventoryItem((RectangleMapObject)object,
                        new Image(new Texture(Gdx.files.internal(itemName + ".png"))), false,
                        itemName));
                object = objects.get(itemName);
                objects.remove(object);
            }
        }
    }

    /**
     * This method get the area within which the hero will be teleported to the next map.
     * It will look for a layer named "Next level".
     */
    private void getExitArea(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Next level");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e) {
            System.out.println("No 'Next level' layer detected in the map. Consider adding one.");
            return;
        }
        exitArea = ((RectangleMapObject)objects.get(0)).getRectangle();
    }

    /**
     *
     */
    private void setSpawnArea(){
        try {
            MapLayer spawnPoint = tiledMap.getLayers().get("Spawn");
            spawnX = (Float) spawnPoint.getObjects().get(0).getProperties().get("x");
            spawnY = (Float) spawnPoint.getObjects().get(0).getProperties().get("y");
        } catch (NullPointerException e){}

    }

    /**
     * This method create a Bridge object for every object in the bridge layer of the map and then
     * stocks them in the 'bridges' array.
     */
    private void getBridges(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Bridges");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e) {
            return;
        }

        int numberOfObject = objects.getCount();

        bridges = new Bridge[numberOfObject];

        int i = 0;
        int j;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            j = i+1;
            String id = "Bridge " + j;
            String bid = "Broken Bridge " + j;
            bridges[i] = new Bridge(rectangleObject, 3, (TiledMapTileLayer) tiledMap.getLayers().get(id), (TiledMapTileLayer) tiledMap.getLayers().get(bid));
            i++;
        }
    }

    private void getTeleporters(){
        MapLayer teleportersObjectLayer = tiledMap.getLayers().get("Teleporters");
        MapObjects objects;
        try {
            objects = teleportersObjectLayer.getObjects();
        } catch (NullPointerException e) {
            return;
        }

        int numberOfObject = objects.getCount();

        RectangleMapObject teleporters1[] = new RectangleMapObject[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            teleporters1[i] = rectangleObject;
            i++;
        }
        teleporters = new Teleporter[(i+1)/2];
        i = 0;

        /* Create pair of teleporters (one with the next one so it has to be created
         * accordingly in the TileMap */
        for (int j = 0; j < teleporters1.length; j+=2){
            Teleporter teleporter = new Teleporter(teleporters1[j], teleporters1[j+1]);

            teleporters[i] = teleporter;
            i++;
        }
    }

    private void getCollisionBoxes(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Collision");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e){
            System.out.println("!!! NO COLLISION LAYER DETECTED !!!");
            return;
        }

        int numberOfObject = objects.getCount();

        collisionBoxes = new Rectangle[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            collisionBoxes[i] = rectangleObject.getRectangle();
            i++;
        }
    }

    public TiledMap getMap(){
        return tiledMap;
    }





}
