package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

public class RandomTextNPC extends MonologNPC {

	private static String[] strings = Gdx.files.internal("text/Provokationen.txt").readString().split("\\r?\\n");

	RandomTextNPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	RandomTextNPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	RandomTextNPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	RandomTextNPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	RandomTextNPC(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	RandomTextNPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInteract(GameObject obj) {
		String oldText = text;
		do {
			text = strings[(int) (Math.random() * strings.length)].trim();
		} while (text.isEmpty() || text.equals(oldText));
		
		return super.onInteract(obj);
	}

}
