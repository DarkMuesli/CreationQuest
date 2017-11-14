package com.mygdx.game.slotmachine;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class SlotMachineControllerAdapter extends ControllerAdapter {

	private SlotMachine slotMachine;

	SlotMachineControllerAdapter(SlotMachine slotMachine) {
		this.slotMachine = slotMachine;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		if (buttonIndex == 0) {
			slotMachine.getGame().setToWorld();
		} else if (buttonIndex == 2) {
			slotMachine.pushReelButton(0);
		} else if (buttonIndex == 3) {
			slotMachine.pushReelButton(1);
		} else if (buttonIndex == 1) {
			slotMachine.pushReelButton(2);
		} else
			return false;

		return true;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		if (axisIndex == 2 || axisIndex == 0) {
			if (value == 1)
				slotMachine.startReels();
			else if (value == -1)
				slotMachine.tiltReels();
			else
				slotMachine.untilt();
			return true;
		} else
			return false;
	}

}
