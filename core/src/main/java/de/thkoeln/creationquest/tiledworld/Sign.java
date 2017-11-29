package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;

public class Sign extends GameObject {

    private static final int TEXT_SHOWTIME = 5;
    private String text;

    Sign(MapObject mapObject, TiledWorld world) {
        super(mapObject, world);
        text = mapObject.getProperties().get("text", String.class);
    }

    @Override
    public boolean onInteract(GameObject obj) {
        world.addText(text, TEXT_SHOWTIME, this);
        return true;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
    }

}
