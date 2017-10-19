package com.mygdx.game.tiledworld;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

public class MoralNPC extends MonologNPC {

	private static final String TAG = MoralNPC.class.getName();

	private List<List<String>> wordLists = new ArrayList<>(3);

	MoralNPC(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, moveSpeed, facing, world);
	}

	MoralNPC(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
	}

	MoralNPC(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
	}

	MoralNPC(Sprite sprt, TiledWorld world) {
		super(sprt, world);
	}

	MoralNPC(Texture tex, TiledWorld world) {
		super(tex, world);
	}

	MoralNPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
		FileHandle handle = Gdx.files.internal("text/Moral.txt");

		String tmpText = handle.readString();
		String[] parts = tmpText.split("###");

		for (int i = 0; i < parts.length; i++) {
			String[] word = parts[i].split("\\r?\\n");
			wordLists.add(new ArrayList<>());
			for (String aWord : word) {
				if (!aWord.isEmpty())
					wordLists.get(i).add(aWord.trim());
			}
		}
	}

	@Override
	public boolean onInteract(GameObject obj) {

		text = getWord();

		return super.onInteract(obj);
	}

	private String getWord() {
		StringBuilder result = new StringBuilder(32);
		for (List<String> list : wordLists) {
			result.append(" ").append(list.get((int) (Math.random() * list.size())));
		}
		return result.toString().trim();
	}

}
