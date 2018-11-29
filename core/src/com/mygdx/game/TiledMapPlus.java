package com.mygdx.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class TiledMapPlus {
    private TiledMap tiledMap;
    public int width;
    public int height;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Rectangle[] collisionBoxes;
    private Bridge[] bridges;
    private Teleporter[] teleporters;

    public TiledMapPlus(String fileLocation){
        this.tiledMap = new TmxMapLoader().load(fileLocation);
        this.width = (Integer) tiledMap.getProperties().get("width");
        this.height = (Integer) tiledMap.getProperties().get("height");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        getBridges();
        getTeleporters();
        getCollisionBoxes();

    }

    /**
     * This method create a Bridge object for every object in the bridge layer of the map and then
     * stocks them in the 'bridges' array.
     */
    private void getBridges(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Bridges");
        MapObjects objects = bridgesObjectLayer.getObjects();

        int numberOfObject = objects.getCount();

        bridges = new Bridge[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            bridges[i] = new Bridge(rectangleObject, 3, (TiledMapTileLayer) tiledMap.getLayers().get("Bridge " + (i+1)), (TiledMapTileLayer) tiledMap.getLayers().get("Broken bridge " + (i+1)));
            i++;
        }
    }

    private void getTeleporters(){
        MapLayer teleportersObjectLayer = tiledMap.getLayers().get("Teleporters");
        MapObjects objects = teleportersObjectLayer.getObjects();

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
        MapObjects objects = bridgesObjectLayer.getObjects();

        int numberOfObject = objects.getCount();

        collisionBoxes = new Rectangle[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            collisionBoxes[i] = rectangleObject.getRectangle();
            i++;
        }
    }





}
