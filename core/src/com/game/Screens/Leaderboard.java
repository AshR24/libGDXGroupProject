package com.game.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.App;

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

        sb.begin();
        sb.draw(assets.get("textures/leaderboardBackground.jpg", Texture.class), 0, 0);
        assets.get("badaboom25.ttf", BitmapFont.class).draw(sb,"Press M to go back to menu",  100, 100);
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
