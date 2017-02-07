package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

	public Fruit(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);
		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));
		TextureRegion[][] split = TextureRegion.split(tex, 16, 16);
		sprt = new Sprite(split[5][2]);
		sprt.setPosition(world.getPixelFromCell(x, y).x, world.getPixelFromCell(x, y).y);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		if (obj instanceof Entity) {
			Entity e = (Entity) obj;
			e.pull(this);
		}
		return true;
	}

	public void removeFruit() {
		world.removeGameObject(this);
		Gdx.app.log(TAG, "Frucht wird entfernt");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/plop.wav"));
		long id = sound.play();
		sound.setPitch(id, 1f + ((float) Math.random() * 0.2f) - 0.1f);
	}

}
