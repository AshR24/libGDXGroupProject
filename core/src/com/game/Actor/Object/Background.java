package com.game.actor.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.misc.Vars;

/**
 * Created by Ash on 09/02/2016.
 */
public class Background {

    private Vector2 pos;

    private Texture texture;

    public Background(String path)
    {
        loadTexture(path);
    }

    public void update(float dt, Vector2 pos)
    {
        this.pos = pos;
    }

    public void render(SpriteBatch sb)
    {
        sb.draw(texture, pos.x, pos.y, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
    }

    private void loadTexture(String path)
    {
        this.texture = new Texture(path);
    }
}
