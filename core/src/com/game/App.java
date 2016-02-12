package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.Misc.Vars;
import com.game.Screens.Loading;
import com.game.Screens.Menu;

public class App extends Game {

	private float accum;

	// Batches
	private SpriteBatch sb;
	private ShapeRenderer sr;

	// Cameras
	//private OrthographicCamera cam;
	//private OrthographicCamera hudCam;

	// Managers
	private AssetManager assetManager;
	//private StateManager sm;

	@Override
	public void create() {
		assetManager = new AssetManager();

		// Create batches
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);

		// Create Main + HUD cameras
		//cam = new OrthographicCamera();
		//cam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		//hudCam = new OrthographicCamera();
		//hudCam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);

		this.setScreen(new Loading(this));
	}

	@Override
	public void render () {
		super.render();
		if(Gdx.input.isKeyPressed(Input.Keys.M))
		{
			this.setScreen(new Menu(this));
		}


		/*accum += Gdx.graphics.getDeltaTime();
		while (accum >= Vars.STEP) {
			accum -= Vars.STEP;
			sm.handleInput();
			sm.update(Vars.STEP);
			sm.render();
		}*/

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }
	}

	@Override
	public void dispose () {
		super.dispose();
		assetManager.dispose();
		sb.dispose();
		sr.dispose();
	}

	public SpriteBatch getSpriteBatch() { return sb; }
	public ShapeRenderer getSr() { return sr; }
	//public OrthographicCamera getCam() { return cam; }
	//public OrthographicCamera getHudCam() { return hudCam; }
	public AssetManager getAssetManager() { return assetManager; }
}
