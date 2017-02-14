package com.mygdx.game.tiledworld;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

public class MoralNPC extends NPC {

	private static final String TAG = MoralNPC.class.getName();

	private SimpleTextDrawer textDrawer = new SimpleTextDrawer(this);
	private List<List<String>> wordLists = new ArrayList<List<String>>(3);
	private String word;
	private boolean drawText = false;
	private float counter = 0;

	public MoralNPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	public MoralNPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	public MoralNPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	public MoralNPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	public MoralNPC(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	public MoralNPC(MapObject mapObject, TiledWorld tiledWorld){
		super(mapObject, tiledWorld);
		FileHandle handle = Gdx.files.internal("text/Moral.txt");

		String text = handle.readString();
		String[] parts = text.split("###");

		for (int i = 0; i < parts.length; i++) {
			String[] word = parts[i].split("\\r?\\n");
			wordLists.add(new ArrayList<String>());
			for (int j = 0; j < word.length; j++) {
				if (!word[j].isEmpty())
					wordLists.get(i).add(word[j].trim());
			}
		}
	}
	
	@Override
	public boolean onInteract(GameObject obj) {
		counter = 0;
		drawText = true;
		word = getWord();
		textDrawer.setText(word);

		Gdx.app.log(TAG, "Interaction hat funktioniert");
		return true;
	}

	private String getWord() {
		StringBuilder result = new StringBuilder(32);
		for (List<String> list : wordLists) {
			result.append(" ").append(list.get((int) (Math.random() * list.size())));
		}
		return result.toString().trim();
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
		if (drawText && (counter += deltaTime) >= 3)
			drawText = false;

		super.update(deltaTime);
	}

}
