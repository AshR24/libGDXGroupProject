package com.game.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Ash on 08/02/2016.
 */
public abstract class Base {
    // Physics world reference
    protected World world;

    // Physics definitions
    protected Body body;

    // Position and Size
    protected Vector2 pos;
    protected Vector2 size;

    protected Colours curColour;
    public enum Colours
    {
        RED,
        GREEN,
        BLUE,
        WHITE
    }

    public Base(World world, Vector2 pos, Vector2 size, Colours curColour)
    {
        this.world = world;
        this.pos = pos;
        this.size = size;
        this.curColour = curColour;
    }

    // Accessors
    public Vector2 getPos() { return body.getPosition(); }
    public Vector2 getSize() { return size; }
    public Colours getCurColour() { return  curColour; }

    // Mutators
    public void setCurColour(Colours curColour) { this.curColour = curColour; }
}
