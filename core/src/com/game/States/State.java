package com.game.States;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.App;
import com.game.Managers.StateManager;

/**
 * Created by Ash on 08/02/2016.
 */
public abstract class State {

    // App reference
    protected final App app;

    // Batches
    protected SpriteBatch sb;
    protected ShapeRenderer sr;

    // Cameras
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    // Stage
    protected Stage stage;

    // Managers
    protected final StateManager sm;

    public State (StateManager sm)
    {
        this.sm = sm;
        this.app = sm.app();
        sb = app.getSpriteBatch();
        sr = app.getSr();
        cam = app.getCam();
        hudCam = app.getHudCam();
        stage = new Stage();
    }

    public abstract void init();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void handleInput();
    public abstract void dispose();

}
