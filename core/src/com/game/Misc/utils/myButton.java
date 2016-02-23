package com.game.misc.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Ash on 22/02/2016.
 */
public class myButton extends TextButton {

    public myButton(String text, Vector2 pos, Skin skin, String styleName, ClickListener cl) {
        super(text, skin, styleName);
        this.setPosition(pos.x, pos.y);
        this.setSize(50, 50);
        this.addListener(cl);
    }
}
