package com.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.game.Managers.StateManager;

/**
 * Created by Ash on 08/02/2016.
 */
public class Menu extends State {


    public Menu(StateManager sm) {
        super(sm);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
        {
            sm.setState(StateManager.States.PLAY);
        }
    }

    @Override
    public void dispose() {

    }
}
