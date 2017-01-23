/**
 * 
 */
package com.mygdx.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * @author Matthias Gross
 *
 */
public class InputHandler {

	public enum CommandsEnum {
		MOVE_DOWN, MOVE_UP, MOVE_LEFT, MOVE_RIGHT, TESTOUTPUT, DO_NOTHING, INTERACT, EXIT
	}

	private static final String TAG = InputHandler.class.getName();

	private static final InputHandler instance = new InputHandler();

	private Map<CommandsEnum, Command> commandContMap;
	private Map<CommandsEnum, Command> commandOnceMap;
	private Map<Integer, CommandsEnum> keyMap;

	private InputHandler() {

		commandContMap = new EnumMap<CommandsEnum, Command>(CommandsEnum.class);
		commandOnceMap = new EnumMap<CommandsEnum, Command>(CommandsEnum.class);
		keyMap = new HashMap<Integer, CommandsEnum>();

		// Adding Continuous commands

		// Adding one-time commands
		mapOnceCommand(CommandsEnum.MOVE_DOWN, e -> e.move(Direction.DOWN));
		mapOnceCommand(CommandsEnum.MOVE_UP, e -> e.move(Direction.UP));
		mapOnceCommand(CommandsEnum.MOVE_LEFT, e -> e.move(Direction.LEFT));
		mapOnceCommand(CommandsEnum.MOVE_RIGHT, e -> e.move(Direction.RIGHT));

		mapOnceCommand(CommandsEnum.DO_NOTHING, e -> {} );
		mapOnceCommand(CommandsEnum.TESTOUTPUT, e -> Gdx.app.log(TAG, "Testing Lambdas"));
		mapOnceCommand(CommandsEnum.INTERACT, e -> e.interactWithFacing());
		mapOnceCommand(CommandsEnum.EXIT, (e) -> {
			Gdx.app.log(TAG, "Spiel wurde durch ESC beendet.");
			Gdx.app.exit();
		});

		mapKey(Keys.UP, CommandsEnum.MOVE_UP);
		mapKey(Keys.DOWN, CommandsEnum.MOVE_DOWN);
		mapKey(Keys.LEFT, CommandsEnum.MOVE_LEFT);
		mapKey(Keys.RIGHT, CommandsEnum.MOVE_RIGHT);
		mapKey(Keys.SPACE, CommandsEnum.INTERACT);
		mapKey(Keys.ENTER, CommandsEnum.TESTOUTPUT);
		mapKey(Keys.ESCAPE, CommandsEnum.EXIT);

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

	public static InputHandler instance() {
		return instance;
	}

	public List<Command> handleInput() {

		List<Command> list = new ArrayList<Command>();

		for (Map.Entry<Integer, CommandsEnum> entry : keyMap.entrySet()) {
			if (Gdx.input.isKeyJustPressed(entry.getKey())) {
				Command c = commandOnceMap.get(entry.getValue());
				//TODO: GENAUERE ABFRAGE FÜR MEHRFACHSCHRITTE
				if ((c != null) && (!list.contains(c)))
					list.add(commandOnceMap.get(entry.getValue()));
			}
			if (Gdx.input.isKeyPressed(entry.getKey())) {
				Command c = commandContMap.get(entry.getValue());
				//TODO: GENAUERE ABFRAGE FÜR MEHRFACHSCHRITTE
				if ((c != null) && (!list.contains(c)))
					list.add(commandContMap.get(entry.getValue()));

			}
		}

		// Debugging Controls
		if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
			commandContMap.put(CommandsEnum.MOVE_DOWN, commandOnceMap.get(CommandsEnum.MOVE_DOWN));
			commandContMap.put(CommandsEnum.MOVE_LEFT, commandOnceMap.get(CommandsEnum.MOVE_LEFT));
			commandContMap.put(CommandsEnum.MOVE_RIGHT, commandOnceMap.get(CommandsEnum.MOVE_RIGHT));
			commandContMap.put(CommandsEnum.MOVE_UP, commandOnceMap.get(CommandsEnum.MOVE_UP));
		}
		if (Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)) {
			commandContMap.remove(CommandsEnum.MOVE_DOWN);
			commandContMap.remove(CommandsEnum.MOVE_LEFT);
			commandContMap.remove(CommandsEnum.MOVE_RIGHT);
			commandContMap.remove(CommandsEnum.MOVE_UP);
		}

		return list;

	}

}
