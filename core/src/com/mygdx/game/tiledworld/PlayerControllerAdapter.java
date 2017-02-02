package com.mygdx.game.tiledworld;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class PlayerControllerAdapter extends ControllerAdapter {

	private static final String TAG = PlayerControllerAdapter.class.getName();

	private Player player;

	private CommandGenerator comGen;

	private Map<Integer, Commands> buttonMap;
	private boolean[] buttonsPressed;

	public PlayerControllerAdapter(Player player, PlayerInputHandler playerInpHandl) {
		this.player = player;
		this.comGen = playerInpHandl;

		buttonMap = new HashMap<Integer, Commands>();

		buttonsPressed = new boolean[256];

		mapButton(0, Commands.INTERACT);
		mapButton(6, Commands.TESTOUTPUT);
		mapButton(10, Commands.SPEED_DOWN);
		mapButton(11, Commands.SPEED_UP);
	}

	public void mapButton(int key, Commands command) {
		buttonMap.put(key, command);
	}

	public void unmapButton(int key) {
		buttonMap.remove(key);
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		buttonsPressed[buttonIndex] = true;

		Commands cEnum;
		if ((cEnum = buttonMap.get(buttonIndex)) != null) {
			comGen.postCommand(cEnum);
			return true;
		} else
			return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		buttonsPressed[buttonIndex] = false;

		Commands cEnum;
		if ((cEnum = buttonMap.get(buttonIndex)) != null) {
			comGen.stopCommand(cEnum);
			return true;
		} else
			return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		if (axisIndex == 2)
			if (value == 1)
				comGen.postCommand(Commands.MOVE_DOWN);
			else if (value == -1)
				comGen.postCommand(Commands.MOVE_UP);
			else {
				comGen.stopCommand(Commands.MOVE_UP);
				comGen.stopCommand(Commands.MOVE_DOWN);
			}
		else if (axisIndex == 3)
			if (value == 1)
				comGen.postCommand(Commands.MOVE_RIGHT);
			else if (value == -1)
				comGen.postCommand(Commands.MOVE_LEFT);
			else {
				comGen.stopCommand(Commands.MOVE_LEFT);
				comGen.stopCommand(Commands.MOVE_RIGHT);
			}
		return true;

	}

	public boolean isButtonPressed(int keycode) {
		return buttonsPressed[keycode];
	}

}
