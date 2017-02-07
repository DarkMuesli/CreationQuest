package com.mygdx.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.tiledworld.Player;

public class FarmInputAdapter extends InputAdapter {

	private Player player;

	public FarmInputAdapter(Player player) {
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
