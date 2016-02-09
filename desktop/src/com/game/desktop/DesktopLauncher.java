package com.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.PixmapIO;
import com.game.Game;
import com.game.Misc.Vars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), config);
		config.title = Vars.TITLE;
		config.width = Vars.SCREEN_WIDTH;
		config.height = Vars.SCREEN_HEIGHT;
		config.resizable = Vars.RESIZABLE;
		config.addIcon("spritesheets/icon.jpg", Files.FileType.Internal);
		config.foregroundFPS = (int)Vars.FRAMERATE;
	}
}
