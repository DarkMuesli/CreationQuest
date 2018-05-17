package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.maps.MapObject;

/**
 * Main character class, instantiated and managed by {@link TiledWorld}.
 *
 * @author Matthias Gross
 */
public class Player extends Entity {

    @SuppressWarnings("unused")
    private static final String TAG = Player.class.getName();

    Player(MapObject mapObject, TiledWorld world) {
        super(mapObject, world);
        comGen = new PlayerInputHandler(this);
    }

    InputProcessor getInputProcessor() {
        return ((PlayerInputHandler) comGen).getPlayerInpAd();
    }

    ControllerListener getControllerListener() {
        return ((PlayerInputHandler) comGen).getPlayerContAd();
    }

    @Override
    public boolean move(Direction dir) {

        boolean hasMoved = super.move(dir);
        if (hasMoved) {
            this.setChanged();
            this.notifyObservers(PlayerEvents.PLAYER_MOVED);
        }

        return hasMoved;

    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void reset() {
        ((PlayerInputHandler) comGen).reset();
        super.reset();
    }

}
