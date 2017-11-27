package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.slotmachine.SlotMachine;
import com.mygdx.game.tiledworld.TiledWorld;

public class MyGdxGame extends Game {

    private static final String TAG = MyGdxGame.class.getName();

    private SpriteBatch spriteBatch;
    private OrthographicCamera cam;
    private Screen world;
    private Screen slots;

    @Override
    public void create() {

        Gdx.app.setLogLevel(Constants.LOG_LVL);

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.ESCAPE) {
                    Gdx.app.log(TAG, "Spiel wurde durch ESC beendet.");
                    Gdx.app.exit();
                    return true;
                } else
                    return false;
            }
        }));

        Gdx.input.setCursorCatched(false);

        EventManager.setGame(this);

        //TODO Singleton SpriteBatch? Cam?
        spriteBatch = new SpriteBatch();

        // Constructs a new OrthographicCamera, using the given viewport width
        // and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        // Karte Laden
        world = new TiledWorld("overworld.tmx", spriteBatch, cam, this);
        slots = new SlotMachine(spriteBatch, cam, this);
        setScreen(world);

    }

    public void setToWorld() {
        setScreen(world);
    }

    public void setToSlotMachine() {
        setScreen(slots);
    }

    public Screen getWorld() {
        return world;
    }

    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
        world.dispose();
        slots.dispose();

    }

}
