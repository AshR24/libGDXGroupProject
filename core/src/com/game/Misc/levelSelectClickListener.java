package com.game.misc;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.App;

/**
 * Created by Ash on 13/02/2016.
 */
public class levelSelectClickListener extends ClickListener {

    private App app;
    private int levelNumber;

    public levelSelectClickListener(App app, int levelNumber)
    {
        this.app = app;
        this.levelNumber = levelNumber;
    }

    @Override
    public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        app.sm.setPlayScreen(levelNumber);
    }
}
