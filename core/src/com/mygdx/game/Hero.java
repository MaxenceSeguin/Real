package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Hero extends Image {
    private int dx, dy;
    public Sprite sprite;
    private boolean isOnBridge=false;
    private int isOnTeleporter = -1;
    private TiledMapPlus tiledMap;
    public Animation walkingRightAnimation;
    public Animation walkingLeftAnimation;
    public Animation walkingUpAnimation;
    public Animation walkingDownAnimation;
    private float elapsedTime;
    public int health;
    public boolean isOnPlatform = false;
    public boolean playerBlocked =false;
    public int light = 0;
    public int machete = 0;

    public Hero(String path, TiledMapPlus tiledmap, int health,
                String filePathRight, String filePathLeft, String filePathUp, String filePathDown){
        this.tiledMap = tiledmap;
        Texture texture = new Texture(Gdx.files.internal(path));
        this.sprite = new Sprite(texture);
        sprite.setPosition(tiledmap.spawnX, tiledmap.spawnY);
        TextureAtlas textureAtlasRight = new TextureAtlas(Gdx.files.internal(filePathRight));
        TextureAtlas textureAtlasLeft = new TextureAtlas(Gdx.files.internal(filePathLeft));
        TextureAtlas textureAtlasUp = new TextureAtlas(Gdx.files.internal(filePathUp));
        TextureAtlas textureAtlasDown = new TextureAtlas(Gdx.files.internal(filePathDown));
        this.walkingRightAnimation = new Animation(1f/15f, textureAtlasRight.getRegions());
        this.walkingLeftAnimation = new Animation(1f/15f, textureAtlasLeft.getRegions());
        this.walkingUpAnimation = new Animation(1f/15f, textureAtlasUp.getRegions());
        this.walkingDownAnimation = new Animation(1f/15f, textureAtlasDown.getRegions());
        this.health = health;

    }

    public void refresh(TiledMapPlus tiledmap){
        this.tiledMap = tiledmap;
        sprite.setPosition(tiledmap.spawnX, tiledmap.spawnY);
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

        if (this.isMoving() && !isCollision(heroX, heroY) && !isCrossingBrokenBridge(heroX, heroY) &&
                isAttemptingPush()){
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

    private boolean isCollision(Rectangle rock){

        rock.setPosition(rock.getX(), rock.getY()-rock.height);

        /*
         *  We only treat the rectangle form the COLLISION layer, need improvement if other collision
         *  shapes are added.
         */
        for (Rectangle rectangleObject : tiledMap.collisionBoxes) {

            Rectangle rectangle = rectangleObject;
            if (Intersector.overlaps(rectangle, rock)) {
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
            if (dx < 0){
                sb.draw((TextureAtlas.AtlasRegion)this.walkingLeftAnimation.getKeyFrame(elapsedTime,
                        true),sprite.getX(),sprite.getY());
            } else if (dx > 0){
                sb.draw((TextureAtlas.AtlasRegion)this.walkingRightAnimation.getKeyFrame(elapsedTime,
                        true),sprite.getX(),sprite.getY());
            } else if (dy < 0){
                sb.draw((TextureAtlas.AtlasRegion)this.walkingDownAnimation.getKeyFrame(elapsedTime,
                        true),sprite.getX(),sprite.getY());
            } else {
                sb.draw((TextureAtlas.AtlasRegion)this.walkingUpAnimation.getKeyFrame(elapsedTime,
                        true),sprite.getX(),sprite.getY());
            }
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

    public boolean isInBackArea(){
        if(Intersector.overlaps(sprite.getBoundingRectangle(), tiledMap.backArea)){
            return true;
        }
        return false;
    }

    /**
     * Automates the movement of platforms and hero if he's on a platform
     */
    public void platformBusiness(){

        for (int i = 0; i < tiledMap.getNumberOfPlatforms(); i++) {
            if (tiledMap.platformMovementArray[i]) {
                if (tiledMap.platformSpriteArray[i].getX() <= tiledMap.platformRight[i]) {
                    tiledMap.platformSpriteArray[i].setPosition(tiledMap.platformSpriteArray[i].getX() + 1, tiledMap.platformSpriteArray[i].getY());
                } else {
                    tiledMap.platformMovementArray[i] = false;
                }
            }
            else {
                if (tiledMap.platformSpriteArray[i].getX() >= tiledMap.platformLeft[i]) {
                    tiledMap.platformSpriteArray[i].setPosition(tiledMap.platformSpriteArray[i].getX() - 1, tiledMap.platformSpriteArray[i].getY());
                } else {
                    tiledMap.platformMovementArray[i] = true;
                }
            }
            Vector2 heroPosition = new Vector2(this.sprite.getX() + this.sprite.getWidth()/2,
                    this.sprite.getY() + this.sprite.getHeight()/5);
            for (int j = 0; j < tiledMap.getNumberOfPlatforms(); j++) {
                if (tiledMap.platformSpriteArray[j].getBoundingRectangle().contains(heroPosition)) {
                    tiledMap.isOnPlatformArray[j] = true;
                } else {
                    tiledMap.isOnPlatformArray[j] = false;
                }
            }
            if (tiledMap.isOnPlatformArray[i]) {
                if ((tiledMap.platformMovementArray[i] && this.sprite.getX() <= (tiledMap.platformRight[i] + tiledMap.platformSpriteArray[i].getWidth()))) {
                    this.sprite.setPosition(this.sprite.getX() + 1, this.sprite.getY());
                }
                else if (!tiledMap.platformMovementArray[i] && this.sprite.getX() >= tiledMap.platformLeft[i] - tiledMap.platformSpriteArray[i].getWidth()) {
                    this.sprite.setPosition(this.sprite.getX() - 1, this.sprite.getY());
                }
            }
        }
        for (int k = 0; k < tiledMap.getNumberOfPlatforms(); k++){
            if(tiledMap.isOnPlatformArray[k]){
                isOnPlatform = true;
                return;
            }
        }
        isOnPlatform = false;
    }

    /**
     * Check if the hero is in a 'lossing health' state.
     * Checks whether the hero is on a 'death' box, or if he is on a 'water' box without not being
     * on a platform.
     * Return whether he is in a dying state.
     */
    public boolean checkDeath(){

        try {
            for (Rectangle object : tiledMap.water){
                if (Intersector.overlaps(sprite.getBoundingRectangle(), object) && !isOnPlatform){
                    return true;
                }
            }
        } catch (NullPointerException e){}

        try {
            for (Rectangle object : tiledMap.deathBoxes){
                if (Intersector.overlaps(sprite.getBoundingRectangle(), object)){
                    return true;
                }
            }
        } catch (NullPointerException e){}
        return false;
    }

    /**
     * If the hero is in a dying state, he loses 1 health.
     * Return whether he died.
     */
    public boolean deathManager(){
        if (checkDeath()){
            health -= 1;
            return true;
        }
        return false;
    }


    /**
     * This method deals with movable objects and what
     * should occur when an attempt is made to push one
     *
     */
    private boolean isAttemptingPush(){
        if (tiledMap.rocks == null){
            return true;
        }
        int numRocks = 9;
        boolean overlapping = false;
        boolean blocked = false;
        Rectangle heroPos = this.sprite.getBoundingRectangle(); //Gets the bounding rectangle of the hero
        heroPos.setX(heroPos.getX()+this.getDx()); //Sets the next step for the hero (x direction)
        heroPos.setY(heroPos.getY()+this.getDy()); //Sets the next step for the hero (y direction)

        //Loop over all rocks
        for(int i=0;i<numRocks;i++) {
            Rectangle objPos = tiledMap.rockSprite[i].getBoundingRectangle(); //Gets the bounding rectangle of the rock (/object)
            if (Intersector.overlaps(heroPos, objPos)) {
                Rectangle intersect = new Rectangle();
                Intersector.intersectRectangles(heroPos,objPos,intersect);

                //Push from left
                if(intersect.x > heroPos.x && this.getDx() > 0){
                    //Check for any rocks blocking a push
                    for(int j=0;j<numRocks;j++){
                        if(i!=j && tiledMap.rockSprite[i].getX() == tiledMap.rockSprite[j].getX() - tiledMap.rockSprite[j].getWidth()
                                && tiledMap.rockSprite[i].getY() == tiledMap.rockSprite[j].getY()){
                            blocked = true;
                            this.setDx(0);
                        }
                    }
                    if(blocked && !isCollision(objPos)){
                        tiledMap.rockSprite[i].setPosition(tiledMap.rockSprite[i].getX()+tiledMap.rockSprite[i].getWidth(),
                                tiledMap.rockSprite[i].getY());
                        playerBlocked = false;
                    }
                }

                //Push from right
                else if(intersect.x + intersect.width < heroPos.x + heroPos.width && this.getDx() < 0){
                    //Check for any rocks blocking a push
                    for(int j=0;j<numRocks;j++){
                        if(i!=j && tiledMap.rockSprite[i].getX() == tiledMap.rockSprite[j].getX() + tiledMap.rockSprite[j].getWidth()
                                && tiledMap.rockSprite[i].getY() == tiledMap.rockSprite[j].getY()){
                            blocked = true;
                            this.setDx(0);
                        }
                    }
                    if(blocked == false){
                        tiledMap.rockSprite[i].setPosition(tiledMap.rockSprite[i].getX()-tiledMap.rockSprite[i].getWidth(),
                                tiledMap.rockSprite[i].getY());
                        playerBlocked = false;
                    }
                }

                //Push from bottom
                else if(intersect.y > heroPos.y && this.getDy() > 0){
                    //Check for any rocks blocking a push
                    for(int j=0;j<numRocks;j++){
                        if(i!=j && tiledMap.rockSprite[i].getY() == tiledMap.rockSprite[j].getY() - tiledMap.rockSprite[j].getHeight()
                                && tiledMap.rockSprite[i].getX() == tiledMap.rockSprite[j].getX()){
                            blocked = true;
                            this.setDy(0);
                        }
                    }
                    if(blocked == false){
                        tiledMap.rockSprite[i].setPosition(tiledMap.rockSprite[i].getX(),
                                tiledMap.rockSprite[i].getY()+tiledMap.rockSprite[i].getHeight());
                        playerBlocked = false;
                    }
                }

                //Push from top
                else if(intersect.y + intersect.height < heroPos.y + heroPos.height && this.getDy() < 0){
                    //Check for any rocks blocking a push
                    for(int j=0;j<numRocks;j++){
                        if(i!=j && tiledMap.rockSprite[i].getY() == tiledMap.rockSprite[j].getY() + tiledMap.rockSprite[j].getHeight()
                                && tiledMap.rockSprite[i].getX() == tiledMap.rockSprite[j].getX()){
                            blocked = true;
                            this.setDy(0);
                        }
                    }
                    if(blocked == false){
                        tiledMap.rockSprite[i].setPosition(tiledMap.rockSprite[i].getX(),
                                tiledMap.rockSprite[i].getY()-tiledMap.rockSprite[i].getHeight());
                        playerBlocked = false;
                    }

                }
                blocked = false;
                overlapping = true;
            }
        }
        return true;
    }


    /**
     * Getters and setters.
     */

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

    public boolean isOnBridge() {
        return isOnBridge;
    }

    public void setOnBridge(boolean onBridge) {
        isOnBridge = onBridge;
    }

}
