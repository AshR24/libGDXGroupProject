package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.App;
import com.game.managers.ScreenManager;

/**
 * Created by Ash on 11/02/2016.
 */
public class Leaderboard extends AbstractScreen {

    private Skin skin;

    // Buttons
    private Vector2 buttonSize;
    private TextButton butBack;


    public Leaderboard(App app) {
        super(app);

        skin = new Skin();
        buttonSize = new Vector2(50, 50);
    }

    @Override
    public void show() {
        super.show();

        skin.add("default-font", app.assets.get("badaboom45.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        initButtons();
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        app.sb.begin();
        app.sb.draw(app.assets.get("textures/backgrounds/leaderboardBackground.jpg", Texture.class), 0, 0);
        app.sb.end();

        stage.draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { app.sm.setScreen(ScreenManager.Screen.MENU); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void initButtons()
    {
        butBack = new TextButton("Back", skin, "default");
        butBack.setPosition(buttonSize.x, buttonSize.y / 2);
        butBack.setSize(buttonSize.x, buttonSize.y);
        butBack.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        });

        stage.addActor(butBack);
    }
}
