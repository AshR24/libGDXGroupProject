package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.App;
import com.game.managers.ScreenManager;

/**
 * Created by Ash on 11/02/2016.
 */
public class Leaderboard extends AbstractScreen {

    private TextButton butBack;
    private Vector2 buttonSize;


    public Leaderboard(App app) {
        super(app);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(float dt) {
        super.render(dt);

        app.sb.begin();
        app.sb.draw(app.assets.get("textures/leaderboardBackground.jpg", Texture.class), 0, 0);
        app.assets.get("badaboom25.ttf", BitmapFont.class).draw(app.sb,"Press M to go back to menu",  100, 100);
        app.sb.end();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { app.sm.setScreen(ScreenManager.Screen.MENU); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
