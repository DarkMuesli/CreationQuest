package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch spriteBatch;
	OrthographicCamera cam;
	TiledWorld world;
	Player player;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();

		// Karte Laden
		world = new TiledWorld("bright.tmx");

		player = new Player(new Texture("tilesets/town_rpg_pack/town_rpg_pack/graphics/anims/walk-loop.png"), world);
		world.spawnPlayer(player, "");

		// Constructs a new OrthographicCamera, using the given viewport width
		// and height
		// Height is multiplied by aspect ratio.
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.setMapRendererView(cam);

		world.renderBackgroundLayers();

		spriteBatch.setProjectionMatrix(cam.combined);

		spriteBatch.begin();

		spriteBatch.draw(player.getSprt(), player.getPixelPosition().x, player.getPixelPosition().y,
				player.getWorld().getTileWidth(), player.getWorld().getTileHeight());

		spriteBatch.end();

		world.renderCollisionLayers();
		world.renderForegroundLayers();

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
				|| (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)))
			player.move(Direction.LEFT);
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
				|| (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)))
			player.move(Direction.RIGHT);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
				|| (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.SPACE)))
			player.move(Direction.UP);
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
				|| (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.SPACE)))
			player.move(Direction.DOWN);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();

		float camx, camy, camz = 0;

		if (world.getMapPixelWidth() >= cam.viewportWidth)
			camx = Math.min(Math.max(cam.viewportWidth / 2, player.getPixelCenter().x),
					world.getMapPixelWidth() - cam.viewportWidth / 2);
		else
			camx = world.getMapPixelWidth() / 2;

		if (world.getMapPixelHeight() >= cam.viewportHeight)
			camy = Math.min(Math.max(cam.viewportHeight / 2, player.getPixelCenter().y),
					world.getMapPixelHeight() - cam.viewportHeight / 2);
		else
			camy = world.getMapPixelHeight() / 2;

		cam.position.set(camx, camy, camz);

		cam.update();

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		world.dispose();
		player.dispose();
	}
}
