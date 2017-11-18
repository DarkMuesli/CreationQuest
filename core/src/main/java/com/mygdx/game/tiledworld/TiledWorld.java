package com.mygdx.game.tiledworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Constants;
import com.mygdx.game.EventManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.utils.CoordinateHelper;

/**
 * Class, representing the whole world, the game takes place in. Contains a
 * reference to the current {@link TiledMap} and a lot of additional
 * information.
 * 
 * @author Matthias Gross
 *
 */

public class TiledWorld extends ScreenAdapter implements Observer{

	private static final String TAG = TiledWorld.class.getName();

	private MyGdxGame game;

	private float lag;
	private TiledMap map;

	private WorldProperties mapProp;

	private CollisionDetector collisionDetector;

	private GameObjectProvider gameObjectProvider;

	private String mapName;
	private String newMap;
	private Player player;

	private SpriteBatch spriteBatch;

	private OrthographicCamera cam;

	private List<GameObject> newGameObjectList;
	private List<SimpleTextDrawer> texts;

	private MapRenderer mapRenderer;

	private ShapeRenderer shapeRenderer;
	private float fadeCounter;

	private boolean fadingOut;
	private boolean fadingIn;
	private float fadeTime;

	private TiledWorldData tiledWorldData;

	/**
	 * Instantiates a new game world, using the {@link TiledMap} in the
	 * specified location. Only necessary once per application run.
	 *
	 * @param mapName
	 */
	public TiledWorld(String mapName, SpriteBatch spriteBatch, OrthographicCamera cam, MyGdxGame game) {
		this.newGameObjectList = new ArrayList<>();
		this.texts = new ArrayList<>();

		this.cam = cam;
		this.spriteBatch = spriteBatch;
		this.game = game;

		this.fadeCounter = 0;
		this.fadeTime = 0;
		this.shapeRenderer = new ShapeRenderer();

		gameObjectProvider = new GameObjectProvider(this);

		setMap(mapName);

		this.player = gameObjectProvider.returnPlayer();

		//TODO Delete line?
		this.newGameObjectList.addAll(tiledWorldData.getGameObjectList());

		// update(0);

		resetCam(cam);

	}

	public MyGdxGame getGame() {
		return game;
	}

	public void setGame(MyGdxGame game) {
		this.game = game;
	}

	public List<GameObject> getGameObjectList() {
		return tiledWorldData.getGameObjectList();
	}

	public List<MapObject> getLoadingZoneObjects() {
		return tiledWorldData.getLoadingZoneObjects();
	}

	public OrthographicCamera getCam() {
		return cam;
	}


	public WorldProperties getMapProp() {
		return mapProp;
	}

	public GameObjectProvider getGameObjectProvider() {
		return gameObjectProvider;
	}

	/**
	 * Checks, if there is anything to collide with at a given cell based
	 * position.
	 *
	 * @param p
	 *            cell based position to check as a {@link Point}
	 * @return <code>true</code> if there is something to collide with at the
	 *         given point, <code>false</code> otherwise
	 */
	boolean checkCollision(Point p) {

		if (p.x < 0 || p.x >= this.mapProp.getWidthInTiles() || p.y < 0 || p.y >= mapProp.getWidthInTiles()) {
			return true;
		}

		for (TiledMapTileLayer layer : tiledWorldData.getCollisionTileLayers()) {
			if (layer.getCell(p.x, p.y) != null) {
				return true;
			}
		}

		for (GameObject obj : tiledWorldData.getGameObjectList()) {
			if (obj.getCellPosition().equals(p) && !obj.isPenetrable())
				return true;
		}

		return false;
	}

	/**
	 * Checks, if there is anything to collide with at a given cell based
	 * position.
	 *
	 * @param x
	 *            cell based x coordinate
	 * @param y
	 *            cell based y coordinate
	 * @return <code>true</code> if there is something to collide with at the
	 *         given point, <code>false</code> otherwise
	 */
	boolean checkCollision(int x, int y) {
		return checkCollision(new Point(x, y));
	}

	/**
	 * Loads a new map into the {@link TiledWorld}. Plenty of stuff happening
	 * here.
	 * 
	 * @param mapName
	 *            path of the map to load, relative to the assets directory.
	 */
	private void setMap(String mapName) {

		if (this.map != null)
			this.map.dispose();

		this.map = new TmxMapLoader().load(mapName);
		this.mapName = mapName;

		mapProp = new WorldProperties(map.getProperties());

//		collisionDetector = new CollisionDetector(map.getProperties());

		tiledWorldData = new TiledWorldData(map.getLayers(), gameObjectProvider);

		mapRenderer = new OrthogonalTiledMapRenderer(this.map);
	}

	/**
	 * Render the whole map at once.
	 */
	void renderMap() {
		mapRenderer.render();
	}

	/**
	 * Render only the background {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "bgr". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	void renderBackgroundLayers() {
		mapRenderer.render(tiledWorldData.getBgTileLayersIndices().stream().mapToInt(i -> i).toArray());
	}

	/**
	 * Render only the collision {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "col". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	void renderCollisionLayers() {
		mapRenderer.render(tiledWorldData.getCollisionTileLayersIndices().stream().mapToInt(i -> i).toArray());
	}

	/**
	 * Render only the foreground {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "fgr". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	void renderForegroundLayers() {
		mapRenderer.render(tiledWorldData.getFgTileLayersIndices().stream().mapToInt(i -> i).toArray());
	}

	/**
	 * Spawn the given Player on the map. If there's no spawn point for the
	 * given old map, the default spawn point will be used. If there's no
	 * default spawn, the cell 0,0 will be used instead.
	 * 
	 * @param player
	 *            {@link Player} to spawn
	 * @param oldMap
	 *            the map, the player is coming from. Empty string or
	 *            <code>null</code> causes default spawn.
	 */
	void spawnPlayer(Player player, String oldMap) {

		MapObject defaultSpawn = null;

		for (MapObject spwnObject : tiledWorldData.getPlayerSpawnObjects()) {
			MapProperties spwnObjProp = spwnObject.getProperties();

			if (spwnObjProp.get("From", String.class).equals(oldMap)) {
				player.setCellPosition(CoordinateHelper.getCellFromPixel(spwnObjProp.get("x", float.class),
						spwnObjProp.get("y", float.class), mapProp.getTileWidth(), mapProp.getTileHeight()));
				return;
			}

			if (spwnObjProp.get("From", String.class).equals("Default"))
				defaultSpawn = spwnObject;
		}

		if (defaultSpawn != null)
			player.setCellPosition(CoordinateHelper.getCellFromPixel(defaultSpawn.getProperties().get("x", float.class),
					defaultSpawn.getProperties().get("y", float.class), mapProp.getTileWidth(), mapProp.getTileHeight()));
		else
			player.setCellPosition(0, 0);

		player.reset();
	}

	@Override
	public void update(Observable subj, Object arg) {
		if (subj instanceof Player) {
			Player player = (Player) subj;
			PlayerEvents event = (PlayerEvents) arg;

			switch (event) {

			case PLAYER_MOVED:

				for (MapObject obj : getLoadingZoneObjects()) {
					MapProperties objProp = obj.getProperties();

					Rectangle objPixelPos = new Rectangle(objProp.get("x", float.class), objProp.get("y", float.class),
							objProp.get("width", float.class), objProp.get("height", float.class));

					if (objPixelPos.contains(player.getPixelCenter())) {

						Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/door.wav"));
						long id = sound.play();
						sound.setPitch(id, 1f + ((float) Math.random() * 0.1f) - 0.05f);

						newMap = objProp.get("nextMap", String.class);

						fadeOut(0.2f);

					}
				}

				EventManager.instance().checkTrigger(tiledWorldData.getTriggerObjects(), player);

				break;
			}

		}

	}

	private void changeMap(String newMap) {
		String oldMap = mapName;
		if (oldMap.equals("farm.tmx")) {
			EventManager.instance().farmLeft(player);
		}

		texts.clear();

		newGameObjectList.clear();
//		newGameObjectList.add(gameObjectProvider.returnPlayer());

		setMap(newMap);

		newGameObjectList.addAll(tiledWorldData.getGameObjectList());
		if (!newGameObjectList.contains(gameObjectProvider.returnPlayer()))
			newGameObjectList.add(gameObjectProvider.returnPlayer());

		Gdx.app.log(TAG, "Neue Karte geladen: " + mapName);
		if (mapName.equals("farm.tmx")) {
			EventManager.instance().farmEntered(player);
		}

		spawnPlayer(player, oldMap);
//		player.updateSize(getTileWidth(), getTileHeight() / getTileWidth() * getTileHeight());
		player.updateSize(mapProp.getTileWidth(), mapProp.getTileHeight() / mapProp.getTileWidth() * mapProp.getTileHeight());

		player.reset();
		player.waitFor(0.2f);

		resetCam(cam);
	}

	void draw(SpriteBatch spriteBatch, OrthographicCamera cam) {

		updateCam(cam);

		spriteBatch.setProjectionMatrix(cam.combined);
		mapRenderer.setView(cam);

		renderBackgroundLayers();
		renderCollisionLayers();

		// TODO: NIX GUT SO
//		 spriteBatch.begin();
		tiledWorldData.getGameObjectList().forEach(obj -> obj.draw(spriteBatch));
//		 spriteBatch.end();

		renderForegroundLayers();

		SimpleTextDrawer.beginShapes();
		texts.forEach(SimpleTextDrawer::drawBackground);
		SimpleTextDrawer.endShapes();


		spriteBatch.begin();
		texts.forEach(text -> text.drawText(spriteBatch));
		spriteBatch.end();

		if (fadingOut || fadingIn) {

			Color color;
			if (fadingOut)
				color = new Color(0, 0, 0, fadeCounter / fadeTime);
			else
				color = new Color(0, 0, 0, 1 - fadeCounter / fadeTime);

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(color);
			shapeRenderer.rect(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2,
					cam.viewportWidth, cam.viewportHeight);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

	}

	void updateCam(OrthographicCamera cam) {
		float camx, camy, camz = 0;

		if (mapProp.getWidthInPixels() >= cam.viewportWidth)
			camx = Math.min(Math.max(cam.viewportWidth / 2, player.getSprt().getX() - player.getSprt().getWidth() / 2),
					mapProp.getWidthInPixels() - cam.viewportWidth / 2);
		else
			camx = mapProp.getWidthInPixels() / 2;

		if (mapProp.getHeightInPixels() >= cam.viewportHeight)
			camy = Math.min(
					Math.max(cam.viewportHeight / 2, player.getSprt().getY() - player.getSprt().getHeight() / 2),
					mapProp.getHeightInPixels() - cam.viewportHeight / 2);
		else
			camy = mapProp.getHeightInPixels() / 2;

		cam.position.set(lerp(cam.position.x, camx, 0.1f), lerp(cam.position.y, camy, 0.1f), camz);

		cam.update();
	}

	// TODO Codedopplung zu updateCam vermeiden
	void resetCam(OrthographicCamera cam) {
		float camx, camy, camz = 0;

		if (mapProp.getWidthInPixels() >= cam.viewportWidth)
			camx = Math.min(Math.max(cam.viewportWidth / 2, player.getSprt().getX() - player.getSprt().getWidth() / 2),
					mapProp.getWidthInPixels() - cam.viewportWidth / 2);
		else
			camx = mapProp.getWidthInPixels() / 2;

		if (mapProp.getHeightInPixels() >= cam.viewportHeight)
			camy = Math.min(
					Math.max(cam.viewportHeight / 2, player.getSprt().getY() - player.getSprt().getHeight() / 2),
					mapProp.getHeightInPixels() - cam.viewportHeight / 2);
		else
			camy = mapProp.getHeightInPixels() / 2;

		cam.position.set(camx, camy, camz);

		cam.update();
	}

	void update(float deltaTime) {

		newGameObjectList.clear();
		newGameObjectList.addAll(tiledWorldData.getGameObjectList());

		tiledWorldData.getGameObjectList().forEach(obj -> obj.update(deltaTime));

		if (fadingOut || fadingIn) {
			fadeCounter += deltaTime;
			if (fadeCounter >= fadeTime) {
				fadeCounter = 0;
				if (fadingOut) {
					changeMap(newMap);
					fadeIn(fadeTime);
				} else
					fadingIn = false;
			}
		}

		List<GameObject> tmp = tiledWorldData.getGameObjectList();
		tiledWorldData.setGameObjectList(newGameObjectList);
		newGameObjectList = tmp;

		tiledWorldData.getGameObjectList().sort((o1, o2) -> Integer.compare(o2.getCellPosition().y, o1.getCellPosition().y));

		for (Iterator<SimpleTextDrawer> iterator = texts.iterator(); iterator.hasNext();) {
			SimpleTextDrawer drawer = iterator.next();
			if (drawer.isDead())
				iterator.remove();
			else
				drawer.update(deltaTime);
		}

	}

	@Override
	public void dispose() {
		map.dispose();
		tiledWorldData.getGameObjectList().forEach(GameObject::dispose);
	}

	@Override
	public void show() {

		player.reset();
		resetCam(cam);

		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(player.getInputProcessor());
		else
			Gdx.input.setInputProcessor(player.getInputProcessor());

		Controllers.addListener(player.getControllerListener());
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
	public void hide() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer)
			((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(player.getInputProcessor());

		Controllers.removeListener(player.getControllerListener());
	}

	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	void removeGameObject(GameObject obj) {
		newGameObjectList.remove(obj);
	}

	void addText(String text, float lifetime, GameObject obj) {
		for (SimpleTextDrawer drawer : texts) {
			if (drawer.getObject() == obj) {
				drawer.setText(text);
				drawer.setLifeTime(lifetime);
				drawer.reset();
				return;
			}
		}

		texts.add(new SimpleTextDrawer(obj, text, lifetime));
	}

	void addText(String text, float lifeTime, float fadeTime, GameObject obj) {
		for (SimpleTextDrawer drawer : texts) {
			if (drawer.getObject() == obj) {
				drawer.setText(text);
				drawer.setLifeTime(lifeTime);
				drawer.setFadeTime(fadeTime);
				drawer.reset();
				return;
			}
		}

		texts.add(new SimpleTextDrawer(obj, text, lifeTime, fadeTime));
	}

	private void fadeOut(float seconds) {
		fadeTime = seconds;
		fadingIn = false;
		fadingOut = true;
		player.waitFor(seconds * 2);
	}

	private void fadeIn(float seconds) {
		fadeTime = seconds;
		fadingIn = true;
		fadingOut = false;
	}
}
