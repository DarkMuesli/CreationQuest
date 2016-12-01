package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Abstract type for all entities, as in "living things". Characterized by being
 * able to move and/or interact with.
 * 
 * @author mgadm
 *
 */
public abstract class Entity extends GameObject {

	/**
	 * The movement speed of the {@link Entity}, 1 meaning 100% or "normal"
	 * movement speed.
	 */
	protected double moveSpeed;

	/**
	 * {@link Direction}, the {@link Entity} is facing.
	 */
	protected Direction facing;

	/**
	 * Instantiates an {@link Entity} with the given coordinates, movement
	 * speed, {@link Direction} to face, {@link Sprite} to be represented by and
	 * {@link TiledWorld} to be present in.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link Entity} will be represented by
	 * @param moveSpeed
	 *            Speed the {@link Entity} will move with, 1 meaning 100% or
	 *            "normal" speed
	 * @param facing
	 *            {@link Direction} the {@link Entity} will be facing
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Sprite sprt, double moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, world);
		this.moveSpeed = moveSpeed;
		this.facing = facing;
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the given
	 * coordinates, represented by the given {@link Sprite} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Sprite sprt, TiledWorld world) {
		this(x, y, sprt, 1, Direction.DOWN, world);
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the given
	 * coordinates, represented by the given {@link Texture} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param tex
	 *            {@link Texture} the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Texture tex, TiledWorld world) {
		this(x, y, new Sprite(tex), world);
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the coordinate
	 * origin represented by the given {@link Sprite} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param sprt
	 *            {@link Sprite}, the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld}, the {@link Entity} will be present in
	 */
	public Entity(Sprite sprt, TiledWorld world) {
		this(0, 0, sprt, world);
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the coordinate
	 * origin represented by the given {@link Texture} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param tex
	 *            {@link Texture}, the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld}, the {@link Entity} will be present in
	 */
	public Entity(Texture tex, TiledWorld world) {
		this(0, 0, new Sprite(tex), world);
	}

	/**
	 * @return current movement sped of this {@link Entity}
	 */
	public double getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * Sets the current movement speed for this {@link Entity}
	 * 
	 * @param moveSpeed
	 */
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * Try to move this {@link Entity} one cell towards the specified
	 * {@link Direction}.
	 * 
	 * @param dir
	 *            {@link Direction} to move to
	 * @return <code>true</code> if the movement was successful,
	 *         <code>false</code> otherwise (e.g. when collision happened).
	 */
	public boolean move(Direction dir) {
		switch (dir) {
		case UP:
			if (!world.checkCollision(x, y + 1)) {
				y++;
				return true;
			}
			break;

		case RIGHT:
			if (!world.checkCollision(x + 1, y)) {
				x++;
				return true;
			}
			break;

		case DOWN:
			if (!world.checkCollision(x, y - 1)) {
				y--;
				return true;
			}
			break;

		case LEFT:
			if (!world.checkCollision(x - 1, y)) {
				x--;
				return true;
			}
			break;
		}

		return false;

	}

	/**
	 * Starts an interaction with the specified {@link GameObject}.
	 * 
	 * @param obj
	 *            the {@link GameObject} to interact with
	 * @return <code>true</code>, if the interaction did happen,
	 *         <code>false</code> otherwise.
	 */
	public abstract boolean interactWith(GameObject obj);

	/**
	 * @return <code>true</code>, if this {@link Entity} is the player character
	 */
	public abstract boolean isPlayer();

}
