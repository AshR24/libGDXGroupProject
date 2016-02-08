package com.game.States;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Ash on 08/02/2016.
 */
public class Play extends State {

    Texture img;

    public Play(StateManager sm) {
        super(sm);
    }

    @Override
    public void init() {
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(img, 0, 0);
        sb.end();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
