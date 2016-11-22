package com.mygdx.game;

import java.awt.Point;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

	public Player(Sprite sprt, TiledWorld map) {
		super(sprt, map);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tex
	 * @param map
	 */
	public Player(Texture tex, TiledWorld map) {
		super(tex, map);
		// TODO Auto-generated constructor stub
	}

	public Player(int x, int y, Sprite sprt, TiledWorld map) {
		super(x, y, sprt, map);
		// TODO Auto-generated constructor stub
	}

	public Player(int x, int y, Texture tex, TiledWorld map) {
		super(x, y, tex, map);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(Direction dir) {
		boolean hasMoved = super.move(dir);
		if (hasMoved) {
			this.setChanged();
			this.notifyObservers(Events.PLAYER_MOVED);
		}

		return hasMoved;

	}

	@Override
	public boolean interact(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	public void setCellPosition(Vector2 pos) {
		setCellPosition(Math.round(pos.x), Math.round(pos.y));
	}

}
