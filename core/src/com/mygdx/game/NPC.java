/**
 * 
 */
package com.mygdx.game;

import java.awt.Point;

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
		// TODO Auto-generated constructor stub
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.Entity#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return false;
	}

	public static Entity createNPC(MapObject mapObject, TiledWorld tiledWorld) {
		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		float pixx = mapObject.getProperties().get("x", float.class);
		float pixy = mapObject.getProperties().get("y", float.class);
		Point pos = tiledWorld.getCellFromPixel(pixx, pixy);
		return new NPC(pos.x, pos.y, tex, tiledWorld);
	}

	public static Entity createMoralNPC(MapObject mapObject, TiledWorld tiledWorld) {
		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		float pixx = mapObject.getProperties().get("x", float.class);
		float pixy = mapObject.getProperties().get("y", float.class);
		Point pos = tiledWorld.getCellFromPixel(pixx, pixy);
		return new MoralNPC(pos.x, pos.y, tex, tiledWorld);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
