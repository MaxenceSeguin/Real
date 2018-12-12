package com.mygdx.game.Maps;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.mygdx.game.GameTitleScreen;


public class Introduction implements Screen {

    VideoPlayer videoPlayer;
    Stage stage;
    boolean videoLoaded = false;

    Game game;

    public Introduction(Game agame){
        game = agame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        stage.getViewport().update(1920, 1080, true);

        videoPlayer = VideoPlayerCreator.createVideoPlayer(stage.getViewport());
        videoPlayer.setOnVideoSizeListener(new VideoPlayer.VideoSizeListener() {
            @Override
            public void onVideoSize(float v, float v2) {
                videoLoaded = true;
            }
        });

        stage.getViewport().getCamera().translate(-960, -540, 0);

        try {
            FileHandle fh = Gdx.files.internal("magenta.webm");
            Gdx.app.log("TEST", "Loading file : " + fh.file().getAbsolutePath());
            videoPlayer.play(fh);
        } catch (FileNotFoundException e) {
            Gdx.app.log("TEST", "Err: " + e);
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(videoLoaded) {
            if (!videoPlayer.render()) {
                game.setScreen(new GameTitleScreen(game));
            }
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}