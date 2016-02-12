package com.game.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 09/02/2016.
 */
public class Box2dUtils {

    private static float density = 1f;
    private static float friction = 0.9f;

    public static Body makeBody(World world, BodyDef.BodyType bodyType, Vector2 pos)
    {
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(pos.x / PPM,
                pos.y / PPM
        );
        return world.createBody(bd);
    }

    public static void makePolygon(Body body, Vector2 size, String userData, boolean isSensor)
    {
        FixtureDef fd = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((size.x / 2) / PPM,
                (size.y / 2) / PPM
        );
        fd.shape = shape;
        fd.density = density;
        fd.friction = friction;
        fd.isSensor = isSensor;

        if(userData.equals("")) { body.createFixture(fd); }
        else { body.createFixture(fd).setUserData(userData); }
    }

    public static void makeCircle(Body body, float diameter, String userData, boolean isSensor)
    {
        FixtureDef fd = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius((diameter / 2) / PPM);
        fd.shape = shape;
        fd.density = density;
        fd.friction = friction;
        fd.isSensor = isSensor;

        if(userData.equals("")) { body.createFixture(fd); }
        else { body.createFixture(fd).setUserData(userData); }
    }

    public static void makeChain(Body body, Vector2[] v, String userData, boolean isSensor)
    {
        FixtureDef fd = new FixtureDef();

        ChainShape shape = new ChainShape();
        shape.createChain(v);
        fd.shape = shape;
        fd.density = density;
        fd.friction = friction;
        fd.isSensor = isSensor;

        if(userData.equals("")) { body.createFixture(fd); }
        else { body.createFixture(fd).setUserData(userData); }
    }


}
