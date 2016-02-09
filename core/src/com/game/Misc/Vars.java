package com.game.Misc;

import com.badlogic.gdx.math.Vector2;

import java.io.File;

/**
 * Created by Ash on 08/02/2016.
 */
public class Vars {
    // Application related
    public static final String TITLE = "Group Project";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final boolean RESIZABLE = false;
    public static final float FRAMERATE = 60;

    // Physics related
    public static final float STEP =  1 / FRAMERATE;
    public static final Vector2 GRAVITY = new Vector2(0, -9.81f);
    public static final float PPM = 100f; // Pixels per meter
    public static final float TILESIZE = 64f;
    public static final Vector2 SCROLLSPEED = new Vector2(150f, 0);

    // Filter bits
    public static final short BIT_RED = 2;
    public static final short BIT_GREEN = 4;
    public static final short BIT_BLUE = 8;
}
