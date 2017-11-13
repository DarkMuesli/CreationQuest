package com.mygdx.game.tiledworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;

/**
 * @author mgadm
 *
 */
public class NPC extends Entity {

	NPC(MapObject mapObject, TiledWorld tiledWorld) {
		super(mapObject, tiledWorld);
		String prop = mapObject.getProperties().get("ComGen", String.class);
		if (prop != null && prop.equals("RandomWalk"))
			comGen = new RandomWalkAI();
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public boolean onInteract(GameObject obj) {
		return super.onInteract(obj);
	}

}
