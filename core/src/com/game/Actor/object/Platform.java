package com.game.actor.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.actor.Base;
import com.game.misc.utils.Box2dUtils;
import com.game.misc.Vars;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 08/02/2016.
 */
public class Platform extends Base {

    public Platform(World world, Vector2 pos, Vector2 size, Colours curColour, short categoryBits, short maskBits) {
        super(world, pos, size, curColour);

        body = Box2dUtils.makeBody(world,
                BodyDef.BodyType.StaticBody,
                pos
        );
        Box2dUtils.makePolygon(body, size, "", false, categoryBits, maskBits);
        Box2dUtils.makeChain(body,
                new Vector2[]{
                        new Vector2((-size.x / 2 + 5) / PPM, (size.y / 2 + 5) / PPM),
                        new Vector2((size.x / 2 - 5) / PPM, (size.y / 2 + 5) / PPM)
                },
                "PLATFORM",
                true,
                Vars.BIT_ALL,
                Vars.BIT_PLAYER
        );
    }
}
