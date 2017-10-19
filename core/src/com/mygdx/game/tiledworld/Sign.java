package com.mygdx.game.tiledworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

public class Sign extends GameObject {

	private static final int TEXT_SHOWTIME = 5;
	private String text;

	Sign(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	Sign(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	Sign(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	Sign(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);
		text = mapObject.getProperties().get("text", String.class);
	}

	public Sign(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		world.addText(text, TEXT_SHOWTIME, this);
		return true;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}

}
