package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

public class MonologNPC extends NPC {

	private static final String TAG = MonologNPC.class.getName();

	private static final int TEXT_SHOWTIME = 5;

	protected String text;

	MonologNPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	MonologNPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	MonologNPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	MonologNPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	MonologNPC(Texture tex, TiledWorld world) {
		super(tex, world);
	}

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
