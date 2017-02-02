package com.mygdx.game.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

public class SlotMachine implements Screen {

	private float lag;

	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;
	private MyGdxGame game;
	private SlotMachineInputAdapter inpAd;
	private SlotMachineControllerAdapter contAd;

	public SlotMachine(SpriteBatch spriteBatch, OrthographicCamera cam, MyGdxGame myGdxGame) {
		this.spriteBatch = spriteBatch;
		this.cam = cam;
		this.setGame(myGdxGame);
		inpAd = new SlotMachineInputAdapter(this);
		contAd = new SlotMachineControllerAdapter(this);
	}

	public MyGdxGame getGame() {
		return game;
	}

	public void setGame(MyGdxGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(inpAd);
		else
			Gdx.input.setInputProcessor(inpAd);

		Controllers.addListener(contAd);
	}

	private void draw(SpriteBatch spriteBatch, OrthographicCamera cam) {

	}

	private void update(float deltaTime) {

	}

	@Override
	public void render(float delta) {
		lag += delta;

		while (lag >= Constants.MS_PER_UPDATE) {
			update(Constants.MS_PER_UPDATE);
			lag -= Constants.MS_PER_UPDATE;
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		draw(spriteBatch, cam);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(inpAd);

		Controllers.removeListener(contAd);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
