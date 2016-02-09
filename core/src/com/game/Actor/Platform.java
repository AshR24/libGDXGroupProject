package com.game.Actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Misc.Box2dUtils;
import com.game.Misc.Vars;

import static com.game.Misc.Vars.PPM;
import static com.game.Misc.Vars.TILESIZE;

/**
 * Created by Ash on 08/02/2016.
 */
public class Platform extends Base {

    public Platform(World world, Vector2 pos, Colours curColour) {
        super(world, pos, new Vector2(TILESIZE, TILESIZE), "STATIC", curColour);

        body = Box2dUtils.makeBody(world,
                BodyDef.BodyType.StaticBody,
                pos
        );
        Box2dUtils.makePolygon(body, size, "", false);
        Box2dUtils.makeChain(body,
                new Vector2[]{
                        new Vector2((-TILESIZE / 2 + 5) / PPM, (TILESIZE / 2 + 5) / PPM),
                        new Vector2((TILESIZE / 2 - 5) / PPM, (TILESIZE / 2 + 5) / PPM)
                },
                "PLATFORM",
                true
        );
    }
}
