package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DungeonTransition1 implements Screen, InputProcessor {

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch sb;
    private Hero hero;
    private Sprite heroSprite;

    private Bridge[] bridges;
    private Teleporter[] teleporters;

    private Animation animation;
    private float elapsedTime = 0f;

    private Image image;
    private boolean draw;

    private Game game;


    public DungeonTransition1(Game aGame) {
        game = aGame;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        tiledMap = new TmxMapLoader().load("dungeon_corridor1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        hero = new Hero("hero1.png", 470, 979);
        heroSprite = hero.getSprite();

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("anim1.atlas"));
        animation = new Animation(1f/15f, textureAtlas.getRegions());

        image = new Image(new Texture(Gdx.files.internal("badlogic.jpg")));
        image.setPosition(300,400);

    }

    @Override
    public void show() {    }

    /**
     *  This methods renders the graphics and keep them updated.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        if (heroMovement()){
            elapsedTime += Gdx.graphics.getDeltaTime();
            sb.draw((TextureAtlas.AtlasRegion)animation.getKeyFrame(elapsedTime,true),heroSprite.getX(),heroSprite.getY());
        } else {
            heroSprite.draw(sb);
        }

        if (draw) {
            image.draw(sb, 1);
        }

        sb.end();

        nextStageListener();
    }

    private void updateCamera(){
        camera.position.x = Math.max(Math.min(heroSprite.getX(), 1024 - camera.viewportWidth/2), camera.viewportWidth/2);
        camera.position.y = Math.max(Math.min(heroSprite.getY(), 1024 - camera.viewportHeight/2), camera.viewportHeight/2);
        camera.update();
    }

    /**
     * This methods checks if the hero has to move.
     * If so it also checks if the hero will enter in collision with any object
     * in the COLLISION layer.
     * If not, then the hero will move and activate the moving state by returning true.
     */
    private boolean heroMovement(){
        int heroX = hero.getDx();
        int heroY = hero.getDy();

        if (hero.isMoving() && !isCollision(heroX, heroY) /*&& !isCrossingBrokenBridge(heroX, heroY)*/){
            heroSprite.setPosition(heroSprite.getX()+heroX, heroSprite.getY()+heroY);
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
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

        int objectLayerId = 2; /* The id of the COLLISION layer is 2 in our tilemap */
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();

        Rectangle heroPos = heroSprite.getBoundingRectangle();
        heroPos.setX(heroPos.getX()+dx);
        heroPos.setY(heroPos.getY()+dy);

        /*
        *  We only treat the rectangle form the COLLISION layer, need improvement if other collision
        *  shapes are added.
        */
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, heroPos)) {
                /* collision happened */
                return true;
            }
        }

        return  false;
    }

    private void nextStageListener(){
        Rectangle door = ((RectangleMapObject)tiledMap.getLayers().get(3).getObjects().get(0)).getRectangle();
        if(Intersector.overlaps(door, heroSprite.getBoundingRectangle())) {
            game.setScreen(new JungleBridge(game));
        }
    }

    /**
     * Called when a key is pressed.
     * @param keycode the key pressed
     */
    @Override public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            hero.setDx(-2);
        }
        if (keycode == Input.Keys.RIGHT) {
            hero.setDx(2);
        }
        if (keycode == Input.Keys.UP) {
            hero.setDy(2);
        }
        if (keycode == Input.Keys.DOWN) {
            hero.setDy(-2);
        }
        if (keycode == Input.Keys.D && hero.getIsOnTeleporter() != -1){
            teleporters[hero.getIsOnTeleporter()].teleportTo(heroSprite);
        }
        if (keycode == Input.Keys.L) {
            draw = true;
        }
        return false;
    }

    /**
     * Called when a key is released.
     * @param keycode the key released
     */

    @Override public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            hero.setDx(0);
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
            hero.setDy(0);
        }
        if (keycode == Input.Keys.L) {
            draw = false;
        }
        return false;
    }

    /**
     * Called when a key is typed.
     * @param character the character pressed
     */
    @Override public boolean keyTyped(char character) {

        return false;
    }

    /**
     * Called when a mouse button is pressed.
     */
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 position = camera.unproject(clickCoordinates);
        heroSprite.setPosition(position.x, position.y);
        System.out.println(position.x + " " + position.y);
        return true;
    }

    /**
     * Called when a mouse button is released.
     */
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    /**
     * Called when the mouse is dragged.
     */
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse is moved.
     */
    @Override public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse scroller is used.
     */
    @Override public boolean scrolled(int amount) {
        return false;
    }
}