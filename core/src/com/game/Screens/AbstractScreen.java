package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.App;

/**
 * Created by Ash on 11/02/2016.
 */
public abstract class AbstractScreen implements Screen {

    // App reference
    protected App app;

    // Batches
    protected SpriteBatch sb;
    protected ShapeRenderer sr;

    // Cameras
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    // Managers
    protected AssetManager assets;

    // Stage
    protected Stage stage;

    public AbstractScreen(final App app)
    {
        this.app = app;
        sb = app.getSpriteBatch();
        sr = app.getSr();
        cam = app.getCam();
        hudCam = app.getHudCam();
        assets = app.getAssetManager();
        stage = new Stage();
    }


    @Override
    public void show() {

    }

    public abstract void update(float dt);

    @Override
    public void render(float dt) {
        handleInput();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public abstract void handleInput();

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
        stage.dispose();
    }
}
