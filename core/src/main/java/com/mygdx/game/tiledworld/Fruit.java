package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utils.CoordinateHelper;


public class Fruit extends GameObject {

    private static final String TAG = Fruit.class.getName();

    private static final float FADE_TIME = 3f;
    private static final float PITCH_RANGE = 0.8f;
    private static final Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/plop.wav"));
    private static final String words[] = Gdx.files.internal("text/test.txt").readString().trim().split("\n?\r");

    private boolean plucked;
    private float counter;

    private String text;

    private Sprite pluckedSprt;

    Fruit(MapObject mapObject, TiledWorld world) {
        super(mapObject, world);

        int row = 0;
        int col = 0;
        int tileSize = world.getMapProp().getTileHeight();
        try {
            row = mapObject.getProperties().get("row", int.class);
            col = mapObject.getProperties().get("column", int.class);
            tileSize = mapObject.getProperties().get("tileSize", int.class);
        } catch (NullPointerException ignored) {
            Gdx.app.debug(TAG, "No parameters for texture detected; using defaults.");
            row = 0;
            col = 0;
            tileSize = world.getMapProp().getTileHeight();
        } finally {
            TextureRegion[][] split = TextureRegion.split(this.sprt.getTexture(), tileSize, tileSize);
            sprt = new Sprite(split[row][col]);
        }


        try {
            Texture pluckedTex = new Texture(mapObject.getProperties().get("pluckedPath", String.class));
            int pluckedRow = 0;
            int pluckedCol = 0;
            int pluckedTileSize = pluckedTex.getHeight();
            try {
                pluckedRow = mapObject.getProperties().get("pluckedRow", int.class);
                pluckedCol = mapObject.getProperties().get("pluckedColumn", int.class);
                pluckedTileSize = mapObject.getProperties().get("pluckedTileSize", int.class);
            } catch (NullPointerException ignored) {
                Gdx.app.debug(TAG, "No parameters for plucked texture detected; using defaults.");
                pluckedRow = 0;
                pluckedCol = 0;
                pluckedTileSize = getWorld().getMapProp().getTileHeight();
            } finally {
                TextureRegion[][] split = TextureRegion.split(pluckedTex, pluckedTileSize, pluckedTileSize);
                pluckedSprt = new Sprite(split[pluckedRow][pluckedCol]);
            }
        } catch (NullPointerException ignored) {
            Gdx.app.debug(TAG, "No plucked texture detected; using normal texture.");
            pluckedSprt = sprt;
        }

        Vector2 v = CoordinateHelper.getPixelFromCell(x, y, world.getMapProp().getTileWidth(), world.getMapProp().getTileHeight());
        sprt.setPosition(v.x, v.y);

        counter = 0;

        text = words[(int) (Math.random() * words.length)].trim();
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
            if (counter <= FADE_TIME / 10f)
                sprt.translate(0, (200f / FADE_TIME) * deltaTime);

            float newSpriteAlpha = Math.max(0, sprt.getColor().a - (2 / (FADE_TIME)) * deltaTime);
            sprt.setAlpha(Math.max(0, newSpriteAlpha));

            if ((counter += deltaTime) >= FADE_TIME) {
                removeFruit();
            }
        }
    }

    void pluckFruit() {

        plucked = true;
        penetrable = true;
        counter = 0;
        Gdx.app.log(TAG, "Fruit is plucked.");

        long id = sound.play();
        sound.setPitch(id, 1f + ((float) Math.random() * PITCH_RANGE) - (PITCH_RANGE / 2));

        world.addText(text, FADE_TIME, FADE_TIME, this);

        sprt.setRegion(pluckedSprt);
    }

    private void removeFruit() {
        world.removeGameObject(this);
    }

}
