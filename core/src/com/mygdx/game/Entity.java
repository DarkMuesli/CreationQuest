package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity extends GameObject {

	protected double moveSpeed;

	public Entity(Sprite sprt, TiledWorld map) {
		super(sprt, map);
		moveSpeed = 1;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tex
	 * @param map
	 */
	public Entity(Texture tex, TiledWorld map) {
		super(tex, map);
		moveSpeed = 1;
	}

	public Entity(int x, int y, Sprite sprt, TiledWorld map) {
		super(x, y, sprt, map);
		moveSpeed = 1;
		// TODO Auto-generated constructor stub
	}

	public Entity(int x, int y, Texture tex, TiledWorld map) {
		super(x, y, tex, map);
		moveSpeed = 1;
		// TODO Auto-generated constructor stub
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public abstract boolean isPlayer();

	public boolean move(Direction dir) {
		switch (dir) {
		case UP:
			if (!map.checkCollision(x, y + 1)) {
				y++;
				return true;
			}
			break;

		case RIGHT:
			if (!map.checkCollision(x + 1, y)) {
				x++;
				return true;
			}
			break;

		case DOWN:
			if (!map.checkCollision(x, y - 1)) {
				y--;
				return true;
			}
			break;

		case LEFT:
			if (!map.checkCollision(x - 1, y)) {
				x--;
				return true;
			}
			break;
		}

		return false;

	}

	public abstract boolean interact(GameObject obj);

}
