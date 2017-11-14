package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

public class MonologNPC extends NPC {

	private static final String TAG = MonologNPC.class.getName();

	private static final int TEXT_SHOWTIME = 5;

	protected String text;

	MonologNPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
		text = mapObject.getProperties().get("text", String.class);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		
		world.addText(text, TEXT_SHOWTIME, this);
		
		Gdx.app.log(TAG, "Interaction hat funktioniert");

		return super.onInteract(obj);
	}

}
