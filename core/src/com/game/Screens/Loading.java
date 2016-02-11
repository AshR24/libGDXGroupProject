package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
        assets.load("textures/player.png", Texture.class);
        assets.finishLoading();

        logo = new Image(assets.get("textures/player.png", Texture.class));
        logo.setPosition((stage.getWidth() / 2) - logo.getWidth() / 2, (stage.getHeight() / 1.5f) - logo.getHeight() / 2);
        // 853.3333
        loadingRect = new Rectangle(stage.getWidth() / 6f, (stage.getHeight() / 2f), 0, 25);


        assets.load("textures/badlogic.jpg", Texture.class);
        assets.load("textures/position0.png", Texture.class);
        assets.load("textures/position1.png", Texture.class);
        assets.load("textures/position2.png", Texture.class);
    }

    @Override
    public void update(float dt) {
        percent = Interpolation.linear.apply(percent, assets.getProgress(), 0.1f);

        loadingRect.width = 0 + 853.3333f * percent;

        if (assets.update()) {
            if (Gdx.input.isTouched()) {
                app.setScreen(new Menu(app));
            }
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1, 0, 0, 1);
        sr.rect(loadingRect.x, loadingRect.y, loadingRect.width, loadingRect.height); // Red loading bar
        sr.end();

        sb.begin();
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
}
