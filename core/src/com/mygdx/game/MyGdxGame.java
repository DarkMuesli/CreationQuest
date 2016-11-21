package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch spriteBatch;
	OrthographicCamera cam;
	TiledWorld world;
	Player player;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();

		// Karte Laden
		world = new TiledWorld(new TmxMapLoader().load("bright.tmx"));

		player = new Player(new Texture("tilesets/town_rpg_pack/town_rpg_pack/graphics/anims/walk-loop.gif"), world);

		// Constructs a new OrthographicCamera, using the given viewport width
		// and height
		// Height is multiplied by aspect ratio.
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);

		// Ab hier nur noch Test-Ausgaben

		// TiledMapTileLayer tmtl = (TiledMapTileLayer) tm.getLayers().get(0);

		// TiledMapTileLayer tmtl1 = (TiledMapTileLayer) tm.getLayers().get(1);
		//
		// System.out.println("Layer Count:\t" + tm.getLayers().getCount());
		//
		// System.out.println("Layer 0: " + tmtl.getName());
		// System.out.println("Layer 1: " + tmtl1.getName());
		//
		// System.out.println("Layer 0, Cell 0 0 Tile ID :" + tmtl.getCell(0,
		// 0).getTile().getId());
		// System.out.println("Layer 0, Cell 1 1 Tile ID :" + tmtl.getCell(1,
		// 1).getTile().getId());
		//
		// System.out.println(sp.getWidth());
		// System.out.println(img.getWidth());
		//
		// System.out.println(tm.getLayers().get(5).getObjects().get(1).getName());
		//
		// Iterator<String> it =
		// tm.getLayers().get(5).getObjects().get(1).getProperties().getKeys();
		// while (it.hasNext())
		// System.out.println(it.next());
		//
		// System.out.println();
		//
		// Iterator<Object> it1 =
		// tm.getLayers().get(5).getObjects().get(1).getProperties().getValues();
		// while (it1.hasNext())
		// System.out.println(it1.next());
		//
		// try {
		// System.out.println("Layer 1, Cell 0 0 Tile ID :" + tmtl1.getCell(0,
		// 0).toString());
		// } catch (NullPointerException e) {
		// System.out.println("Layer 1, Cell 0 0 Tile ID NULLPOINTER MESSAGE :"
		// + e.getMessage());
		// }
		//
		// try {
		// System.out.println("Layer 1, Cell 1 1 Tile ID :" + tmtl1.getCell(1,
		// 1).toString());
		// } catch (NullPointerException e) {
		// System.out.println("Layer 1, Cell 1 1 Tile ID NULLPOINTER MESSAGE :"
		// + e.getMessage());
		// }
		//
		// Array<TiledMapTileLayer> muh =
		// tm.getLayers().getByType(TiledMapTileLayer.class);
		// for (TiledMapTileLayer kup : muh) {
		// System.out.println(kup.getName());
		// }
		// System.out.println();
		// MapLayers mau = tm.getLayers();
		// for (MapLayer mapLayer : mau) {
		// System.out.println(mapLayer.getName());
		// }
		//
		// System.out.println();
		//
		// System.out.println(world.getTileHeight());
		//
		// System.out.println();
		// System.out.println(world.getCollisionTileLayers().size());
		// for (MapLayer mapLayer : world.getCollisionTileLayers()) {
		// System.out.println(mapLayer.getName());
		// }
		//
		// System.out.println();
		// System.out.println(world.getBgTileLayers().size());
		// for (MapLayer mapLayer : world.getBgTileLayers()) {
		// System.out.println(mapLayer.getName());
		// }
		//
		// System.out.println();
		// System.out.println(world.getFgTileLayers().size());
		// for (MapLayer mapLayer : world.getFgTileLayers()) {
		// System.out.println(mapLayer.getName());
		// }
		// System.out.println();
		//
		// System.out.println(world.getTileLayers().size());
		// for (MapLayer mapLayer : world.getTileLayers()) {
		// System.out.println(mapLayer.getName());
		// }
		//
		// System.out.println();
		//
		// Iterator<String> its = tm.getProperties().getKeys();
		// Iterator<Object> itv = tm.getProperties().getValues();
		//
		// while (its.hasNext()) {
		// String key = (String) its.next();
		// System.out.println(key + ": " + itv.next().toString());
		// }

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
				player.getMap().getTileWidth(), player.getMap().getTileHeight());

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

		cam.position.set(
				Math.min(Math.max(cam.viewportWidth / 2, player.getPixelPosition().x + (world.getTileWidth() / 2)),
						world.getMapPixelWidth() - cam.viewportWidth / 2),
				Math.min(Math.max(cam.viewportHeight / 2, player.getPixelPosition().y + (world.getTileHeight() / 2)),
						world.getMapPixelHeight() - cam.viewportHeight / 2),
				0);
		cam.update();

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		world.dispose();
		player.dispose();
	}
}
