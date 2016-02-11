package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.App;

/**
 * Created by Ash on 11/02/2016.
 */
public class Menu extends AbstractScreen {

    private TextureAtlas atlas;
    private Skin skin;

    // Buttons
    private TextButton butPlay, butExit, butLeaderboard;

    // Font



    public Menu(App app) {
        super(app);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            app.setScreen(new Play(app));
        }
    }

    @Override
    public void render(float dt)
    {
        super.render(dt);
    }

    @Override
    public void handleInput() {

    }

}
