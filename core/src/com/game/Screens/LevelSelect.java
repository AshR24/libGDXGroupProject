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
import com.game.misc.levelSelectClickListener;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Ash on 11/02/2016.
 */
public class LevelSelect extends AbstractScreen {

    private Skin skin;

    // Buttons
    private ArrayList<TextButton> butLevels = new ArrayList<TextButton>();
    private TextButton butEndless, butBack;
    private Vector2 buttonSize;

    public LevelSelect(App app) {
        super(app);

        skin = new Skin();
        buttonSize = new Vector2(50, 50);
    }

    @Override
    public void show()
    {
        super.show();

        skin.add("default-font", app.assets.get("badaboom60.ttf", BitmapFont.class));
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
        app.sb.draw(app.assets.get("textures/backgrounds/levelSelectBackground.jpg", Texture.class), 0, 0);
        app.sb.end();

        stage.draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { app.sm.setScreen(ScreenManager.Screen.MENU); }
    }

    @Override
    public void dispose()
    {
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

        int tempCounter = 1;
        for(int row = 0; row < 2; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                TextButton butTemp = new TextButton(String.valueOf(tempCounter), skin, "default");
                butTemp.setPosition(500 + col * 60, 400 - row * 60);
                butTemp.setSize(buttonSize.x, buttonSize.y);
                butTemp.addListener(new levelSelectClickListener(app, tempCounter));

                tempCounter++;

                stage.addActor(butTemp);
                butLevels.add(butTemp);
            }
        }

        butEndless = new TextButton("Endless Mode", skin, "default");
        butEndless.setPosition((stage.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 120);
        butEndless.setSize(buttonSize.x, buttonSize.y);
        butEndless.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                //app.sm.setScreen(ScreenManager.Screen.ENDLESSMODE); TODO, finish this ;)
                butEndless.addAction(forever(moveBy(20, 3)));
            }
        });

        stage.addActor(butEndless);
        stage.addActor(butBack);
    }
}
