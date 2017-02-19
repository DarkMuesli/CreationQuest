package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;

public class Fruit extends GameObject {

	private static final String TAG = Fruit.class.getName();

	private static final float FADE_TIME = 3f;

	private boolean plucked;
	private float counter;

	private SimpleTextDrawer textDrawer;

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
		textDrawer = new SimpleTextDrawer(this);

		FileHandle file = Gdx.files.internal("text/test.txt");
		String txt = file.readString();
		String[] words = txt.split("\n?\r");
		textDrawer.setText(words[(int) (Math.random() * words.length)].trim());
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
			if (sprt.getColor().a != 0)
				sprt.translate(0, (200f / FADE_TIME) * deltaTime);

			float newSpriteAlpha = Math.max(0, sprt.getColor().a - (10 / (FADE_TIME)) * deltaTime);
			sprt.setAlpha(Math.max(0, newSpriteAlpha));

			float newTextAlpha = Math.max(0, textDrawer.getAlpha() - (1 / FADE_TIME) * deltaTime);
			textDrawer.setAlpha(newTextAlpha);
			
			if ((counter += deltaTime) >= FADE_TIME) {
				removeFruit();
			}
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);

		if (plucked)
			textDrawer.drawText(spriteBatch);

	}

	public void pluckFruit() {

		plucked = true;
		penetrable = true;
		counter = 0;
		Gdx.app.log(TAG, "Frucht wird entfernt");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/plop.wav"));
		long id = sound.play();
		sound.setPitch(id, 1f + ((float) Math.random() * 0.2f) - 0.1f);

		sprt.setRegion(new Texture("tilesets/town_rpg_pack/town_rpg_pack/graphics/ruebe.png"));
	}

	private void removeFruit() {
		world.removeGameObject(this);
	}

}
