package de.thkoeln.creationquest;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import de.thkoeln.creationquest.tiledworld.Player;

public class FarmControllerAdapter extends ControllerAdapter {

    private Player player;

    FarmControllerAdapter(Player player) {
        this.player = player;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonIndex) {
        if (buttonIndex == 2) {
            if (player.isPulling()) {
                player.pull(true);
            } else if (player.isIdle()) {
                player.interactWithFacing();
            }
        } else if (buttonIndex == 1) {
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
