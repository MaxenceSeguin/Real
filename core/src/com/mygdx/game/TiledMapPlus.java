/**
 * This class holds all the map information.
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public Rectangle[] deathBoxes;
    public Rectangle[] water;
    public Rectangle exitArea;
    public Rectangle backArea;
    public Rectangle[] dialogTriggeringBoxes;
    public float spawnX=0, spawnY=0;
    public ArrayList<InventoryItem> items;

    public PlatformTiled[] platformTiledArray;
    public PlatformSprite platformS;
    public Sprite[] platformSpriteArray;

    public boolean[] platformMovementArray;
    public boolean[] isOnPlatformArray;
    public float[] platformLeft;
    public float[] platformRight;

    public Sprite[] rockSprite;
    public MovableObject[] rocks;


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
        getBackArea();
        getItems(object);
        getPlatforms();
        getDeathBoxes();
        getWater();
        getDialogBoxes();
        //getRocks();

    }



    public void getRocks(){
        int numRocks = 9;
        rockSprite = new Sprite[numRocks];

        rocks = new MovableObject[numRocks];
        for(int i=0;i<numRocks;i++){
            rocks[i] = new MovableObject("Rock.png",64*(i+1),64*(i+1));
        }

        for(int i=0;i<numRocks;i++){
            rockSprite[i] = rocks[i].getSprite();
        }
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
     * This method get the area within which the hero will be teleported to the next map.
     * It will look for a layer named "Next level".
     */
    private void getBackArea(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Last level");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e) {
            System.out.println("No 'Last level' layer detected in the map. Consider adding one.");
            return;
        }
        backArea = ((RectangleMapObject)objects.get(0)).getRectangle();
    }


    /**
     * This method sets the spawning coordinates on this map.
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


    /**
     * This method initializes all the platform.
     */
    private void getPlatforms() {
        MapLayer platformsObjectLayer = tiledMap.getLayers().get("Platforms");
        MapObjects objects;
        try {
            objects = platformsObjectLayer.getObjects();
        } catch (NullPointerException e){
            System.out.println("No platforms layer detected.");
            return;
        }

        int numberOfObject = objects.getCount();

        RectangleMapObject platforms1[] = new RectangleMapObject[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            platforms1[i] = rectangleObject;
            i++;
        }

        platformTiledArray = new PlatformTiled[(i + 1) / 2];
        platformSpriteArray = new Sprite[(i + 1) / 2];
        platformLeft = new float[(i + 1) / 2];
        platformRight = new float[(i + 1) / 2];
        platformMovementArray = new boolean[(i + 1) / 2];
        isOnPlatformArray = new boolean[(i + 1) / 2];

        i = 0;

        System.out.println(platforms1.length);

        for (int j = 0; j < platforms1.length; j += 2) {
            PlatformTiled platform = new PlatformTiled(platforms1[j], platforms1[j + 1]);
            platformTiledArray[i] = platform;
            platformLeft[i] = platforms1[j].getRectangle().x;
            platformRight[i] = platforms1[j + 1].getRectangle().x;
            platformS = new PlatformSprite("crate.png", (int) platformLeft[i], (int) platforms1[j].getRectangle().y);
            platformSpriteArray[i] = platformS.getPlatformSprite();
            platformMovementArray[i] = true;
            isOnPlatformArray[i] = false;
            i++;
        }
    }


    /**
     * Returns the number of platform (length of platformSpriteArray)
     */
    public int getNumberOfPlatforms(){
        return platformSpriteArray.length;
    }


    /**
     * Displays all the platforms on the map.
     */
    public void drawPlatforms(SpriteBatch sb){
        for (int i = 0; i < getNumberOfPlatforms(); i++) {
            platformSpriteArray[i].draw(sb);
        }
    }


    /**
     * This method initializes all the teleporters and creates Teleporter instances.
     */
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

    /**
     * This methods collects all the objects of the Water layer of the tiledmap and adds
     * their bounding rectangle in the water array.
     */
    private void getWater(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Water");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e){
            System.out.println("No water");
            return;
        }

        int numberOfObject = objects.getCount();

        water = new Rectangle[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            water[i] = rectangleObject.getRectangle();
            i++;
        }
    }

    /**
     * This methods collects all the objects of the Death layer of the tiledmap and adds
     * their bounding rectangle in the deathBoxes array.
     */
    private void getDeathBoxes(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Death");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e){
            System.out.println("No death");
            return;
        }

        int numberOfObject = objects.getCount();

        deathBoxes = new Rectangle[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            deathBoxes[i] = rectangleObject.getRectangle();
            i++;
        }
    }

    /**
     * This methods collects all the objects of the Dialog layer of the tiledmap and adds
     * their bounding rectangle in the dialogTriggeringBoxes array.
     */
    private void getDialogBoxes(){
        MapLayer bridgesObjectLayer = tiledMap.getLayers().get("Dialog");
        MapObjects objects;
        try {
            objects = bridgesObjectLayer.getObjects();
        } catch (NullPointerException e){
            System.out.println("No dialog");
            return;
        }

        int numberOfObject = objects.getCount();

        dialogTriggeringBoxes = new Rectangle[numberOfObject];

        int i = 0;

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            dialogTriggeringBoxes[i] = rectangleObject.getRectangle();
            i++;
        }
    }

    /**
     * This methods collects all the objects of the Collision layer of the tiledmap and adds
     * their bounding rectangle in the collisionBoxes array.
     */
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
