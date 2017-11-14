package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class RandomTextNPC extends MonologNPC {

	private static String[] strings = Gdx.files.internal("text/Provokationen.txt").readString().split("\\r?\\n");

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
