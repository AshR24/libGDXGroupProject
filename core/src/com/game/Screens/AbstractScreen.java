package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.App;
import com.game.misc.Vars;

/**
 * Created by Ash on 11/02/2016.
 */
public abstract class AbstractScreen implements Screen {

    // App reference
    protected App app;

    /*// Manager references
    protected AssetManager assets;

    // Batch references
    protected SpriteBatch sb;
    protected ShapeRenderer sr;*/



    // Cameras
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    // Stage
    protected Stage stage;

    public AbstractScreen(final App app)
    {
        this.app = app;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);

        this.stage = new Stage();
    }


    @Override
    public void show() {
        System.out.println("Showing screen: " + this.getClass().getSimpleName());
        Gdx.input.setInputProcessor(stage);
    }

    public abstract void update(float dt);

    @Override
    public void render(float dt) {
        handleInput();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.sb.setProjectionMatrix(cam.combined);
    }

    public abstract void handleInput();

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        System.out.println("Disposing screen: " + this.getClass().getSimpleName());
    }
}
