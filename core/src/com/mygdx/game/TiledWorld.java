package com.mygdx.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class TiledWorld implements Disposable, Observer {

	private TiledMap map;
	private String mapName;

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

	// Array<TiledMapObjectLayer> objectLayers;

	public TiledWorld(String mapName) {
		setMap(mapName);
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

	public float getTileWidth() {
		return (map.getProperties().get("tilewidth", int.class));
	}

	public float getTileHeight() {
		return (map.getProperties().get("tileheight", int.class));
	}

	public Point getPixelFromCell(Point p) {
		return getPixelFromCell(p.x, p.y);
	}

	public Point getPixelFromCell(int x, int y) {
		int newX = Math.round(x * getTileWidth());
		int newY = Math.round(y * getTileHeight());

		return new Point(newX, newY);
	}

	public Vector2 getCellFromPixel(Point p) {
		return getCellFromPixel(p.x, p.y);
	}

	public Vector2 getCellFromPixel(int x, int y) {
		int newX = Math.round(x / getTileWidth());
		int newY = Math.round(y / getTileHeight());

		return new Vector2(newX, newY);
	}

	public Vector2 getCellFromPixel(float x, float y) {
		return getCellFromPixel(Math.round(x), Math.round(y));
	}

	public boolean checkCollision(Point p) {

		if (p.x < 0 || p.x >= this.getMapWidth() || p.y < 0 || p.y >= this.getMapHeight()) {
			return true;
		}

		for (TiledMapTileLayer layer : collisionTileLayers) {
			if (layer.getCell(p.x, p.y) != null) {
				return true;
			}
		}
		return false;
	}

	public boolean checkCollision(int x, int y) {
		return checkCollision(new Point(x, y));
	}

	public float getMapPixelWidth() {
		return getTileWidth() * (map.getProperties().get("width", int.class));
	}

	public float getMapPixelHeight() {
		return getTileHeight() * (map.getProperties().get("height", int.class));
	}

	public float getMapHeight() {
		return (map.getProperties().get("height", int.class));
	}

	public float getMapWidth() {
		return (map.getProperties().get("width", int.class));
	}

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

	public void setMapRendererView(OrthographicCamera cam) {
		mapRenderer.setView(cam);
	}

	public void render() {
		mapRenderer.render();
	}

	public void renderBackgroundLayers() {
		mapRenderer.render(bgTileLayersIndices);
	}

	public void renderCollisionLayers() {
		mapRenderer.render(collisionTileLayersIndices);
	}

	public void renderForegroundLayers() {
		mapRenderer.render(fgTileLayersIndices);
	}

	@Override
	public void dispose() {
		this.map.dispose();
	}

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
	}

	@Override
	public void update(Observable subj, Object arg) {
		if (subj instanceof Player) {
			Player player = (Player) subj;
			Events event = (Events) arg;

			switch (event) {

			case PLAYER_MOVED:
				System.out.println("Spieler hat sich bewegt!");
				for (MapObject obj : getLoadingZoneObjects()) {
					MapProperties objProp = obj.getProperties();

					Rectangle objPixelPos = new Rectangle(objProp.get("x", float.class), objProp.get("y", float.class),
							objProp.get("width", float.class), objProp.get("height", float.class));

					if (objPixelPos.contains(player.getPixelCenter())) {

						String oldMap = mapName;
						setMap(objProp.get("nextMap", String.class));

						System.out.println("Neue Karte geladen!");

						spawnPlayer(player, oldMap);

					}
				}
		
				EventManager.instance().checkTrigger(triggerObjects, player);
				
				break;
			}

		}

	}

}
