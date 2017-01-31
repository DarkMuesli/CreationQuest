package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

	private static final String TAG = MyGdxGame.class.getName();

	InputMultiplexer inputMultiplexer;
	SpriteBatch spriteBatch;
	OrthographicCamera cam;
	Screen world;
	Screen dialog;

	float lag = 0;

	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		inputMultiplexer = new InputMultiplexer(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Keys.ESCAPE) {
					Gdx.app.log(TAG, "Spiel wurde durch ESC beendet.");
					Gdx.app.exit();
					return true;
				} else
					return false;
			}
		});

		Gdx.input.setInputProcessor(inputMultiplexer);

		EventManager.setGame(this);

		spriteBatch = new SpriteBatch();

		// Constructs a new OrthographicCamera, using the given viewport width
		// and height
		// Height is multiplied by aspect ratio.
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

		// Karte Laden
		world = new TiledWorld("overworld.tmx", spriteBatch, cam, this);

		// TODO: TEST-SCREEN
		dialog = new Screen() {

			@Override
			public void show() {
			}

			@Override
			public void resume() {
			}

			@Override
			public void resize(int width, int height) {
			}

			@Override
			public void render(float delta) {
				Gdx.gl20.glClearColor(1f, 0f, 0f, 0.5f);
				Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

				if (Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
					setToWorld();
			}

			@Override
			public void pause() {
			}

			@Override
			public void hide() {
			}

			@Override
			public void dispose() {
			}
		};
		setScreen(world);

	}

	public void setToWorld() {
		setScreen(world);

	}

	public void setToDialog() {
		setScreen(dialog);

	}

	public Screen getWorld() {
		return world;
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	@Override
	public void dispose() {
		super.dispose();
		spriteBatch.dispose();
		world.dispose();
		dialog.dispose();

	}

}
