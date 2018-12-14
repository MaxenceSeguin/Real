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

public class GameTitleScreen implements InputProcessor, Screen {

    private Game game;
    private SpriteBatch sb;
    private OrthographicCamera camera;
    private Image background, optionButton, playButton, credits, gmOff, gmOn, back, quit, seOff, seOn;
    private float delay;
    private GameSettings settings;

    public GameTitleScreen(Game aGame) {

        settings = new GameSettings();

        game = aGame;

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);
        camera.update();

        background = new Image(new Texture(Gdx.files.internal("TitleScreenImages/TitleScreenBackground.png")));
        optionButton = new Image(new Texture(Gdx.files.internal("OptionsButton.png")));
        optionButton.setPosition(centerX(optionButton), 680);
        playButton = new Image(new Texture(Gdx.files.internal("EnterButton.png")));
        playButton.setPosition(centerX(playButton), 800);
        credits = new Image(new Texture(Gdx.files.internal("TitleScreenImages/Credits.png")));
        gmOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicOFF.png")));
        gmOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicON.png")));
        back = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GoBack.png")));
        quit = new Image(new Texture(Gdx.files.internal("TitleScreenImages/QuitGame.png")));
        seOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsOFF.png")));
        seOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsON.png")));


    }

    public GameTitleScreen(Game aGame, GameSettings settings) {

        this.settings = settings;

        game = aGame;

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);
        camera.update();

        background = new Image(new Texture(Gdx.files.internal("TitleScreenImages/TitleScreenBackground.png")));
        optionButton = new Image(new Texture(Gdx.files.internal("OptionsButton.png")));
        optionButton.setPosition(centerX(optionButton), 680);
        playButton = new Image(new Texture(Gdx.files.internal("EnterButton.png")));
        playButton.setPosition(centerX(playButton), 800);
        credits = new Image(new Texture(Gdx.files.internal("TitleScreenImages/Credits.png")));
        gmOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicOFF.png")));
        gmOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicON.png")));
        back = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GoBack.png")));
        quit = new Image(new Texture(Gdx.files.internal("TitleScreenImages/QuitGame.png")));
        seOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsOFF.png")));
        seOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsON.png")));


    }

    @Override
    public void render(float delta) {
        delay+=1;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, 1);
        optionButton.draw(sb, 1);
        playButton.draw(sb, ((float)0.5 + (delay%50)/100));
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
        game.setScreen(new Beginning(game));
        //dispose();
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

        System.out.println(screenY + " " + screenX);

        float dx = camera.viewportWidth/Gdx.graphics.getWidth();
        float dy = camera.viewportHeight/Gdx.graphics.getHeight();

        if (clickedInside(screenX, screenY, optionButton)){
            dispose();
            game.setScreen(new OptionScreen(game, settings));
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
        sb.dispose();
    }

    float centerX(Image image){
        return 1920/2 - image.getWidth()/2;
    }
    float centerY(Image image){
        return 1080/2 - image.getHeight()/2;
    }
}
