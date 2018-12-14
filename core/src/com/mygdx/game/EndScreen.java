package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Maps.DungeonTransition1;
import com.mygdx.game.Maps.FuturisticTransition1;
import com.mygdx.game.Maps.JungleTransition1;

public class EndScreen implements InputProcessor, Screen {
    private OrthographicCamera camera;
    private Game game;
    private GameSettings settings;
    private SpriteBatch sb;
    private Image background, endOfGame, quit, retry;
    private int level;

    public EndScreen(Game agame){

        game = agame;

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);
        camera.update();

        background = new Image(new Texture(Gdx.files.internal("EndScreen/OptionScreenBackground1.png")));
        endOfGame = new Image(new Texture(Gdx.files.internal("EndScreen/theend.png")));
        quit = new Image(new Texture(Gdx.files.internal("EndScreen/QuitGame.png")));
        retry = new Image(new Texture(Gdx.files.internal("EndScreen/PlayAgainButton.png")));

        endOfGame.setPosition(centerX(endOfGame), 680);
        quit.setPosition(centerX(quit), 200);
        retry.setPosition(centerX(retry), 300);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, 1);
        endOfGame.draw(sb, 1);
        retry.draw(sb, 1);
        quit.draw(sb, 1);
        sb.end();
    }

    /**
     * Returns whether or not the coordinates of the click (passed as parameters) are inside the
     * image (passed as parameter) bounding rectangle.
     */
    private boolean clickedInside(int x , int y, Image image){
        if (x > image.getX() && x < image.getX() + image.getWidth()
                && y > image.getY() && y < image.getY() + image.getHeight()){
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
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
        screenX*=camera.viewportWidth/Gdx.graphics.getWidth();
        screenY*=camera.viewportHeight/Gdx.graphics.getHeight();
        screenY = (int)  camera.viewportHeight - screenY;

        if (clickedInside(screenX, screenY, retry)){
            game.setScreen(new DungeonTransition1(game, new GameSettings()));
        }
        if (clickedInside(screenX, screenY, quit)){
            Gdx.app.exit();;
        }
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

    @Override
    public void show() {

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

    float centerX(Image image){
        return 1920/2 - image.getWidth()/2;
    }
}
