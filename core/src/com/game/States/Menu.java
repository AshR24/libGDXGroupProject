package com.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.Managers.StateManager;

import java.awt.event.InputEvent;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Ash on 08/02/2016.
 */
public class Menu extends State {

    Random rand = new Random();
    int threshhold = 200;
    long lastChanged = 0;

    private Image tempImage;

    private TextureAtlas atlas;

    private Skin skin;
    private Table table;

    private TextButton butPlay, butExit, butLeaderboard;
    public BitmapFont font64;
    public FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/badaboom.TTF"));
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private Label heading;


    public Menu(StateManager sm) {
        super(sm);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void init() {

        atlas = new TextureAtlas("spritesheets/uiskin.atlas");
        skin = new Skin(atlas);

        parameter.size = 64;
        parameter.borderWidth = 2f;
        parameter.borderColor = Color.WHITE;
        parameter.color = Color.BLACK;
        font64 = generator.generateFont(parameter);

        skin.add("default-font", font64);
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        initButtons();
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
        //genRandColour();

    }

    private void genRandColour()
    {
        if(System.currentTimeMillis() - lastChanged < threshhold) { return; }
        font64.setColor(new Color(
                rand.nextFloat() / 2f + 0.5f,
                rand.nextFloat() / 2f + 0.5f,
                rand.nextFloat() / 2f + 0.5f,
                1f
        ));
        lastChanged = System.currentTimeMillis();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);

        stage.draw();

        sb.begin();
        //font12.draw(sb, "Main menu", 200, 200);
        sb.end();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            sm.setState(StateManager.States.PLAY);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void initButtons()
    {
        butPlay = new TextButton("Play", skin, "default");
        butPlay.setPosition(500, 260);
        butPlay.setSize(128, 40);
        butPlay.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                sm.setState(StateManager.States.PLAY);
            }
        });

        butExit = new TextButton("Exit", skin, "default");
        butExit.setPosition(500, 110);
        butExit.setSize(128, 40);
        butExit.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        butPlay.addAction(parallel(alpha(0), moveTo(stage.getWidth() / 2, stage.getHeight() / 2, 5f, Interpolation.pow5), sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow2))));
        butExit.addAction(sequence(alpha(0f), fadeIn(2f, Interpolation.pow2)));

        stage.addActor(butPlay);
        stage.addActor(butExit);
    }
}
