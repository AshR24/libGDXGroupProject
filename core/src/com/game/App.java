package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.Misc.Vars;
import com.game.Managers.StateManager;

public class App extends ApplicationAdapter {

	private float accum;

	// Batches
	private SpriteBatch sb;
	private ShapeRenderer sr;

	// Cameras
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	// Managers
	private StateManager sm;

	@Override
	public void create() {

		// Create batches
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		// Create Main + HUD cameras
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);

		// Create statemanager (Should always happen last)
		sm = new StateManager(this);
	}

	@Override
	public void resize (int width, int height) {

	}

	@Override
	public void render () {
		super.render();
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= Vars.STEP) {
			accum -= Vars.STEP;
			sm.handleInput();
			sm.update(Vars.STEP);
			sm.render();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }
	}

	@Override
	public void dispose () {
		sm.dispose();
		sb.dispose();
		sr.dispose();
	}

	public SpriteBatch getSpriteBatch() { return sb; }
	public ShapeRenderer getSr() { return sr; }
	public StateManager getSm() { return sm; }
	public OrthographicCamera getCam() { return cam; }
	public OrthographicCamera getHudCam() { return hudCam; }
}
