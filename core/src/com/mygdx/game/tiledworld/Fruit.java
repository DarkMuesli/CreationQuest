package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;

public class Fruit extends GameObject {

	private static final String TAG = Fruit.class.getName();

	private static final float FADE_TIME = 3f;
	private static final float PITCH_RANGE = 0.8f;
	private static final Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/plop.wav"));
	private static final String words[] = Gdx.files.internal("text/test.txt").readString().trim().split("\n?\r");

	private boolean plucked;
	private float counter;

	private String text;

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
		counter = 0;

		text = words[(int) (Math.random() * words.length)].trim();
	}

	@Override
	public boolean onInteract(GameObject obj) {
		if (!plucked && (obj instanceof Entity)) {
			Entity e = (Entity) obj;
			e.pull(this);
		}
		return true;
	}

	@Override
	public void update(float deltaTime) {
		if (plucked) {
			if (counter <= FADE_TIME / 10f)
				sprt.translate(0, (200f / FADE_TIME) * deltaTime);

			float newSpriteAlpha = Math.max(0, sprt.getColor().a - (2 / (FADE_TIME)) * deltaTime);
			sprt.setAlpha(Math.max(0, newSpriteAlpha));

			if ((counter += deltaTime) >= FADE_TIME) {
				removeFruit();
			}
		}
	}

	public void pluckFruit() {

		plucked = true;
		penetrable = true;
		counter = 0;
		Gdx.app.log(TAG, "Frucht wird entfernt");

		long id = sound.play();
		sound.setPitch(id, 1f + ((float) Math.random() * PITCH_RANGE) - (PITCH_RANGE / 2));

		world.addText(text, FADE_TIME, FADE_TIME, this);
		sprt.setRegion(new Texture("tilesets/town_rpg_pack/town_rpg_pack/graphics/ruebe.png"));
	}

	private void removeFruit() {
		world.removeGameObject(this);
	}

}
