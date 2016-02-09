package com.game.Misc;

import com.badlogic.gdx.math.Vector2;
import com.game.Actor.Object.Background;

/**
 * Created by Ash on 09/02/2016.
 */
public class ParralaxBackground {
    private Background back;
    private Background middle;
    private Background front;

    public ParralaxBackground(String back, String middle, String front)
    {
        this.back = new Background(back);
        createBackground(middle);
        createBackground(front);
    }

    public void update(float dt)
    {
        Vector2 movementAmount = new Vector2(.1f, 0);

        back.update(dt, movementAmount);

        middle.update(dt, movementAmount);

        front.update(dt, movementAmount);
    }

    private void createBackground(String path)
    {

    }
}
