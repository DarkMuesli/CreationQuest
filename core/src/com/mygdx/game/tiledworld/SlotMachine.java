package com.mygdx.game.tiledworld;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.EventManager;

public class SlotMachine extends GameObject {

	SlotMachine(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);
	}

	@Override
	public boolean onInteract(GameObject obj) {
		EventManager.instance().triggerEvent("SlotMachine");
		return true;
	}

}
