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
import com.mygdx.game.Maps.TestScreen;


public class OptionScreen implements Screen, InputProcessor {

    private Game game;
    private SpriteBatch sb;
    private OrthographicCamera camera;
    private Image background, optionButton, playButton, credits, gmOff, gmOn, back, quit, seOff, seOn, optionBackground;
    private GameSettings settings;

    public OptionScreen(Game aGame, GameSettings settings) {
        this.settings = settings;

        game = aGame;

        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);
        camera.update();

        background = new Image(new Texture(Gdx.files.internal("TitleScreenImages/TitleScreenBackground.png")));
        optionBackground = new Image(new Texture(Gdx.files.internal("TitleScreenImages/OptionScreenBackground.png")));

        optionButton = new Image(new Texture(Gdx.files.internal("OptionsButton.png")));
        optionButton.setPosition(centerX(optionButton), 680);

        playButton = new Image(new Texture(Gdx.files.internal("EnterButton.png")));
        playButton.setPosition(centerX(playButton), 900);

        credits = new Image(new Texture(Gdx.files.internal("TitleScreenImages/Credits.png")));
        credits.setPosition(centerX(credits), 500);

        gmOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicOFF.png")));
        gmOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GameMusicON.png")));
        gmOn.setPosition(centerX(gmOn), 600);
        gmOff.setPosition(centerX(gmOn), 600);

        back = new Image(new Texture(Gdx.files.internal("TitleScreenImages/GoBack.png")));
        back.setPosition(centerX(back), 200);

        quit = new Image(new Texture(Gdx.files.internal("TitleScreenImages/QuitGame.png")));
        quit.setPosition(centerX(quit), 400);

        seOff = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsOFF.png")));
        seOn = new Image(new Texture(Gdx.files.internal("TitleScreenImages/SoundEffectsON.png")));
        seOn.setPosition(centerX(seOn), 800);
        seOff.setPosition(centerX(seOn), 800);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        optionBackground.draw(sb, 1);

        drawSoundEffects();
        drawGameMusic();

        back.draw(sb, 1);
        quit.draw(sb, 1);
        credits.draw(sb, 1);

        sb.end();
    }

    /**
     * Display the image of Sound Effects option according to the settings.
     */
    private void drawSoundEffects(){
        if(settings.soundEffects){
            seOn.draw(sb, 1);
        } else {
            seOff.draw(sb, 1);
        }
    }

    /**
     * Display the image of Game Music option according to the settings.
     */
    private void drawGameMusic(){
        if(settings.gameMusic){
            gmOn.draw(sb, 1);
        } else {
            gmOff.draw(sb, 1);
        }
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

    /**
     * Return the abscissa that the image should be displayed at so that it is centered on the
     * screen.
     */
    float centerX(Image image){
        return 1920/2 - image.getWidth()/2;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.setScreen(new TestScreen(game, settings));
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

        float dx = 1920/Gdx.graphics.getWidth();
        float dy = 1080/Gdx.graphics.getHeight();

        if (clickedInside(screenX, screenY, back)){
            game.setScreen(new GameTitleScreen(game, settings));
        } else if (clickedInside(screenX, screenY, credits)) {
            //show credits
        } else if (clickedInside(screenX, screenY, gmOn) || clickedInside(screenX, screenY, gmOff)){
            settings.gameMusic = !settings.gameMusic;
        } else if (clickedInside(screenX, screenY, seOn) || clickedInside(screenX, screenY, seOff)){
            settings.soundEffects = !settings.soundEffects;
        } else if (clickedInside(screenX, screenY, quit)){
            Gdx.app.exit();
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
}
