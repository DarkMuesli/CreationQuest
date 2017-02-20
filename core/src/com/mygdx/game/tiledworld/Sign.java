package com.mygdx.game.tiledworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

public class Sign extends GameObject {
	
	private static final int TEXT_SHOWTIME = 5;
	private SimpleTextDrawer textDrawer;
	private boolean drawText;
	private float counter = 0f;

	public Sign(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	public Sign(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	public Sign(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	public Sign(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);
		textDrawer = new SimpleTextDrawer(this);
		textDrawer.setText(mapObject.getProperties().get("text", String.class));
	}

	public Sign(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		drawText = true;
		return true;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if (drawText) 
			textDrawer.drawText(spriteBatch);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (drawText && (counter += deltaTime) > TEXT_SHOWTIME) {
			drawText = false;
			counter = 0f;
		}
	}

}
