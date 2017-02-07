/**
 * 
 */
package com.mygdx.game.tiledworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

/**
 * @author mgadm
 *
 */
public class NPC extends Entity {

	/**
	 * @param x
	 * @param y
	 * @param sprt
	 * @param moveSpeed
	 * @param facing
	 * @param world
	 */
	public NPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	/**
	 * @param x
	 * @param y
	 * @param sprt
	 * @param world
	 */
	public NPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	/**
	 * @param x
	 * @param y
	 * @param tex
	 * @param world
	 */
	public NPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	/**
	 * @param sprt
	 * @param world
	 */
	public NPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	/**
	 * @param tex
	 * @param world
	 */
	public NPC(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	public NPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.Entity#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public boolean onInteract(GameObject obj) {
		return false;
	}

}
