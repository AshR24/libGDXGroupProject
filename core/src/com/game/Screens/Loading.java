package com.game.Screens;

import com.badlogic.gdx.Gdx;
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

/**
 * Created by Ash on 11/02/2016.
 */
public class Loading extends AbstractScreen {

    private Image logo;

    private float percent;

    private Rectangle loadingRect;

    public Loading(App app) {
        super(app);
    }

    @Override
    public void show() {
        super.show();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        loadFont("fonts/badaboom.TTF", 60, Color.BLACK);

        assets.load("textures/player.png", Texture.class);
        assets.finishLoading(); // make sure player texture and font is loaded

        logo = new Image(assets.get("textures/player.png", Texture.class));
        logo.setPosition((stage.getWidth() / 2) - logo.getWidth() / 2, (stage.getHeight() / 1.5f) - logo.getHeight() / 2);
        loadingRect = new Rectangle(stage.getWidth() / 6f, (stage.getHeight() / 2f - 25), 0, 25);

        assetsToLoad();
    }

    @Override
    public void update(float dt) {
        percent = Interpolation.linear.apply(percent, assets.getProgress(), 0.3f);

        loadingRect.width = 0 + 853.3333f * percent;

        if (assets.update() && loadingRect.width >= 852f) { // continue to menu screen when all assets have loaded
            app.setScreen(new Menu(app));
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1, 0, 0, 1);
        sr.rect(loadingRect.x, loadingRect.y, loadingRect.width, loadingRect.height); // Red loading bar
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(loadingRect.x, loadingRect.y, 853.3333f, loadingRect.height); // Outline
        sr.end();

        sb.begin();
        assets.get("badaboom60.ttf", BitmapFont.class).draw(sb, "Loading", stage.getWidth() / 2 - 80, loadingRect.y + 80);
        logo.draw(sb, 1f);
        sb.end();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void assetsToLoad()
    {
        loadFont("fonts/badaboom.TTF", 25, Color.BLACK);
        loadFont("fonts/badaboom.TTF", 30, Color.BLACK);
        loadFont("fonts/badaboom.TTF", 45, Color.BLACK);


        assets.load("textures/badlogic.jpg", Texture.class);
        assets.load("textures/menuBackground.jpg", Texture.class);
        assets.load("textures/leaderboardBackground.jpg", Texture.class);
        assets.load("textures/position0.png", Texture.class);
        assets.load("textures/position1.png", Texture.class);
        assets.load("textures/position2.png", Texture.class);

        assets.load("sounds/jumping.mp3", Sound.class);

        assets.load("music/TheComplex.mp3", Music.class);
    }

    private void loadFont(String fontFileName, int size, Color borderColour)
    {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = fontFileName;
        params.fontParameters.size = size;
        params.fontParameters.borderWidth = 2;
        params.fontParameters.borderColor = borderColour;

        assets.load("badaboom" + size + ".ttf", BitmapFont.class, params);
    }
}
