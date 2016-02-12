package com.game.misc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ash on 10/02/2016.
 */
public class CameraUtils {

    public static void lockOnTarget(Camera cam, float tarX, float tarY)
    {
        Vector3 pos = cam.position;
        pos.x = tarX;
        pos.y = tarY;
        cam.position.set(pos);
        cam.update();
    }

    public static void lerpToTarget(Camera cam, float tarX, float tarY)
    {
        Vector3 pos = cam.position;
        pos.x = cam.position.x + (tarX - cam.position.x) * .2f;
        pos.y = cam.position.y + (tarY - cam.position.y) * .2f;;
        cam.position.set(pos);
        cam.update();
    }

    public static void setBoundary(Camera cam, Vector2 start, Vector2 size)
    {
        Vector3 pos = cam.position;
        if(pos.x < start.x)
        {
            pos.x = start.x;
        }
        if(pos.y < start.y)
        {
            pos.y = start.y;
        }

        if(pos.x > start.x + size.x)
        {
            pos.x = start.x + size.x;
        }

        if(pos.y > start.y + size.y)
        {
            pos.y = start.y + size.y;
        }
        cam.position.set(pos);
        cam.update();
    }
}
