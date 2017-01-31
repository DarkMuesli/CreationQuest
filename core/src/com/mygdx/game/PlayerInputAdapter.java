package com.mygdx.game;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class PlayerInputAdapter extends InputAdapter implements CommandGenerator {

	private Player player;

	public enum CommandsEnum {
		MOVE_DOWN, MOVE_UP, MOVE_LEFT, MOVE_RIGHT, TESTOUTPUT, DO_NOTHING, INTERACT, EXIT, SPEED_UP, SPEED_DOWN
	}

	private static final String TAG = PlayerInputAdapter.class.getName();

	private Map<CommandsEnum, Command> commandContMap;
	private Map<CommandsEnum, Command> commandOnceMap;
	private Map<Integer, CommandsEnum> keyMap;

	private boolean[] buttonsPressed;

	private Command currentOnceCommand;

	private LinkedList<Command> contCommandList;

	public PlayerInputAdapter(Player player) {

		this.player = player;

		commandContMap = new EnumMap<CommandsEnum, Command>(CommandsEnum.class);
		commandOnceMap = new EnumMap<CommandsEnum, Command>(CommandsEnum.class);
		keyMap = new HashMap<Integer, CommandsEnum>();

		buttonsPressed = new boolean[256];

		contCommandList = new LinkedList<Command>();

		// Adding Continuous commands
		mapContCommand(CommandsEnum.MOVE_DOWN, e -> e.move(Direction.DOWN));
		mapContCommand(CommandsEnum.MOVE_UP, e -> e.move(Direction.UP));
		mapContCommand(CommandsEnum.MOVE_LEFT, e -> e.move(Direction.LEFT));
		mapContCommand(CommandsEnum.MOVE_RIGHT, e -> e.move(Direction.RIGHT));

		// Adding one-time commands
		mapOnceCommand(CommandsEnum.DO_NOTHING, e -> {
		});
		mapOnceCommand(CommandsEnum.TESTOUTPUT, e -> Gdx.app.log(TAG, "Testing Lambdas"));
		mapOnceCommand(CommandsEnum.INTERACT, e -> e.interactWithFacing());
		mapOnceCommand(CommandsEnum.SPEED_UP, e -> e.setMoveSpeed(e.getMoveSpeed() + 1));
		mapOnceCommand(CommandsEnum.SPEED_DOWN, e -> e.setMoveSpeed(e.getMoveSpeed() - 1));

		mapKey(Keys.UP, CommandsEnum.MOVE_UP);
		mapKey(Keys.DOWN, CommandsEnum.MOVE_DOWN);
		mapKey(Keys.LEFT, CommandsEnum.MOVE_LEFT);
		mapKey(Keys.RIGHT, CommandsEnum.MOVE_RIGHT);
		mapKey(Keys.SPACE, CommandsEnum.INTERACT);
		mapKey(Keys.ENTER, CommandsEnum.TESTOUTPUT);
		mapKey(Keys.PLUS, CommandsEnum.SPEED_UP);
		mapKey(Keys.MINUS, CommandsEnum.SPEED_DOWN);

	}

	public void mapContCommand(CommandsEnum commandEnum, Command commandLambda) {
		commandContMap.put(commandEnum, commandLambda);
	}

	public void mapOnceCommand(CommandsEnum commandEnum, Command commandLambda) {
		commandOnceMap.put(commandEnum, commandLambda);
	}

	public void mapKey(int key, CommandsEnum command) {
		keyMap.put(key, command);
	}

	public void unmapKey(int key) {
		keyMap.remove(key);
	}

	@Override
	public boolean keyDown(int keycode) {
		buttonsPressed[keycode] = true;

		Command c;

		if ((c = commandOnceMap.get(keyMap.get(keycode))) != null) {
			currentOnceCommand = c;
			return true;
		} else if ((c = commandContMap.get(keyMap.get(keycode))) != null) {

			if (contCommandList.contains(c))
				contCommandList.remove(c);

			contCommandList.addFirst(c);

			return true;
		} else
			return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		buttonsPressed[keycode] = false;

		Command c;
		if ((c = commandContMap.get(keyMap.get(keycode))) != null && contCommandList.contains(c))
			contCommandList.remove(c);

		return true;
	}

	public boolean isButtonPressed(int keycode) {
		return buttonsPressed[keycode];
	}

	@Override
	public Command updateOnceCommand(float deltaTime) {
		Command tmp = currentOnceCommand;
		currentOnceCommand = null;
		return tmp;
	}

	@Override
	public Command updateContCommand(float deltaTime) {
		return contCommandList.peek();
	}

}
