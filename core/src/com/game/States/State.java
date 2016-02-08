package com.game.States;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.Game;

/**
 * Created by Ash on 08/02/2016.
 */
public abstract class State {
    protected StateManager sm;
    protected Game game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    public State (StateManager sm)
    {
        this.sm = sm;
        game = sm.game();
        sb = game.getSpriteBatch();
        cam = game.getCam();
        hudCam = game.getHudCam();
    }

    public abstract void init();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void handleInput();
    public abstract void dispose();

}
