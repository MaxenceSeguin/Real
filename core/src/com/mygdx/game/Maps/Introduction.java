package com.mygdx.game.Maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameTitleScreen;

import java.io.FileNotFoundException;

public class Introduction /*implements InputProcessor, Screen */{

    /*public PerspectiveCamera cam;
    public CameraInputController inputController;
    public ModelInstance instance;
    public Environment environment;

    public VideoPlayer videoPlayer;
    public Mesh mesh;

    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 target = new Vector3();

    private Game game;

    public Introduction(Game agame) {
        game = agame;
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        MeshBuilder meshBuilder = new MeshBuilder();
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, GL20.GL_TRIANGLES);
        // @formatter:off
        meshBuilder.box(5, 5, 5);
        // @formatter:on
        mesh = meshBuilder.end();
        videoPlayer = VideoPlayerCreator.createVideoPlayer(cam, mesh, GL20.GL_TRIANGLES);
        try {
            videoPlayer.play(Gdx.files.internal("drop.avi"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gdx.input.setInputProcessor(new InputMultiplexer(this, inputController = new CameraInputController(cam)));
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        inputController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        tmpV1.set(cam.direction).crs(cam.up).y = 0f;
        cam.rotateAround(target, tmpV1.nor(), delta * 20);
        cam.rotateAround(target, Vector3.Y, delta * -30);
        cam.update();

        if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            game.setScreen(new GameTitleScreen(game));
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
    }*/
}
