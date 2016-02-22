package com.game.misc;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ash on 08/02/2016.
 */
public class Vars {
    // Application related
    public static final String TITLE = "A Reckless Disregard for Mercy";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final boolean RESIZABLE = false;
    public static final boolean VSYNC = true;
    public static final float FRAMERATE = 60;

    // Physics related
    public static final float STEP =  1 / FRAMERATE;
    public static final Vector2 GRAVITY = new Vector2(0, -9.81f);
    public static final float PPM = 100f; // Pixels per meter
    //public static final Vector2 TILESIZE = new Vector2(64, 64);
    public static final Vector2 SCROLLSPEED = new Vector2(150f, 0);

    // Filter bits
    public static final short BIT_PLAYER = 1;
    public static final short BIT_PRISMATIC = 1;
    public static final short BIT_RED = 2;
    public static final short BIT_GREEN = 4;
    public static final short BIT_BLUE = 8;
    public static final short BIT_YELLOW = 16;
}
