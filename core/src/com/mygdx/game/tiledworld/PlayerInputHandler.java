package com.mygdx.game.tiledworld;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class PlayerInputHandler implements CommandGenerator {

	private static final String TAG = PlayerInputHandler.class.getName();

	private Player player;

	private PlayerControllerAdapter playerContAd;
	private PlayerInputAdapter playerInpAd;

	private Map<Commands, Command> commandContMap;
	private Map<Commands, Command> commandOnceMap;

	private Command currentOnceCommand;

	private LinkedList<Command> contCommandList;

	public PlayerInputHandler(Player player) {

		this.player = player;

		playerContAd = new PlayerControllerAdapter(player, this);
		playerInpAd = new PlayerInputAdapter(player, this);

		commandContMap = new EnumMap<Commands, Command>(Commands.class);
		commandOnceMap = new EnumMap<Commands, Command>(Commands.class);

		contCommandList = new LinkedList<Command>();

		// Adding Continuous commands
		mapContCommand(Commands.MOVE_DOWN, e -> e.move(Direction.DOWN));
		mapContCommand(Commands.MOVE_UP, e -> e.move(Direction.UP));
		mapContCommand(Commands.MOVE_LEFT, e -> e.move(Direction.LEFT));
		mapContCommand(Commands.MOVE_RIGHT, e -> e.move(Direction.RIGHT));

		// Adding one-time commands
		mapOnceCommand(Commands.DO_NOTHING, e -> {
		});
		mapOnceCommand(Commands.TESTOUTPUT, e -> Gdx.app.log(TAG, "Testing Lambdas"));
		mapOnceCommand(Commands.INTERACT, e -> e.interactWithFacing());
		mapOnceCommand(Commands.SPEED_UP, e -> e.setMoveSpeed(e.getMoveSpeed() + 1));
		mapOnceCommand(Commands.SPEED_DOWN, e -> e.setMoveSpeed(e.getMoveSpeed() - 1));

	}

	public PlayerControllerAdapter getPlayerContAd() {
		return playerContAd;
	}

	public void setPlayerContAd(PlayerControllerAdapter playerContAd) {
		this.playerContAd = playerContAd;
	}

	public PlayerInputAdapter getPlayerInpAd() {
		return playerInpAd;
	}

	public void setPlayerInpAd(PlayerInputAdapter playerInpAd) {
		this.playerInpAd = playerInpAd;
	}

	public void mapContCommand(Commands commandEnum, Command commandLambda) {
		commandContMap.put(commandEnum, commandLambda);
	}

	public void mapOnceCommand(Commands commandEnum, Command commandLambda) {
		commandOnceMap.put(commandEnum, commandLambda);
	}

	public void postCommand(Commands commandEnum) {
		Command c;
		if ((c = commandOnceMap.get(commandEnum)) != null) {
			currentOnceCommand = c;
		}

		if ((c = commandContMap.get(commandEnum)) != null) {
			if (contCommandList.contains(c))
				contCommandList.remove(c);
			contCommandList.addFirst(c);
		}
	}

	@Override
	public void stopCommand(Commands commandEnum) {
		Command c;
		if ((c = commandContMap.get(commandEnum)) != null && contCommandList.contains(c))
			contCommandList.remove(c);
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

	public void reset() {
		currentOnceCommand = null;
		contCommandList.clear();
	}

}
