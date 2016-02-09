package com.game.Actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Misc.Vars;
import javafx.scene.shape.Circle;

import static com.game.Misc.Vars.PPM;

/**
 * Created by Ash on 08/02/2016.
 */
public class Player extends Base {

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

        if(bodyType.equals("STATIC")) { bd.type = BodyDef.BodyType.StaticBody; } // Doesn't move, isn't affected by forces
        else if(bodyType.equals("KINEMATIC")) { bd.type = BodyDef.BodyType.KinematicBody; } // Can move, isn't affected by forces
        else { bd.type = BodyDef.BodyType.DynamicBody; } // Can move, is affected by forces
        bd.position.set(pos.x / PPM, pos.y / PPM);

        body = world.createBody(bd);
        //body.setFixedRotation(true);

        FixtureDef fd = new FixtureDef();
        //PolygonShape polygon = new PolygonShape();
        //polygon.setAsBox((size.x / 2) / PPM, (size.y / 2) / PPM);
        //fd.shape = polygon;

        CircleShape circle = new CircleShape();
        circle.setRadius((size.x / 2) / PPM);
        fd.shape = circle;

        fd.density = 1f;
        fd.restitution = 0f;
        fd.friction = 0.9f;

        body.createFixture(fd).setUserData("player");

        /*PolygonShape polygon = new PolygonShape();
        polygon.setAsBox((size.x / 4) / PPM, (size.y / 4) / PPM, new Vector2(0, (size.y / -3) / PPM), 0);
        fd.shape = polygon;
        fd.isSensor = true;
        body.createFixture(fd).setUserData("sensor");*/
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
    }

    public void jump()
    {
        if(curAction != Action.FALLING)
        {
            curAction = Action.JUMPING;
        }
    }

    public void moveLeft()
    {
        Vector2 vel = body.getLinearVelocity();
        vel.x = -5f;
        body.setLinearVelocity(vel);
    }

    public void moveRight()
    {
        Vector2 vel = body.getLinearVelocity();
        vel.x = 5f;
        body.setLinearVelocity(vel);
    }

    // Accessors
    public Action getCurAction() { return curAction; }

    // Mutators
    public void setAction(Action curAction) { this.curAction = curAction; }
}
