package com.game.screens;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.App;
import com.game.managers.ScreenManager;

/**
 * Created by Ash on 11/02/2016.
 */
public class Loading extends AbstractScreen {

    private float percent;
    private Rectangle loadingRect;
    private Texture logo;

    public Loading(App app) {
        super(app);
    }

    @Override
    public void show() {
        super.show();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        app.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        app.assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        loadFont("fonts/badaboom.TTF", 60, Color.BLACK);

        app.assets.load("textures/player_red.png", Texture.class);
        app.assets.finishLoading(); // make sure player texture and font is loaded

        logo = app.assets.get("textures/player_red.png", Texture.class);
        loadingRect = new Rectangle(stage.getWidth() / 6f, (stage.getHeight() / 2f - 25), 0, 25);

        assetsToLoad();
    }

    @Override
    public void update(float dt) {
        percent = Interpolation.linear.apply(percent, app.assets.getProgress(), 0.3f);

        loadingRect.width = 0 + 853.3333f * percent;

        if (app.assets.update() && loadingRect.width >= 852f) { // continue to menu screen when all assets have loaded
            app.sm.setScreen(ScreenManager.Screen.MENU);
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        // Loading bar
        app.sr.begin(ShapeRenderer.ShapeType.Filled);
        app.sr.setColor(1, 0, 0, 1);
        app.sr.rect(loadingRect.x, loadingRect.y, loadingRect.width, loadingRect.height); // Red loading bar
        app.sr.set(ShapeRenderer.ShapeType.Line);
        app.sr.rect(loadingRect.x, loadingRect.y, 853.3333f, loadingRect.height); // Outline
        app.sr.end();

        app.sb.begin();
        app.assets.get("badaboom60.ttf", BitmapFont.class).draw(app.sb, "Loading", stage.getWidth() / 2 - 80, loadingRect.y + 80);
        app.sb.draw(logo, (stage.getWidth() / 2) - logo.getWidth() / 2, (stage.getHeight() / 1.5f) - logo.getHeight() / 2);
        app.sb.end();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        super.dispose();
        logo.dispose();
    }

    private void assetsToLoad()
    {
        loadFont("fonts/badaboom.TTF", 25, Color.BLACK);
        loadFont("fonts/badaboom.TTF", 30, Color.BLACK);
        loadFont("fonts/badaboom.TTF", 45, Color.BLACK);


        app.assets.load("textures/badlogic.jpg", Texture.class);
        app.assets.load("textures/menuBackground.jpg", Texture.class);
        app.assets.load("textures/leaderboardBackground.jpg", Texture.class);
        app.assets.load("textures/levelSelectBackground.jpg", Texture.class);
        app.assets.load("textures/pauseBackground.png", Texture.class);
        app.assets.load("textures/failureBackground.png", Texture.class);
        app.assets.load("textures/successBackground.png", Texture.class);
        app.assets.load("textures/level1Intro.png", Texture.class);
        app.assets.load("textures/level2Intro.png", Texture.class);
        app.assets.load("textures/pauseGlow.png", Texture.class);
        app.assets.load("textures/position0.png", Texture.class);
        app.assets.load("textures/position1.png", Texture.class);
        app.assets.load("textures/position2.png", Texture.class);

        app.assets.load("textures/player_green.png", Texture.class);
        app.assets.load("textures/player_blue.png", Texture.class);
        app.assets.load("textures/player_yellow.png", Texture.class);

        app.assets.load("sounds/jumping.mp3", Sound.class);
        app.assets.load("sounds/colourchange.mp3", Sound.class);

        app.assets.load("music/TheComplex.mp3", Music.class);
    }

    private void loadFont(String fontFileName, int size, Color borderColour)
    {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = fontFileName;
        params.fontParameters.size = size;
        params.fontParameters.borderWidth = 2;
        params.fontParameters.borderColor = borderColour;

        app.assets.load("badaboom" + size + ".ttf", BitmapFont.class, params);
    }
}
