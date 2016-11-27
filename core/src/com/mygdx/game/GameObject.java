package com.mygdx.game;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject extends Observable implements Disposable {

	protected int x;
	protected int y;
	protected Sprite sprt;
	protected TiledWorld map;

	/**
	 * Generate a new GameObject by using the given sprite at the coordinate
	 * origin.
	 * 
	 * @param sprt
	 */
	public GameObject(Sprite sprt, TiledWorld map) {
		this(0, 0, sprt, map);
	}

	/**
	 * Generate a new GameObject by using the given sprite at the coordinate
	 * origin.
	 * 
	 * @param sprt
	 */
	public GameObject(Texture tex, TiledWorld map) {
		this(0, 0, tex, map);
	}

	/**
	 * Generate a new GameObject by using the given coordinates, sprite and
	 * movement speed.
	 * 
	 * @param x
	 * @param y
	 * @param sprt
	 * @param moveSpeed
	 */
	public GameObject(int x, int y, Sprite sprt, TiledWorld map) {
		this.x = x;
		this.y = y;
		this.sprt = sprt;
		this.map = map;

		this.addObserver(map);
		this.addObserver(EventManager.instance());
	}

	/**
	 * Generate a new GameObject by using the given coordinates and texture.
	 * 
	 * @param x
	 * @param y
	 * @param tex
	 */
	public GameObject(int x, int y, Texture tex, TiledWorld map) {
		this(x, y, new Sprite(tex), map);
	}

	public TiledWorld getMap() {
		return map;
	}

	public Sprite getSprt() {
		return sprt;
	}

	public void setSprt(Sprite sprt) {
		this.sprt = sprt;
	}

	public Vector2 getCellPosition() {
		return new Vector2(this.x, this.y);
	}

	public void setCellPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 getPixelPosition() {
		Vector2 pt = new Vector2();
		pt.x = Math.round(map.getTileWidth() * this.x);
		pt.y = Math.round(map.getTileHeight() * this.y);
		return pt;
	}
	
	public Vector2 getPixelCenter(){
		Vector2 ct = getPixelPosition();
		ct.x += map.getTileWidth() / 2;
		ct.y += map.getTileHeight() / 2;
		return ct;
	}
	
	@Override
	public void dispose() {
		sprt.getTexture().dispose();
	}
}
