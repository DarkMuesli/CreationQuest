package com.mygdx.game.tiledworld;

public class RandomWalkAI implements CommandGenerator {

	private double time = Math.random() * 2;
	private float counter = 0f;
	private Direction dir = Direction.values()[(int) (Direction.values().length * Math.random())];

	public RandomWalkAI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Command updateOnceCommand(float deltaTime) {
		if ((counter += deltaTime) > time) {
			counter = 0;
			time = Math.random() * 2;
			dir = Direction.values()[(int) (Direction.values().length * Math.random())];
			return e -> e.move(dir);
		}
		return null;
	}

	@Override
	public Command updateContCommand(float deltaTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postCommand(Commands commandEnum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopCommand(Commands commandEnum) {
		// TODO Auto-generated method stub

	}

}
