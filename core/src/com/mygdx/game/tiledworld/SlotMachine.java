package com.mygdx.game.tiledworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.EventManager;

public class SlotMachine extends GameObject {

	SlotMachine(Sprite sprt, TiledWorld world) {
		super(sprt, world);
		// TODO Auto-generated constructor stub
	}

	SlotMachine(Texture tex, TiledWorld world) {
		super(tex, world);
		// TODO Auto-generated constructor stub
	}

	SlotMachine(int x, int y, Sprite sprt, TiledWorld world) {
		super(x, y, sprt, world);
		// TODO Auto-generated constructor stub
	}

	SlotMachine(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);
		// TODO Auto-generated constructor stub
	}

	SlotMachine(int x, int y, Texture tex, TiledWorld world) {
		super(x, y, tex, world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInteract(GameObject obj) {
		EventManager.instance().triggerEvent("SlotMachine");
		return true;
	}

}
