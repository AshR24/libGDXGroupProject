package com.game.Actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Misc.Vars;

import static com.game.Misc.Vars.PPM;
import static com.game.Misc.Vars.TILESIZE;

/**
 * Created by Ash on 08/02/2016.
 */
public class Platform extends Base {

    public Platform(World world, Vector2 pos, Colours curColour) {
        super(world, pos, new Vector2(TILESIZE, TILESIZE), "STATIC", curColour);
    }

    @Override
    protected void makeBody() {
        bd = new BodyDef();

        if(bodyType.equals("STATIC")) { bd.type = BodyDef.BodyType.StaticBody; } // Doesn't move, isn't affected by forces
        else if(bodyType.equals("KINEMATIC")) { bd.type = BodyDef.BodyType.KinematicBody; } // Can move, isn't affected by forces
        else { bd.type = BodyDef.BodyType.DynamicBody; } // Can move, is affected by forces
        bd.position.set(pos.x / PPM, pos.y / PPM);

        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();

        /*ChainShape chain = new ChainShape();
        Vector2[] v = new Vector2[3];
        v[0] = new Vector2(
                -TILESIZE / 2 / PPM, -TILESIZE / 2 / PPM);
        v[1] = new Vector2(
                -TILESIZE / 2 / PPM, TILESIZE / 2 / PPM);
        v[2] = new Vector2(
                TILESIZE / 2 / PPM, TILESIZE / 2 / PPM);
        chain.createChain(v);
        fd.shape = chain;*/
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox((TILESIZE / 2) / PPM, (TILESIZE / 2) / PPM);
        fd.shape = polygon;

        fd.density = 1f;
        fd.restitution = 0f;
        fd.friction = 1f;

        body.createFixture(fd);
    }
}
