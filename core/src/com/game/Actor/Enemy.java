package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.game.App;
import com.game.misc.utils.Box2dUtils;
import com.game.misc.Vars;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 22/02/2016.
 */
public class Enemy extends Base {

    private Sprite sprite;
    private boolean isAlive;

    public Enemy(World world, Vector2 pos, Vector2 size, Colours curColour, short categoryBits, short maskBits) {
        super(world, pos, size, curColour);

        body = Box2dUtils.makeBody(world,
                BodyDef.BodyType.StaticBody,
                pos
        );
        Box2dUtils.makePolygon(body, size, "ENEMY", true, categoryBits, maskBits);
        /*Box2dUtils.makeChain(body,
                new Vector2[]{
                        new Vector2((-size.x / 2 + 5) / PPM, (size.y / 2 + 5) / PPM),
                        new Vector2((size.x / 2 - 5) / PPM, (size.y / 2 + 5) / PPM)
                },
                "ENEMY",
                true,
                Vars.BIT_ALL,
                Vars.BIT_PLAYER
        );*/

        isAlive = true;

        sprite = new Sprite(App.assets.get("textures/enemies/redAlive.png", Texture.class));
        sprite.setPosition(pos.x - size.x / 2, pos.y - size.y / 2);
        setCurColour(curColour);
    }

    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
    }

    @Override
    public void setCurColour(Colours curColour)
    {
        super.setCurColour(curColour);
        Filter filter = body.getFixtureList().first().getFilterData();
        short bits = filter.maskBits;

        switch (curColour)
        {
            case RED:
                bits &= ~Vars.BIT_GREEN;
                bits &= ~Vars.BIT_BLUE;
                bits |= Vars.BIT_RED;
                if(isAlive) { sprite.setTexture(App.assets.get("textures/enemies/redAlive.png", Texture.class)); }
                else { sprite.setTexture(App.assets.get("textures/enemies/redDead.png", Texture.class)); }
                break;
            case GREEN:
                bits &= ~Vars.BIT_RED;
                bits &= ~Vars.BIT_BLUE;
                bits |= Vars.BIT_GREEN;
                if(isAlive) { sprite.setTexture(App.assets.get("textures/enemies/greenAlive.png", Texture.class)); }
                else { sprite.setTexture(App.assets.get("textures/enemies/greenDead.png", Texture.class)); }
                break;
            case BLUE:
                bits &= ~Vars.BIT_RED;
                bits &= ~Vars.BIT_GREEN;
                bits |= Vars.BIT_BLUE;
                if(isAlive) { sprite.setTexture(App.assets.get("textures/enemies/blueAlive.png", Texture.class)); }
                else { sprite.setTexture(App.assets.get("textures/enemies/blueDead.png", Texture.class)); }
                break;
        }

        filter.maskBits = bits;
        body.getFixtureList().first().setFilterData(filter);
    }

    public void setAlive(boolean isAlive) { this.isAlive = isAlive; }

    public boolean isAlive(){
        return isAlive;
    }

    public Array<Fixture> getFixtures() { return body.getFixtureList(); }
}
