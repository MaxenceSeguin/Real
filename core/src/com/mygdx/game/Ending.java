package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Maps.DungeonTransition1;

import java.util.ArrayList;

public class Ending implements InputProcessor, Screen {
    private Game game;
    public ArrayList<Texture> story;
    private SpriteBatch sb;
    private OrthographicCamera camera;
    private int imageNumber = 0;

    private float elapsedTime;

    public Ending (Game agame){
        game = agame;

        Texture image;

        sb = new SpriteBatch();

        story = new ArrayList<Texture>();
        for (int i = 18; i < 42; i++){
            image = new Texture(Gdx.files.internal("End/" + i + ".png"));
            story.add(image);
        }



        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        try{
            for (int i = 0; i < 25; i++){
                if (i * 8 < elapsedTime && elapsedTime <= (i+1) * 8){
                    sb.draw(story.get(i), 0, 0);
                }
            }
        } catch (IndexOutOfBoundsException e){
            dispose();
            game.setScreen(new DungeonTransition1(game, new GameSettings()));
        }
        sb.end();

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

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER){
            System.out.println("enter");
            imageNumber++;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
