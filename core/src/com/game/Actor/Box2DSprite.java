package com.game.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 09/02/2016.
 */
public class Box2DSprite {

    protected Body body;
    protected Animation animation;

    protected float width, height;
    protected Vector2 pos;

    protected TextureRegion currentFrame;

    public Box2DSprite(Body body)
    {
        this.body = body;
        //setAnimation();
    }

    public void update(float dt)
    {
        pos = body.getPosition();
        currentFrame = animation.getKeyFrame(dt, true);
    }

    public void render(SpriteBatch sb)
    {
        sb.begin();
        sb.draw(currentFrame,
                (pos.x * PPM) - width / 2,
                (pos.y * PPM) - height / 2
        );
        sb.end();

    }

    /*public void setAnimation(TextureRegion[] reg, float delay)
    {
        setAnimation(reg, delay);
    }*/


}
