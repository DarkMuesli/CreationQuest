package com.mygdx.game.tiledworld;

import java.awt.Point;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.EventManager;

/**
 * Abstract type for any Object inside the game with basic structures like
 * position and representation.
 * 
 * @author Matthias Gross
 *
 */
public abstract class GameObject extends Observable implements Disposable {

	@SuppressWarnings("unused")
	private static final String TAG = GameObject.class.getName();

	protected int x;
	protected int y;
	protected Sprite sprt;
	protected TiledWorld world;
	protected boolean penetrable;

	public GameObject(MapObject mapObject, TiledWorld world) {
		this(world);

		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		float pixx = mapObject.getProperties().get("x", float.class);
		float pixy = mapObject.getProperties().get("y", float.class);
		Point pos = CoordinateHelper.getCellFromPixel(pixx, pixy, world.getMapProp().getTileWidth(), world.getMapProp().getTileHeight());

		this.x = pos.x;
		this.y = pos.y;
		this.sprt = new Sprite(tex);

		//TODO: Compare with Entity Constructor...
		sprt.setBounds(getPixelPosition().x, getPixelPosition().y, world.getMapProp().getTileWidth(),
				sprt.getRegionHeight() * world.getMapProp().getTileWidth() / sprt.getRegionWidth());
	}

	private GameObject(TiledWorld world) {
		this.world = world;

		this.addObserver(world);
		this.addObserver(EventManager.instance());
	}

	/**
	 * @return the {@link TiledWorld}, the {@link GameObject} is present in
	 */
	public TiledWorld getWorld() {
		return world;
	}

	/**
	 * @return the {@link Sprite}, the GameObject is represented by
	 */
	public Sprite getSprt() {
		return sprt;
	}

	/**
	 * @param sprt
	 *            the new {@link Sprite}, the {@link GameObject} will be
	 *            represented by
	 */
	public void setSprt(Sprite sprt) {
		this.sprt = sprt;
	}

	public boolean isPenetrable() {
		return penetrable;
	}

	public void setPenetrable(boolean penetrable) {
		this.penetrable = penetrable;
	}

	/**
	 * @return the cell-based position of this {@link GameObject} as a
	 *         {@link Vector2}
	 */
	Point getCellPosition() {
		return new Point(this.x, this.y);
	}

	/**
	 * Sets the cell-based position of this {@link GameObject} to the given
	 * coordinates
	 * 
	 * @param x
	 * @param y
	 */
	void setCellPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the cell-based position of this {@link GameObject} to the
	 * coordinates, specified by the x and y values of a {@link Vector2}
	 * 
	 * @param pos
	 *            new position as {@link Vector2}
	 */
	void setCellPosition(Point pos) {
		setCellPosition(pos.x, pos.y);
	}

	/**
	 * @return current pixel-based position of this {@link GameObject} as a
	 *         {@link Vector2}. Calculated by cell-based position and its
	 *         {@link TiledWorld}'s tile height / width in pixels.
	 */
	Vector2 getPixelPosition() {
		Vector2 pt = new Vector2();
		pt.x = world.getMapProp().getTileWidth() * this.x;
		pt.y = world.getMapProp().getTileHeight() * this.y;
		return pt;
	}

	/**
	 * @return the pixel-based center of this {@link GameObject} as a
	 *         {@link Vector2}. Calculated by its {@link TiledWorld}'s tile
	 *         height / width in pixels.
	 */
	public Vector2 getPixelCenter() {
		Vector2 ct = getPixelPosition();
		ct.x += world.getMapProp().getTileWidth() / 2;
		ct.y += world.getMapProp().getTileHeight() / 2;
		return ct;
	}

	@Override
	public void dispose() {
		sprt.getTexture().dispose();
	}

	public void update(float deltaTime) {

	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		sprt.draw(spriteBatch);
		spriteBatch.end();
	}

	public abstract boolean onInteract(GameObject obj);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameObject that = (GameObject) o;

		if (x != that.x) return false;
		if (y != that.y) return false;
		if (penetrable != that.penetrable) return false;
		if (!sprt.equals(that.sprt)) return false;
		return world.equals(that.world);
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + sprt.hashCode();
		result = 31 * result + world.hashCode();
		result = 31 * result + (penetrable ? 1 : 0);
		return result;
	}
}
