package com.game.Managers;

import com.game.App;
import com.game.States.Menu;
import com.game.States.Play;
import com.game.States.State;

import java.util.HashMap;

/**
 * Created by Ash on 08/02/2016.
 */
public class StateManager {

    protected final App app;

    private HashMap<States, State> states = new HashMap<States, State>();

    private States currentState;
    public enum States
    {
        MENU,
        PAUSE,
        PLAY,
    }

    public StateManager(App app)
    {
        this.app = app;
        states.put(States.MENU, new Menu(this));
        states.put(States.PLAY, new Play(this));

        setState(States.MENU); // TODO, set to MENU
    }

    public void update(float dt)
    {
        states.get(currentState).update(dt);
    }

    public void render()
    {
        states.get(currentState).render();
    }

    public void handleInput()
    {
        states.get(currentState).handleInput();
    }

    public void dispose()
    {
        states.get(currentState).dispose();
    }

    // Accessors
    public App app() { return app; }

    // Mutators
    public void setState(States state)
    {
        currentState = state;
        System.out.println("Setting state: " + currentState.name());
        states.get(currentState).init();
    }
}
