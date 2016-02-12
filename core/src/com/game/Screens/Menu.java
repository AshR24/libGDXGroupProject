package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.App;
import com.game.managers.ScreenManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Ash on 11/02/2016.
 */
public class Menu extends AbstractScreen {

    private Skin skin;

    // Buttons
    private TextButton butPlay, butExit, butLeaderboard;
    private Vector2 buttonSize;

    private Music music;

    public Menu(App app) {
        super(app);
        skin = new Skin();

        buttonSize = new Vector2(128, 40);
    }

    @Override
    public void show() {
        super.show();

        skin.add("default-font", app.assets.get("badaboom60.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        music = app.assets.get("music/TheComplex.mp3", Music.class);
        music.setLooping(true);

        initButtons();
        music.play();
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

    }

    @Override
    public void render(float dt)
    {
        super.render(dt);

        app.sb.begin();
        app.sb.draw(app.assets.get("textures/menuBackground.jpg", Texture.class), 0, 0);
        app.sb.end();

        stage.draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void initButtons()
    {
        butPlay = new TextButton("Play", skin, "default");
        butPlay.setPosition((stage.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 180);
        butPlay.setSize(buttonSize.x, buttonSize.y);
        butPlay.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                music.stop();
                app.sm.setPlayScreen(1);
            }
        });

        butLeaderboard = new TextButton("Leaderboard", skin, "default");
        butLeaderboard.setPosition((stage.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 100);
        butLeaderboard.setSize(buttonSize.x, buttonSize.y);
        butLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.LEADERBOARD);
            }
        });

        butExit = new TextButton("Exit", skin, "default");
        butExit.setPosition((stage.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 20);
        butExit.setSize(buttonSize.x, buttonSize.y);
        butExit.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        butPlay.addAction(sequence(alpha(0f), fadeIn(1f, Interpolation.pow2)));
        butLeaderboard.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow2)));
        butExit.addAction(sequence(alpha(0f), fadeIn(2f, Interpolation.pow2)));

        stage.addActor(butPlay);
        stage.addActor(butLeaderboard);
        stage.addActor(butExit);
    }

}
