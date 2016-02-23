package com.game.misc.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.ArrayList;

/**
 * Created by Ash on 22/02/2016.
 */
public class myWindow extends Window {

    protected ArrayList<TextButton> buttons = new ArrayList<TextButton>();

    public myWindow(String title, Vector2 pos, Vector2 size, Skin skin, Texture backgroundTexture) {
        super(title, skin);
        setBackground(new Image(backgroundTexture).getDrawable());
        setPosition(pos.x, pos.y);
        setSize(size.x, size.y);
        setVisible(false);
    }

    public void addButton(TextButton newButton)
    {
        buttons.add(newButton);
        this.addActor(newButton);
    }
}
