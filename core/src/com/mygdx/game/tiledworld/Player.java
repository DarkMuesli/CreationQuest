package com.mygdx.game.tiledworld;

import java.awt.Point;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

/**
 * Main character class, instantiated and managed by {@link TiledWorld}.
 * 
 * 
 * @author Matthias Gross
 *
 */
public class Player extends Entity {

	@SuppressWarnings("unused")
	private static final String TAG = Player.class.getName();

	/**
	 * Instantiates a {@link Player} with the given coordinates, movement speed,
	 * {@link Direction} to face, {@link Sprite} to be represented by and
	 * {@link TiledWorld} to be present in.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link Player} will be represented by
	 * @param moveSpeed
	 *            Speed the {@link Player} will move with, 1 meaning 100% or
	 *            "normal" speed
	 * @param facing
	 *            {@link Direction} the {@link Player} will be facing
	 * @param world
	 *            {@link TiledWorld} the {@link Player} will be present in
	 */
	public Player(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	/**
	 * Instantiates a Player at Position [0,0] with the specified Player
	 * {@link Sprite} in the {@link TiledWorld}.
	 * 
	 * @param sprt
	 *            {@link Sprite} the player will be represented by
	 * @param world
	 *            {@link TiledWorld} to spawn the player in
	 */
	public Player(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	/**
	 * Instantiates a Player at Position [0,0] with the specified Player
	 * {@link Texture} as a {@link Sprite} in the {@link TiledWorld}.
	 * 
	 * @param tex
	 *            {@link Texture} will be transformed into a {@link Sprite} the
	 *            player will be represented by
	 * @param world
	 *            {@link TiledWorld} to spawn the player in
	 */
	public Player(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	/**
	 * Instantiates a Player at the specified Position, represented by the
	 * {@link Sprite} in the {@link TiledWorld}.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the player will be represented by
	 * @param world
	 *            {@link TiledWorld} to spawn the player in
	 */
	public Player(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	/**
	 * Instantiates a Player at the specified Position, represented by the
	 * {@link Texture} as a {@link Sprite} in the {@link TiledWorld}.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param tex
	 *            {@link Texture} will be transformed into a {@link Sprite} the
	 *            player will be represented by
	 * @param world
	 *            {@link TiledWorld} to spawn the player in
	 */
	public Player(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
		// TODO FIX THIS not to be the only functioning constructor
		comGen = new PlayerInputHandler(this);
	}
	
	public InputProcessor getInputProcessor(){
		return ((PlayerInputHandler)comGen).getPlayerInpAd();
	}
	
	public ControllerListener getControllerListener(){
		return ((PlayerInputHandler)comGen).getPlayerContAd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.Entity#move(com.mygdx.game.Direction)
	 */
	@Override
	public boolean move(Direction dir) {
		// if (facing != dir)

		boolean hasMoved = super.move(dir);
		if (hasMoved) {
			this.setChanged();
			this.notifyObservers(PlayerEvents.PLAYER_MOVED);
		}

		return hasMoved;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.Entity#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return true;
	}

	public static Player createPlayer(MapObject mapObject, TiledWorld world) {
		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		float pixx = mapObject.getProperties().get("x", float.class);
		float pixy = mapObject.getProperties().get("y", float.class);
		Point pos = world.getCellFromPixel(pixx, pixy);
		return new Player(pos.x, pos.y, tex, world);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		return false;
	}
	
	@Override
	public void reset() {
		((PlayerInputHandler)comGen).reset();
		super.reset();
	}

}
