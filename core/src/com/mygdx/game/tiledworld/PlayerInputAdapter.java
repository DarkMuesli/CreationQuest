package com.mygdx.game.tiledworld;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class PlayerInputAdapter extends InputAdapter {

	private static final String TAG = PlayerInputAdapter.class.getName();

	private Player player;

	private CommandGenerator comGen;

	private Map<Integer, Commands> keyMap;
	private boolean[] keysPressed;

	PlayerInputAdapter(Player player, PlayerInputHandler playerInpHandl) {

		this.player = player;

		this.comGen = playerInpHandl;

		keyMap = new HashMap<>();

		keysPressed = new boolean[256];

		mapKey(Keys.UP, Commands.MOVE_UP);
		mapKey(Keys.DOWN, Commands.MOVE_DOWN);
		mapKey(Keys.LEFT, Commands.MOVE_LEFT);
		mapKey(Keys.RIGHT, Commands.MOVE_RIGHT);
		mapKey(Keys.SPACE, Commands.INTERACT);
		mapKey(Keys.ENTER, Commands.TESTOUTPUT);
		mapKey(Keys.PLUS, Commands.SPEED_UP);
		mapKey(Keys.MINUS, Commands.SPEED_DOWN);
	}

	public void mapKey(int key, Commands command) {
		keyMap.put(key, command);
	}

	public void unmapKey(int key) {
		keyMap.remove(key);
	}

	@Override
	public boolean keyDown(int keycode) {
		keysPressed[keycode] = true;

		Commands cEnum;
		if ((cEnum = keyMap.get(keycode)) != null) {
			comGen.postCommand(cEnum);
			return true;
		} else
			return false;

	}

	@Override
	public boolean keyUp(int keycode) {
		keysPressed[keycode] = false;

		Commands cEnum;
		if ((cEnum = keyMap.get(keycode)) != null) {
			comGen.stopCommand(cEnum);
			return true;
		} else
			return false;
	}

	public boolean isKeyPressed(int keycode) {
		return keysPressed[keycode];
	}

}
