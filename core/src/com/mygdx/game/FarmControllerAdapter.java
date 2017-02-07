package com.mygdx.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.mygdx.game.tiledworld.Player;

public class FarmControllerAdapter extends ControllerAdapter {

	private Player player;

	public FarmControllerAdapter(Player player) {
		this.player = player;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		if (buttonIndex == 10) {
			if (player.isPulling()) {
				player.pull(true);
			} else if (player.isIdle()) {
				player.interactWithFacing();
			}
		} else if (buttonIndex == 11) {
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
