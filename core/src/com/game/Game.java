package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.Misc.Vars;
import com.game.States.StateManager;

public class Game extends ApplicationAdapter {

	private float accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private StateManager sm;

	@Override
	public void create() {
		sb = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		sm = new StateManager(this);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= Vars.STEP) {
			accum -= Vars.STEP;
			sm.handleInput();
			sm.update(Vars.STEP);
			sm.render();
		}
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		sm.dispose();
	}

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCam() { return cam; }
	public OrthographicCamera getHudCam() { return hudCam; }
}
