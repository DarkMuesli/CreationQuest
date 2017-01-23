package com.mygdx.game;

import java.awt.Point;
import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

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

	/**
	 * Generate a new GameObject using the given {@link Sprite} at the
	 * coordinate origin.
	 * 
	 * @param sprt
	 *            {@link Sprite} the {@link GameObject} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link GameObject} will be present in
	 */
	public GameObject(Sprite sprt, TiledWorld world) {
		this(0, 0, sprt, world);
	}

	/**
	 * Generate a new GameObject using the given {@link Texture} as a
	 * {@link Sprite} at the coordinate origin.
	 * 
	 * @param tex
	 *            {@link Texture} used as {@link Sprite} the {@link GameObject}
	 *            will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link GameObject} will be present in
	 */
	public GameObject(Texture tex, TiledWorld world) {
		this(0, 0, tex, world);
	}

	/**
	 * Generate a new GameObject by using the given coordinates and
	 * {@link Sprite}.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link GameObject} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link GameObject} will be present in
	 */
	public GameObject(int x, int y, Sprite sprt, TiledWorld world) {
		this.x = x;
		this.y = y;
		this.sprt = sprt;
		this.world = world;

		this.addObserver(world);
		this.addObserver(EventManager.instance());
	}

	/**
	 * Generate a new GameObject by using the given {@link Texture} as a
	 * {@link Sprite} at the given coordinates.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param tex
	 *            {@link Texture} used as {@link Sprite} the {@link GameObject}
	 *            will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link GameObject} will be present in
	 */
	public GameObject(int x, int y, Texture tex, TiledWorld world) {
		this(x, y, new Sprite(tex), world);
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

	/**
	 * @return the cell-based position of this {@link GameObject} as a
	 *         {@link Vector2}
	 */
	public Point getCellPosition() {
		return new Point(this.x, this.y);
	}

	/**
	 * Sets the cell-based position of this {@link GameObject} to the given
	 * coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public void setCellPosition(int x, int y) {
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
	public void setCellPosition(Point pos) {
		setCellPosition(pos.x, pos.y);
	}

	/**
	 * @return current pixel-based position of this {@link GameObject} as a
	 *         {@link Vector2}. Calculated by cell-based position and its
	 *         {@link TiledWorld}'s tile height / width in pixels.
	 */
	public Vector2 getPixelPosition() {
		Vector2 pt = new Vector2();
		pt.x = world.getTileWidth() * this.x;
		pt.y = world.getTileHeight() * this.y;
		return pt;
	}

	/**
	 * @return the pixel-based center of this {@link GameObject} as a
	 *         {@link Vector2}. Calculated by its {@link TiledWorld}'s tile
	 *         height / width in pixels.
	 */
	public Vector2 getPixelCenter() {
		Vector2 ct = getPixelPosition();
		ct.x += world.getTileWidth() / 2;
		ct.y += world.getTileHeight() / 2;
		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		sprt.getTexture().dispose();
	}

	public void update() {

	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		spriteBatch.draw(getSprt(), getPixelPosition().x, getPixelPosition().y + 5, getWorld().getTileWidth(),
				getSprt().getHeight() * getWorld().getTileWidth() / getSprt().getWidth());
		spriteBatch.end();
	}
		
	public abstract boolean onInteract(GameObject obj);
}
