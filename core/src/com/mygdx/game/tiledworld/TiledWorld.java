package com.mygdx.game.tiledworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Constants;
import com.mygdx.game.EventManager;
import com.mygdx.game.MyGdxGame;

/**
 * Class, representing the whole world, the game takes place in. Contains a
 * reference to the current {@link TiledMap} and a lot of additional
 * information.
 * 
 * @author Matthias Gross
 *
 */

public class TiledWorld implements Observer, Screen {

	private static final String TAG = TiledWorld.class.getName();

	private MyGdxGame game;

	private float lag;
	private TiledMap map;
	private String mapName;

	private Player player;

	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;

	private List<Entity> entityList;
	private List<Entity> newEntityList;

	private MapRenderer mapRenderer;

	private List<TiledMapTileLayer> tileLayers;

	private List<TiledMapTileLayer> bgTileLayers;
	private int[] bgTileLayersIndices;
	private int bgTileLayersIndicesCount;

	private List<TiledMapTileLayer> fgTileLayers;
	private int[] fgTileLayersIndices;
	private int fgTileLayersIndicesCount;

	private List<TiledMapTileLayer> collisionTileLayers;
	private int[] collisionTileLayersIndices;
	private int collisionTileLayersIndicesCount;

	private List<TiledMapTileLayer> triggerTileLayers;
	private int[] triggerTileLayersIndices;
	private int triggerTileLayersIndicesCount;

	private List<MapObject> loadingZoneObjects;
	private List<MapObject> triggerObjects;

	private MapLayer objectTileLayer;
	private List<MapObject> playerSpawnObjects;

	/**
	 * Instantiates a new game world, using the {@link TiledMap} in the
	 * specified location. Only necessary once per application run.
	 * 
	 * @param mapName
	 */
	public TiledWorld(String mapName, SpriteBatch spriteBatch, OrthographicCamera cam, MyGdxGame game) {
		entityList = new ArrayList<Entity>();
		newEntityList = new ArrayList<Entity>();
		this.cam = cam;
		this.spriteBatch = spriteBatch;
		this.game = game;

		setMap(mapName);

		resetCam(cam);
	}

	public MyGdxGame getGame() {
		return game;
	}

	public void setGame(MyGdxGame game) {
		this.game = game;
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public List<MapObject> getLoadingZoneObjects() {
		return loadingZoneObjects;
	}

	public List<MapObject> getTriggerObjects() {
		return triggerObjects;
	}

	public List<MapObject> getPlayerSpawnObjects() {
		return playerSpawnObjects;
	}

	public int[] getBgTileLayersIndices() {
		return bgTileLayersIndices;
	}

	public int getBgTileLayersIndicesCount() {
		return bgTileLayersIndicesCount;
	}

	public int[] getFgTileLayersIndices() {
		return fgTileLayersIndices;
	}

	public int getFgTileLayersIndicesCount() {
		return fgTileLayersIndicesCount;
	}

	public int[] getCollisionTileLayersIndices() {
		return collisionTileLayersIndices;
	}

	public int getCollisionTileLayersIndicesCount() {
		return collisionTileLayersIndicesCount;
	}

	public TiledMap getMap() {
		return map;
	}

	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public List<TiledMapTileLayer> getTileLayers() {
		return tileLayers;
	}

	public List<TiledMapTileLayer> getBgTileLayers() {
		return bgTileLayers;
	}

	public List<TiledMapTileLayer> getFgTileLayers() {
		return fgTileLayers;
	}

	public List<TiledMapTileLayer> getCollisionTileLayers() {
		return collisionTileLayers;
	}

	/**
	 * @return the width of a single tile of the current {@link TiledMap} in
	 *         pixels.
	 */
	public float getTileWidth() {
		return (map.getProperties().get("tilewidth", int.class));
	}

	/**
	 * @return the height of a single tile of the current {@link TiledMap} in
	 *         pixels.
	 */
	public float getTileHeight() {
		return (map.getProperties().get("tileheight", int.class));
	}

	/**
	 * Calculates a corresponding pixel based position to a cell based position.
	 * 
	 * @param p
	 *            cell based position to transform as a {@link Point}
	 * @return the pixel in the lower, left corner of the given cell in world
	 *         coordinates as a {@link Vector2}
	 */
	public Vector2 getPixelFromCell(Point p) {
		return getPixelFromCell(p.x, p.y);
	}

	/**
	 * Calculates a corresponding pixel based position to a cell based position.
	 * 
	 * @param x
	 *            cell based x coordinate
	 * @param y
	 *            cell based y coordinate
	 * @return the pixel in the lower, left corner of the given cell in world
	 *         coordinates as a {@link Vector2}
	 *
	 * @return
	 */
	public Vector2 getPixelFromCell(int x, int y) {
		float newX = x * getTileWidth();
		float newY = y * getTileHeight();

		return new Vector2(newX, newY);
	}

	/**
	 * Calculates a corresponding cell based position to a pixel based position.
	 * 
	 * @param p
	 *            pixel based position to transform as a {@link Vector2}
	 * @return corresponding cell based position as a {@link Point}
	 */
	public Point getCellFromPixel(Vector2 p) {
		return getCellFromPixel(p.x, p.y);
	}

	/**
	 * Calculates a corresponding cell based position to a pixel based position.
	 * 
	 * @param x
	 *            pixel based x coordinate as float
	 * @param y
	 *            pixel based y coordinate as float
	 * @return corresponding cell based position as a {@link Point}
	 */
	public Point getCellFromPixel(float x, float y) {

		float newX = x / getTileWidth();
		float newY = y / getTileHeight();

		return new Point(Math.round(newX), Math.round(newY));
	}

	/**
	 * Calculates a corresponding cell based position to a pixel based position.
	 * 
	 * @param x
	 *            pixel based x coordinate as integer
	 * @param y
	 *            pixel based y coordinate as integer
	 * @return corresponding cell based position as a {@link Point}
	 */
	public Point getCellFromPixel(int x, int y) {
		return getCellFromPixel((float) x, (float) y);
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
	public boolean checkCollision(Point p) {

		if (p.x < 0 || p.x >= this.getMapWidth() || p.y < 0 || p.y >= this.getMapHeight()) {
			return true;
		}

		for (TiledMapTileLayer layer : collisionTileLayers) {
			if (layer.getCell(p.x, p.y) != null) {
				return true;
			}
		}
		for (Entity entity : entityList) {
			if (entity.getCellPosition().equals(p))
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
	public boolean checkCollision(int x, int y) {
		return checkCollision(new Point(x, y));
	}

	/**
	 * @return the total width of the current map in pixels
	 */
	public float getMapPixelWidth() {
		return getTileWidth() * (map.getProperties().get("width", int.class));
	}

	/**
	 * @return the total height of the current map in pixels
	 */
	public float getMapPixelHeight() {
		return getTileHeight() * (map.getProperties().get("height", int.class));
	}

	/**
	 * @return the total width of the current map in cells
	 */
	public float getMapWidth() {
		return (map.getProperties().get("width", int.class));
	}

	/**
	 * @return the total height of the current map in cells
	 */
	public float getMapHeight() {
		return (map.getProperties().get("height", int.class));
	}

	/**
	 * Loads a new map into the {@link TiledWorld}. Plenty of stuff happening
	 * here.
	 * 
	 * @param mapName
	 *            path of the map to load, relative to the assets directory.
	 */
	public void setMap(String mapName) {

		if (this.map != null)
			this.map.dispose();

		this.map = new TmxMapLoader().load(mapName);
		this.mapName = mapName;

		mapRenderer = new OrthogonalTiledMapRenderer(this.map);

		Array<TiledMapTileLayer> tileLayersArray = map.getLayers().getByType(TiledMapTileLayer.class);

		objectTileLayer = map.getLayers().get("objects");

		loadingZoneObjects = new ArrayList<MapObject>();
		playerSpawnObjects = new ArrayList<MapObject>();
		triggerObjects = new ArrayList<MapObject>();

		if (objectTileLayer != null) {
			Iterator<MapObject> objIt = objectTileLayer.getObjects().iterator();

			while (objIt.hasNext()) {
				MapObject mapObject = (MapObject) objIt.next();

				switch (mapObject.getProperties().get("type", String.class)) {
				case "LoadingZone":
					loadingZoneObjects.add(mapObject);
					break;
				case "PlayerSpawn":
					playerSpawnObjects.add(mapObject);
					break;
				case "PlayerCreate":
					if (player == null) {
						player = Player.createPlayer(mapObject, this);
						entityList.add(player);
					}
					break;
				case "NPCCreate":
					newEntityList.add(NPC.createNPC(mapObject, this));
					break;
				case "MoralNPCCreate":
					newEntityList.add(NPC.createMoralNPC(mapObject, this));
					break;
				case "Trigger":
					triggerObjects.add(mapObject);
				default:
					break;
				}
			}
		}

		tileLayers = new ArrayList<TiledMapTileLayer>();
		bgTileLayers = new ArrayList<TiledMapTileLayer>();
		fgTileLayers = new ArrayList<TiledMapTileLayer>();
		collisionTileLayers = new ArrayList<TiledMapTileLayer>();
		triggerTileLayers = new ArrayList<TiledMapTileLayer>();

		for (TiledMapTileLayer tmtl : tileLayersArray) {
			tileLayers.add(tmtl);
		}

		fgTileLayersIndicesCount = 0;
		bgTileLayersIndicesCount = 0;
		collisionTileLayersIndicesCount = 0;
		triggerTileLayersIndicesCount = 0;

		for (int i = 0; i < tileLayers.size(); i++) {
			TiledMapTileLayer tl = tileLayers.get(i);

			switch (tl.getName().substring(0, 3)) {
			case "fgr":
				fgTileLayersIndicesCount++;
				break;
			case "bgr":
				bgTileLayersIndicesCount++;
				break;
			case "col":
				collisionTileLayersIndicesCount++;
				break;
			case "trg":
				triggerTileLayersIndicesCount++;
				break;
			}
		}

		fgTileLayersIndices = new int[fgTileLayersIndicesCount];
		bgTileLayersIndices = new int[bgTileLayersIndicesCount];
		collisionTileLayersIndices = new int[collisionTileLayersIndicesCount];
		triggerTileLayersIndices = new int[triggerTileLayersIndicesCount];

		fgTileLayersIndicesCount = 0;
		bgTileLayersIndicesCount = 0;
		collisionTileLayersIndicesCount = 0;
		triggerTileLayersIndicesCount = 0;

		for (int i = 0; i < tileLayers.size(); i++) {
			TiledMapTileLayer tl = tileLayers.get(i);

			switch (tl.getName().substring(0, 3)) {
			case "fgr":
				fgTileLayers.add(tl);
				fgTileLayersIndices[fgTileLayersIndicesCount++] = i;
				break;
			case "bgr":
				bgTileLayers.add(tl);
				bgTileLayersIndices[bgTileLayersIndicesCount++] = i;
				break;
			case "col":
				collisionTileLayers.add(tl);
				collisionTileLayersIndices[collisionTileLayersIndicesCount++] = i;
				break;
			case "trg":
				triggerTileLayers.add(tl);
				triggerTileLayersIndices[triggerTileLayersIndicesCount++] = i;
				break;
			}

		}
	}

	/**
	 * Render the whole map at once.
	 */
	public void renderMap() {
		mapRenderer.render();
	}

	/**
	 * Render only the background {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "bgr". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	public void renderBackgroundLayers() {
		mapRenderer.render(bgTileLayersIndices);
	}

	/**
	 * Render only the collision {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "col". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	public void renderCollisionLayers() {
		mapRenderer.render(collisionTileLayersIndices);
	}

	/**
	 * Render only the foreground {@link TiledMapTileLayer}s, specified in the
	 * Tiled-software by the prefix "fgr". Render order of Tiled-software will
	 * be maintained (bottom-up).
	 */
	public void renderForegroundLayers() {
		mapRenderer.render(fgTileLayersIndices);
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
	public void spawnPlayer(Player player, String oldMap) {

		MapObject defaultSpawn = null;

		for (MapObject spwnObject : playerSpawnObjects) {
			MapProperties spwnObjProp = spwnObject.getProperties();

			if (spwnObjProp.get("From", String.class).equals(oldMap)) {
				player.setCellPosition(this.getCellFromPixel(Math.round(spwnObjProp.get("x", float.class)),
						Math.round(spwnObjProp.get("y", float.class))));
				return;
			}

			if (spwnObjProp.get("From", String.class).equals("Default"))
				defaultSpawn = spwnObject;
		}

		if (defaultSpawn != null)
			player.setCellPosition(this.getCellFromPixel(Math.round(defaultSpawn.getProperties().get("x", float.class)),
					Math.round(defaultSpawn.getProperties().get("y", float.class))));
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

						String oldMap = mapName;

						newEntityList.clear();
						newEntityList.add(player);
						setMap(objProp.get("nextMap", String.class));

						Gdx.app.log(TAG, "Neue Karte geladen: " + mapName);

						spawnPlayer(player, oldMap);

						player.reset();
						player.waitFor(0.2f);
						
						resetCam(cam);

					}
				}

				EventManager.instance().checkTrigger(triggerObjects, player);

				break;
			}

		}

	}

	public void draw(SpriteBatch spriteBatch, OrthographicCamera cam) {

		updateCam(cam);

		spriteBatch.setProjectionMatrix(cam.combined);
		mapRenderer.setView(cam);

		renderBackgroundLayers();
		renderCollisionLayers();

		// TODO: NIX GUT SO
		// spriteBatch.begin();
		entityList.forEach(e -> e.draw(spriteBatch));
		// spriteBatch.end();

		renderForegroundLayers();

	}

	public void updateCam(OrthographicCamera cam) {
		float camx, camy, camz = 0;

		if (getMapPixelWidth() >= cam.viewportWidth)
			camx = Math.min(Math.max(cam.viewportWidth / 2, player.getSprt().getX() - player.getSprt().getWidth() / 2),
					getMapPixelWidth() - cam.viewportWidth / 2);
		else
			camx = getMapPixelWidth() / 2;

		if (getMapPixelHeight() >= cam.viewportHeight)
			camy = Math.min(
					Math.max(cam.viewportHeight / 2, player.getSprt().getY() - player.getSprt().getHeight() / 2),
					getMapPixelHeight() - cam.viewportHeight / 2);
		else
			camy = getMapPixelHeight() / 2;

		cam.position.set(lerp(cam.position.x, camx, 0.1f), lerp(cam.position.y, camy, 0.1f), camz);

		cam.update();
	}
	
	
	//TODO Codedopplung zu updateCam vermeiden
	public void resetCam(OrthographicCamera cam){
		float camx, camy, camz = 0;

		if (getMapPixelWidth() >= cam.viewportWidth)
			camx = Math.min(Math.max(cam.viewportWidth / 2, player.getSprt().getX() - player.getSprt().getWidth() / 2),
					getMapPixelWidth() - cam.viewportWidth / 2);
		else
			camx = getMapPixelWidth() / 2;

		if (getMapPixelHeight() >= cam.viewportHeight)
			camy = Math.min(
					Math.max(cam.viewportHeight / 2, player.getSprt().getY() - player.getSprt().getHeight() / 2),
					getMapPixelHeight() - cam.viewportHeight / 2);
		else
			camy = getMapPixelHeight() / 2;

		cam.position.set(camx, camy, camz);

		cam.update();
	}

	public void update(float deltaTime) {
		newEntityList.addAll(entityList);

		entityList.forEach(e -> e.update(deltaTime));

		entityList.clear();
		entityList.addAll(newEntityList);
		newEntityList.clear();

		entityList.sort((e1, e2) -> {
			if (e1.getCellPosition().y == e2.getCellPosition().y)
				return 0;
			else if (e1.getCellPosition().y < e2.getCellPosition().y)
				return 1;
			else
				return -1;
		});

	}

	@Override
	public void dispose() {
		map.dispose();
		entityList.forEach(e -> e.dispose());
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
	public void resize(int width, int height) {
		// TODO: implementation denkbar
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
			((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(player.getInputProcessor());

		Controllers.removeListener(player.getControllerListener());
	}
	
	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

}
