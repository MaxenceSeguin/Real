package com.mygdx.game.Maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.GameInterface;
import com.mygdx.game.GameOrthoCamera;
import com.mygdx.game.GameOverScreen;
import com.mygdx.game.GameSettings;
import com.mygdx.game.Hero;
import com.mygdx.game.TiledMapPlus;

import java.awt.geom.Point2D;

public class DungeonMaze implements Screen, InputProcessor {

    private GameOrthoCamera camera;

    private TiledMapPlus tiledMap;

    private SpriteBatch sb;
    private Hero hero;

    private Game game;

    private GameInterface gameInterface;

    private GameSettings settings;

    private Image torch = new Image (new Texture(Gdx.files.internal("torch.png")));


    public DungeonMaze(Game aGame, GameSettings settings) {
        this.settings = settings;

        game = aGame;

        tiledMap = new TiledMapPlus("Darkmaze.tmx",null);

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        hero = settings.hero;
        hero.refresh(tiledMap);

        camera = new GameOrthoCamera(hero.getSprite(), tiledMap);

        gameInterface = new GameInterface(hero, sb, camera, tiledMap);

    }

    @Override
    public void show() {    }

    /**
     *  This methods renders the graphics and keep them updated.
     */
    @Override
    public void render(float delta) {

        colorManagement();

        camera.updateCamera();

        deathManagement();

        tiledMap.tiledMapRenderer.setView(camera);
        tiledMap.tiledMapRenderer.render();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        hero.draw(sb);

        drawLight();

        gameInterface.setLight(hero.light);

        gameInterface.refresh();

        lastLevelListener();
        nextLevelListener();

        sb.end();
    }


    void nextLevelListener(){
        if (hero.isInExitArea()) {
            settings.refresh(hero);
            game.setScreen(new DungeonTransition4(game, settings));
        }
    }

    void lastLevelListener(){
        if (hero.isInBackArea()){
            settings.refresh(hero);
            game.setScreen(new DungeonTransition3(game, settings));
        }
    }

    void drawLight(){
        if (hero.light == 1){
            torch.setPosition((float) camera.bottomLeftCorner.getX(),
                    (float) camera.bottomLeftCorner.getY());
        }
    }

    void colorManagement(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    void deathManagement(){
        if (hero.deathManager()){
            if (hero.health == 0){
                game.setScreen(new GameOverScreen(game, settings, 1));
            } else {
                game.setScreen(new DungeonMaze(game, settings));
            }
        }
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
        sb.dispose();
        tiledMap.tiledMapRenderer.dispose();
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
            tiledMap.teleporters[hero.getIsOnTeleporter()].teleportTo(hero.getSprite());
        }
        if (keycode == Input.Keys.L) {
            camera.rotate(12);
            //draw = true;
        }
        if (keycode == Input.Keys.R) {
            settings.refresh(hero);
            game.setScreen(new JungleBridge(game, settings));
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
        hero.getSprite().setPosition(position.x, position.y);
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
