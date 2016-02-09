package com.game.Actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Misc.Vars;

import static com.game.Misc.Vars.PPM;

/**
 * Created by Ash on 08/02/2016.
 */
public class Player extends Base {

    // TODO, remove
    private Texture texture = new Texture("textures/player.png");

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
    }

    @Override
    public void makeBody() {
        bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(pos.x / PPM, pos.y / PPM);

        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();

        CircleShape circle = new CircleShape();
        circle.setRadius((size.x / 2) / PPM);
        fd.shape = circle;

        fd.density = 1f;
        fd.friction = 0.9f;

        body.createFixture(fd).setUserData("PLAYER");
    }

    public void update(float dt)
    {
        if(curAction == Action.JUMPING)
        {
            body.applyForceToCenter(new Vector2(0, 65), true);
            curAction = Action.FALLING;
        }

        Vector2 curVel = body.getLinearVelocity();
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
}
