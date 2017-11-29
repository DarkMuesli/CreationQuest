package de.thkoeln.creationquest;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import de.thkoeln.creationquest.tiledworld.Player;

public class FarmInputAdapter extends InputAdapter {

    private Player player;

    FarmInputAdapter(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.Y) {
            if (player.isPulling()) {
                player.pull(true);
            } else if (player.isIdle()) {
                player.interactWithFacing();
            }
        } else if (keycode == Keys.X) {
            if (player.isPulling()) {
                player.pull(false);
            } else if (player.isIdle()) {
                player.interactWithFacing();
            }
        } else
            return false;

        return true;
    }

}
