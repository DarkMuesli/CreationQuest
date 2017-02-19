package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

public class MonologNPC extends NPC {

	private static final String TAG = MonologNPC.class.getName();

	private static final int TEXT_SHOWTIME = 5;

	protected String text;

	private SimpleTextDrawer textDrawer = new SimpleTextDrawer(this);
	private boolean drawText = false;
	private float counter = 0;

	public MonologNPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
		// TODO Auto-generated constructor stub
	}

	public MonologNPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
		// TODO Auto-generated constructor stub
	}

	public MonologNPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
		// TODO Auto-generated constructor stub
	}

	public MonologNPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
		// TODO Auto-generated constructor stub
	}

	public MonologNPC(Texture tex, TiledWorld world) {
		super(tex, world);
		// TODO Auto-generated constructor stub
	}

	public MonologNPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
		text = mapObject.getProperties().get("text", String.class);
		if (text != null)
			textDrawer.setText(text);
		else
			textDrawer.setText("Ich kann nicht richtig reden!");
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if (drawText) {
			textDrawer.drawText(spriteBatch);
		}
	}

	@Override
	public void update(float deltaTime) {
		if (drawText && (counter += deltaTime) >= TEXT_SHOWTIME)
			drawText = false;

		super.update(deltaTime);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		textDrawer.setText(text);
		counter = 0;
		drawText = true;

		Gdx.app.log(TAG, "Interaction hat funktioniert");

		return true;
	}

}
