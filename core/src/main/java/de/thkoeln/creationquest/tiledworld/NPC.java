package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.maps.MapObject;

/**
 * @author mgadm
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

}
