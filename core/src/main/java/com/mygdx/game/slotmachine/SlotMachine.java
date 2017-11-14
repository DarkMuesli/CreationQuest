package com.mygdx.game.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

public class SlotMachine implements Screen {

	private enum State {
		STOPPED, MOVING, STARTING, STOPPING, STARTING_PENDING, STOPPING_PENDING, TILTED_UP, UNTILTING
	}

	private float lag;

	private State state;

	private float timer;
	private int reelIndex;

	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private GlyphLayout layout;
	private MyGdxGame game;
	private SlotMachineInputAdapter inpAd;
	private SlotMachineControllerAdapter contAd;

	private Reel[] reels;

	public SlotMachine(SpriteBatch spriteBatch, OrthographicCamera cam, MyGdxGame myGdxGame) {
		this.spriteBatch = spriteBatch;
		this.cam = cam;
		this.game = myGdxGame;
		this.inpAd = new SlotMachineInputAdapter(this);
		this.contAd = new SlotMachineControllerAdapter(this);

		FileHandle handle = Gdx.files.internal("text/VNA.txt");

		String text = handle.readString();
		String[] parts = text.split("###");

		//TODO: Magic number of reels... maybe rethink that
		// Maybe make it a param that has to be odd?
		this.reels = new Reel[3];
		for (int i = 0; i < reels.length; i++) {
			String[] part = parts[i].split("\\r?\\n");
			this.reels[i] = new Reel(part);
		}

		this.shapeRenderer = new ShapeRenderer();
		this.font = new BitmapFont();
		this.layout = new GlyphLayout(font, "DebugString");

		this.state = State.STOPPED;
		this.timer = 0;
		this.lag = 0;
		this.reelIndex = 0;
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
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		spriteBatch.setProjectionMatrix(cam.combined);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1f, 0f, 0f, 0.5f);
		shapeRenderer.line(0, cam.viewportHeight / 6, cam.viewportWidth, cam.viewportHeight / 6);
		shapeRenderer.line(0, (cam.viewportHeight / 6) * 5, cam.viewportWidth, (cam.viewportHeight / 6) * 5);
		shapeRenderer.rect(0, cam.viewportHeight / 2 - layout.height - 10, cam.viewportWidth, layout.height + 20);

		for (int i = 0; i < reels.length; i++) {
			Reel reel = reels[i];
			if (reel.isLocked()) {
				float xPos = (cam.viewportWidth / (reels.length + 1) * (i)
						+ (cam.viewportWidth / (reels.length + 1)) / 2);
				shapeRenderer.rect(xPos, 40, 10, 10);
			}
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		spriteBatch.begin();
		for (int i = 0; i < reels.length; i++) {
			Reel reel = reels[i];

			float xPos = (cam.viewportWidth / (reels.length + 1) * (i) + (cam.viewportWidth / (reels.length + 1)) / 2);
			float yBorder = cam.viewportHeight / 3;
			float yHalfBorder = yBorder / 2;
			float yHeight = cam.viewportHeight - yBorder;
			float yStart = yHeight / reel.ELEMENTS;
			float yEnd = (yHeight / reel.ELEMENTS) * 2;
			float yDistance = yEnd - yStart;
			float yPos = reel.getVertPosition() * yDistance + yHalfBorder + yStart;

			for (int j = 0; j < reel.ELEMENTS; j++) {
				if (reel.getCurrentWord() == reel.getWords().get(j))
					font.setColor(Color.WHITE);
				else
					font.setColor(Color.GRAY);

				font.draw(spriteBatch, reel.getWords().get(j), xPos, yPos + ((j - 1) * yDistance));
			}

		}

		font.draw(spriteBatch, state.name(), cam.viewportWidth / 2, 20);
		spriteBatch.end();
	}

	private void update(float deltaTime) {

		switch (state) {

		case STARTING:
			if ((timer += deltaTime) >= 0.1f) {
				startReel(reelIndex++);
				timer = 0;
				if (reelIndex >= reels.length) {
					state = State.STARTING_PENDING;
					reelIndex = 0;
				}
			}
			break;

		case STOPPING:
			if ((timer += deltaTime) >= 0.1f) {
				stopReel(reelIndex++);
				timer = 0;
				if (reelIndex >= reels.length) {
					state = State.STOPPING_PENDING;
					reelIndex = 0;
				}
			}
			break;

		case STARTING_PENDING:
			if (areReelsMoving())
				state = State.MOVING;
			break;

		case STOPPING_PENDING:
		case MOVING:
			if (areReelsStopped())
				state = State.STOPPED;
			break;

		case UNTILTING:
			if ((timer += deltaTime) > 0.1f)
				stopReels();
			break;

		case TILTED_UP:
		case STOPPED:
		}

		for (Reel reel : reels) {
			reel.update(deltaTime);
		}
	}

	void startReels() {
		if (state == State.TILTED_UP || state == State.UNTILTING) {
			timer = 0.1f;
			state = State.STARTING;
		}
	}

	void tiltReels() {
		if (state == State.STOPPED) {
			for (Reel reel : reels)
				reel.tilt();
			state = State.TILTED_UP;
		}
	}

	void stopReels() {
		if (state == State.MOVING || state == State.UNTILTING) {
			timer = 0.1f;
			state = State.STOPPING;
		}
	}

	void untilt() {
		if (state == State.TILTED_UP) {
			timer = 0f;
			state = State.UNTILTING;
		}
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
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(inpAd);

		Controllers.removeListener(contAd);
	}

	@Override
	public void dispose() {
		font.dispose();
		shapeRenderer.dispose();

	}

	private void stopReel(int i) {
		if (state == State.MOVING || state == State.STOPPING)
			reels[i].stop();
	}

	private void startReel(int i) {
		if (state == State.STARTING)
			reels[i].start();
	}

	private void toggleReelLock(int i) {
		if (state == State.STOPPED)
			reels[i].toggleLock();
	}

	void pushReelButton(int i) {
		if (state == State.STOPPED)
			toggleReelLock(i);
		else if (state == State.MOVING || state == State.STOPPING)
			reels[i].stop();

	}

	private boolean areReelsStopped() {
		for (Reel reel : reels) {
			if (!reel.isStopped())
				return false;
		}

		return true;
	}

	private boolean areReelsMoving() {
		for (Reel reel : reels) {
			if (!reel.isMoving() && !reel.isLocked())
				return false;
		}

		return true;
	}

}
