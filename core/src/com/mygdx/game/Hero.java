package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Hero extends Image {
    private int dx, dy;
    private Sprite sprite;
    private boolean isOnBridge=false;
    private int isOnTeleporter = -1;
    private TiledMapPlus tiledMap;
    public Animation walkingRightAnimation;
    private float elapsedTime;
    public int health;

    public boolean isOnBridge() {
        return isOnBridge;
    }

    public void setOnBridge(boolean onBridge) {
        isOnBridge = onBridge;
    }

    public Hero(String path, TiledMapPlus tiledmap, String filePath){

        this.tiledMap = tiledmap;
        Texture texture = new Texture(Gdx.files.internal(path));
        this.sprite = new Sprite(texture);
        sprite.setPosition(tiledmap.spawnX, tiledmap.spawnY);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal(filePath));
        this.walkingRightAnimation = new Animation(1f/15f, textureAtlas.getRegions());
        this.health = 3;

    }

    public int getIsOnTeleporter() {
        return isOnTeleporter;
    }

    public void setIsOnTeleporter(int isOnTeleporter) {
        this.isOnTeleporter = isOnTeleporter;
    }

    public void resetDirection(){
        dx = 0;
        dy = 0;
    }



    public boolean isMoving() {
        return (Math.abs(dx)+Math.abs(dy))!=0;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getRectangle(){
        return sprite.getBoundingRectangle();
    }

    /**
     * This methods checks if the hero has to move.
     * If so it also checks if the hero will enter in collision with any object
     * in the COLLISION layer.
     * If not, then the hero will move and activate the moving state by returning true.
     */
    public boolean heroMovement(){
        int heroX = this.getDx();
        int heroY = this.getDy();

        if (this.isMoving() && !isCollision(heroX, heroY) && !isCrossingBrokenBridge(heroX, heroY)){
            sprite.setPosition(sprite.getX()+heroX, sprite.getY()+heroY);
            return true;
        }
        return false;
    }

    /**
     * This method deals with the bridges and hero interaction:
     *      - if the hero is on a bridge and go out of a bridge in the next move, then the bridge is
     *      weakened, and the hero 'isOnBridge'(iOB) is set to false. Returns false.
     *      - if the hero is on a bridge and stay on the bridge for the next move, then it
     *      returns false.
     *      - if the hero is not on a bridge and goes on a bridge, set iOB to true. Then if the
     *      bridge is 'broken', return true. Otherwise, returns false.
     *      - if the hero is not on a bridge and won't be on a bridge in the next move, then return
     *      false.
     */
    private boolean isCrossingBrokenBridge(int dx, int dy){
        Rectangle heroPos = sprite.getBoundingRectangle();
        heroPos.setX(heroPos.getX()+dx);
        heroPos.setY(heroPos.getY()+dy);
        int bridgeNumber = -1;
        int number;

        try {
            number = tiledMap.bridges.length;
        } catch (NullPointerException e){
            return false;
        }

        for(int i = 0; i < number; i++){

            Bridge bridge = tiledMap.bridges[i];
            if (bridgeNumber == -1 && bridge.getRectangleObject().getRectangle().overlaps(heroPos)){
                bridgeNumber = i;
                if(!this.isOnBridge()){
                    tiledMap.bridges[bridgeNumber].weakenBridge();
                }
                this.setOnBridge(true);

            }
        }
        if (bridgeNumber == -1){
            // If the bridge he is going to leave is broken, change the image of the bridge to
            // a broken one.
            for (int i = 0; i < number; i++){
                if (tiledMap.bridges[i].getResistance() == 1 && tiledMap.bridges[i].getRectangleObject().getRectangle().overlaps(sprite.getBoundingRectangle())){
                    tiledMap.bridges[i].setBrokenVisible();
                }
            }
            this.setOnBridge(false);
        }

        try {
            if (tiledMap.bridges[bridgeNumber].getResistance() == 0){
                return true;
            }
        } catch (IndexOutOfBoundsException e) {}
        return false;

    }

    /**
     *  This methods checks if the next move implies a collision between the hero and any
     *  'rectangle' object from the 'COLLISION' layer.
     *  Idea from https://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
     * @param dx direction of the hero on the abscissa
     * @param dy direction of the hero on the ordinate
     * @return false, no collision detected; true otherwise
     */
    private boolean isCollision(int dx, int dy){

        Rectangle heroPos = sprite.getBoundingRectangle();
        heroPos.setX(heroPos.getX()+dx);
        heroPos.setY(heroPos.getY()+dy);

        /*
         *  We only treat the rectangle form the COLLISION layer, need improvement if other collision
         *  shapes are added.
         */
        for (Rectangle rectangleObject : tiledMap.collisionBoxes) {

            Rectangle rectangle = rectangleObject;
            if (Intersector.overlaps(rectangle, heroPos)) {
                /* collision happened */
                return true;
            }
        }

        return  false;
    }

    /**
     * This method draws the hero in game accordingly to its movement.
     */
    public void draw (SpriteBatch sb){
        if (this.heroMovement()){ // Hero is moving
            elapsedTime += Gdx.graphics.getDeltaTime();
            sb.draw((TextureAtlas.AtlasRegion)this.walkingRightAnimation.getKeyFrame(elapsedTime,true),sprite.getX(),sprite.getY());
        } else { // Hero is not moving
            elapsedTime = 0f;
            sprite.draw(sb);
        }
    }

    /**
     * This method returns true if the hero is in the exit zone, false otherwise.
     */
    public boolean isInExitArea(){
        if(Intersector.overlaps(sprite.getBoundingRectangle(), tiledMap.exitArea)){
            return true;
        }
        return false;
    }


}
