package com.mygdx.game.tiledworld;

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
@Deprecated
public class InputHandler {

	public enum CommandsEnum {
		MOVE_DOWN, MOVE_UP, MOVE_LEFT, MOVE_RIGHT, TESTOUTPUT, DO_NOTHING, INTERACT, EXIT, SPEED_UP, SPEED_DOWN
	}

	private static final String TAG = InputHandler.class.getName();

	private static final InputHandler instance = new InputHandler();

	private Map<CommandsEnum, Command> commandContMap;
	private Map<CommandsEnum, Command> commandOnceMap;
	private Map<Integer, CommandsEnum> keyMap;

	private InputHandler() {

		commandContMap = new EnumMap<>(CommandsEnum.class);
		commandOnceMap = new EnumMap<>(CommandsEnum.class);
		keyMap = new HashMap<>();

		// Adding Continuous commands

		// Adding one-time commands
		mapContCommand(CommandsEnum.MOVE_DOWN, e -> e.move(Direction.DOWN));
		mapContCommand(CommandsEnum.MOVE_UP, e -> e.move(Direction.UP));
		mapContCommand(CommandsEnum.MOVE_LEFT, e -> e.move(Direction.LEFT));
		mapContCommand(CommandsEnum.MOVE_RIGHT, e -> e.move(Direction.RIGHT));

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

	public static InputHandler instance() {
		return instance;
	}

	public List<Command> handleInput() {

		List<Command> list = new ArrayList<>();

		for (Map.Entry<Integer, CommandsEnum> entry : keyMap.entrySet()) {
			if (Gdx.input.isKeyJustPressed(entry.getKey())) {
				Command c = commandOnceMap.get(entry.getValue());
				// TODO: GENAUERE ABFRAGE FUER MEHRFACHSCHRITTE
				if ((c != null) && (!list.contains(c)))
					list.add(commandOnceMap.get(entry.getValue()));
			}
			if (Gdx.input.isKeyPressed(entry.getKey())) {
				Command c = commandContMap.get(entry.getValue());
				// TODO: GENAUERE ABFRAGE FUER MEHRFACHSCHRITTE
				if ((c != null) && (!list.contains(c)))
					list.add(commandContMap.get(entry.getValue()));

			}
		}

		return list;

	}

}
