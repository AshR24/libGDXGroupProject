package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.game.App;

/**
 * Created by Ash on 11/02/2016.
 */
public class Splash extends AbstractScreen {

    public Splash(App app) {
        super(app);
    }

    @Override
    public void update(float dt) {

        stage.act(dt);
    }

    @Override
    public void render(float dt)
    {
        super.render(dt);

        stage.draw();
    }


}
