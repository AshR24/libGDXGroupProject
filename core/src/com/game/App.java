package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.managers.ScreenManager;
import com.game.screens.Menu;

public class App extends Game {

	// Managers
	public AssetManager assets;
	public ScreenManager sm;


	// Batches
	public SpriteBatch sb;
	public ShapeRenderer sr;

	// Cameras
	//private OrthographicCamera cam;
	//private OrthographicCamera hudCam;



	@Override
	public void create() {
		// Create managers
		assets = new AssetManager();
		sm = new ScreenManager(this);

		// Create batches
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);


		sm.setScreen(ScreenManager.Screen.LOADING);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		sb.dispose();
		sr.dispose();
		assets.dispose();
		sm.dispose();
	}

	public SpriteBatch getSpriteBatch() { return sb; }
	public ShapeRenderer getSr() { return sr; }
	public AssetManager getAssetManager() { return assets; }
}
