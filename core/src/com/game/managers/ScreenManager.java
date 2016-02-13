package com.game.managers;

import com.game.App;
import com.game.screens.*;

import java.util.HashMap;

/**
 * Created by Ash on 12/02/2016.
 */
public class ScreenManager {

    private final App app;

    private HashMap<Screen, AbstractScreen> screens = new HashMap<Screen, AbstractScreen>();
    public enum Screen
    {
        LOADING,
        MENU,
        LEADERBOARD,
        LEVELSELECT,
        PLAY,
    }

    public ScreenManager(final App app)
    {
        this.app = app;
        initScreens();
    }

    private void initScreens()
    {
        screens.put(Screen.LOADING, new Loading(app));
        screens.put(Screen.MENU, new Menu(app));
        screens.put(Screen.LEADERBOARD, new Leaderboard(app));
        screens.put(Screen.LEVELSELECT, new LevelSelect(app));
    }

    public void setPlayScreen(int levelNumber)
    {

        // remove loaded level
        if(screens.get(Screen.PLAY) != null)
        {
            screens.get(Screen.PLAY).dispose();
            screens.remove(Screen.PLAY);
        }
        screens.put(Screen.PLAY, new Play(app, levelNumber));
        setScreen(Screen.PLAY);
    }

    public void setScreen(Screen s)
    {
        app.setScreen(screens.get(s));
    }

    public void dispose()
    {
        for(AbstractScreen s : screens.values())
        {
            if(s != null)
            {
                s.dispose();
            }
        }
    }
}
