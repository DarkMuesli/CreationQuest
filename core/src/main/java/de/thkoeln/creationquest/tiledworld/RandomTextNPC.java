package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

import java.util.Random;

public class RandomTextNPC extends MonologNPC {

    private static String[] strings = Gdx.files.internal("text/Provokationen.txt").readString().split("\\r?\\n");

    RandomTextNPC(MapObject mapObject, TiledWorld tiledWorld) {
        super(mapObject, tiledWorld);
    }

    @Override
    public boolean onInteract(GameObject obj) {
        String oldText = text;
        do {
            text = strings[(new Random().nextInt(strings.length))].trim();
        } while (text.isEmpty() || text.equals(oldText));

        return super.onInteract(obj);
    }

}
