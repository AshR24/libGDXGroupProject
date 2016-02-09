package com.game.States;

import com.game.Game;

import java.util.HashMap;

/**
 * Created by Ash on 08/02/2016.
 */
public class StateManager {
    private Game game;

    private HashMap<States, State> states = new HashMap<States, State>();

    private States currentState;
    public enum States
    {
        MENU,
        PAUSE,
        PLAY,
    }

    public StateManager(Game game)
    {
        this.game = game;
        states.put(States.MENU, new Menu(this));
        states.put(States.PLAY, new Play(this));

        setState(States.PLAY); // TODO, set to MENU
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
    public Game game() { return game; }

    // Mutators
    public void setState(States state)
    {
        currentState = state;
        System.out.println("Setting state: " + currentState.name());
        states.get(currentState).init();
    }
}
