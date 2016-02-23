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

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Ash on 11/02/2016.
 */
public class Leaderboard extends AbstractScreen {

    private Skin skin;

    // Buttons
    private Vector2 buttonSize;
    private TextButton butBack;


    //Score
    private ArrayList<String> leaderboardValues = new ArrayList<>();

    public Leaderboard(App app) {
        super(app);

        skin = new Skin();
        buttonSize = new Vector2(50, 50);
    }

    @Override
    public void show() {
        super.show();
        leaderboardValues = new ArrayList<>();
        skin.add("default-font", app.assets.get("badaboom45.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        try {
            Scanner scanner = new Scanner(new File("Leaderboard.csv"));
            scanner.useDelimiter(",");
            while(scanner.hasNext()){
                leaderboardValues.add(scanner.next());
            }

            System.out.println(leaderboardValues);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        int count = 0;
        for(int col = 0; col < leaderboardValues.size()/2; col++){
            for(int row = 0; row < 2; row ++){
                app.assets.get("badaboom45.ttf", BitmapFont.class).draw(app.sb, leaderboardValues.get(count++), 100 + (400 * row), 500 + (-50 * col));
            }
        }

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
