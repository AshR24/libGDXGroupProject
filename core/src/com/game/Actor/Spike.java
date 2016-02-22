package com.game.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.misc.Box2dUtils;
import com.game.misc.Vars;

import static com.game.misc.Vars.PPM;

/**
 * Created by Elliot on 22/02/2016.
 */

public class Spike extends Base {

    public Spike(World world, Vector2 pos, Vector2 size, Colours curColour, short categoryBits) {
            super(world, pos, size, "STATIC", curColour);

            body = Box2dUtils.makeBody(world,
                    BodyDef.BodyType.StaticBody,
                    pos
            );

            short maskBits = 0;
            Box2dUtils.makePolygon(body, size, "", false, categoryBits, maskBits);
            Box2dUtils.makeChain(body,
                    new Vector2[]{
                            new Vector2((-size.x / 2 + 5) / PPM, (size.y / 2 + 5) / PPM),
                            new Vector2((size.x / 2 - 5) / PPM, (size.y / 2 + 5) / PPM)
                    },
                    "SPIKE",
                    true,
                    Vars.BIT_MISC,
                    Vars.BIT_PLAYER
            );
    }
}
