package com.mygdx.game.tiledworld;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;

public class Fruit extends GameObject {
	
	private static final String TAG = Fruit.class.getName();

	public Fruit(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	public Fruit(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	public Fruit(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	public Fruit(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	public static Fruit createFruit(MapObject mapObject, TiledWorld world) {
		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		TextureRegion[][] split = TextureRegion.split(tex, 16, 16);
		Sprite sprt = new Sprite(split[5][2]);
		float pixx = mapObject.getProperties().get("x", float.class);
		float pixy = mapObject.getProperties().get("y", float.class);
		sprt.setPosition(pixx, pixy);
		Point pos = world.getCellFromPixel(pixx, pixy);
		return new Fruit(pos.x, pos.y, sprt, world);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		world.removeGameObject(this);
		Gdx.app.log(TAG, "Frucht wird entfernt");
		return true;
	}

}
