package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MoralNPC extends NPC {
	
	SimpleTextDrawer textDrawer = new SimpleTextDrawer(this);
	private boolean isLoaded = false;
	List<List<String>> wordLists = new ArrayList<List<String>>(3);
	public String word;
	public boolean drawText = false;
	public float counter = 0;

	public MoralNPC(int x, int y, Sprite sprt, double moveSpeed, Direction facing, TiledWorld world) {
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
	
	@Override
	public boolean onInteract(GameObject obj) {
		if (!isLoaded) {
			FileHandle handle = Gdx.files.internal("text/Moral.txt");

			String text = handle.readString();
			String[] parts = text.split("###");

			for (int i = 0; i < parts.length; i++) {
				String[] word = parts[i].split("\\r?\\n");
				wordLists.add(new ArrayList<String>());
				for (int j = 0; j < word.length; j++) {
					wordLists.get(i).add(word[j].trim());
				}
			}

			isLoaded = true;

		}

		counter = 0;
		drawText = true;
		word = getWord();
		textDrawer.setText(word);

		Gdx.app.log("Interaction", "Hat funktioniert");
		return true;
	}
	
	public String getWord() {
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
	public void update() {
		if (drawText) {
			counter += Gdx.graphics.getDeltaTime();
			if (counter >= 3) {
				drawText = false;
			}
		}
	}

}