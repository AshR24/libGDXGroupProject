package com.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.App;
import com.game.misc.Vars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new App(), config);
		config.title = Vars.TITLE;
		config.width = Vars.SCREEN_WIDTH;
		config.height = Vars.SCREEN_HEIGHT;
		config.resizable = Vars.RESIZABLE;
		config.vSyncEnabled = Vars.VSYNC;
		config.addIcon("spritesheets/icon.jpg", Files.FileType.Internal);
		config.foregroundFPS = (int)Vars.FRAMERATE;
		config.backgroundFPS = (int)Vars.FRAMERATE;
	}
}
