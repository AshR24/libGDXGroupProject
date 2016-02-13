package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.App;
import com.game.misc.Box2dUtils;
import com.game.misc.Vars;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 08/02/2016.
 */
public class Player extends Base {

    Vector2 curVel;

    private Texture texture;

    private Action curAction;
    public enum Action
    {
        IDLE,
        JUMPING,
        FALLING,
        DEAD
    }

    public Player(World world, Vector2 pos, Vector2 size, Colours curColour) {
        super(world, pos, size, "", curColour);
        curAction = Action.IDLE;

        body = Box2dUtils.makeBody(world,
                BodyDef.BodyType.DynamicBody,
                pos
        );
        Box2dUtils.makeCircle(body, size.x, "PLAYER", false, Vars.BIT_PLAYER, (short)(Vars.BIT_RED | Vars.BIT_MISC));

        texture = App.assets.get("textures/player_red.png");
    }

    public void update(float dt)
    {
        if(curAction == Action.JUMPING)
        {
            body.applyForceToCenter(new Vector2(0, 65), true);
            curAction = Action.FALLING;
        }

        curVel = body.getLinearVelocity();
        curVel.x = Vars.SCROLLSPEED.x * dt;
        body.setLinearVelocity(curVel);
        pos = body.getPosition();
    }

    public void render(SpriteBatch sb)
    {
        sb.draw(texture,
                (pos.x * PPM) - size.x / 2,
                (pos.y * PPM) - size.y / 2,
                size.x,
                size.y);
    }

    public void jump()
    {
        if(curAction != Action.FALLING)
        {
            curAction = Action.JUMPING;
        }
    }

    // Accessors
    public Action getCurAction() { return curAction; }

    // Mutators
    public void setAction(Action curAction) { this.curAction = curAction; }

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
                texture = App.assets.get("textures/player_red.png", Texture.class);
                break;
            case GREEN:
                bits &= ~Vars.BIT_RED;
                bits &= ~Vars.BIT_BLUE;
                bits |= Vars.BIT_GREEN;
                texture = App.assets.get("textures/player_green.png", Texture.class);
                break;
            case BLUE:
                bits &= ~Vars.BIT_RED;
                bits &= ~Vars.BIT_GREEN;
                bits |= Vars.BIT_BLUE;
                texture = App.assets.get("textures/player_blue.png", Texture.class);
                break;
        }

        filter.maskBits = bits;
        body.getFixtureList().first().setFilterData(filter);
    }
}
