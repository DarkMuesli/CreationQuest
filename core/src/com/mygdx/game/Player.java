package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Main character class, instantiated and managed by {@link TiledWorld}.
 * 
 * 
 * @author Matthias Groﬂ
 *
 */
public class Player extends Entity {

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
	public Player(int x, int y, Sprite sprt, double moveSpeed, Direction facing, TiledWorld world) {
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mygdx.game.Entity#move(com.mygdx.game.Direction)
	 */
	@Override
	public boolean move(Direction dir) {
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
	 * @see com.mygdx.game.Entity#interact(com.mygdx.game.GameObject)
	 */
	@Override
	public boolean interactWith(GameObject obj) {
		// TODO: implement interaction
		return false;
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

}
