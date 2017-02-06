package com.mygdx.game.slotmachine;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class SlotMachineControllerAdapter extends ControllerAdapter {

	private SlotMachine slotMachine;

	public SlotMachineControllerAdapter(SlotMachine slotMachine) {
		this.slotMachine = slotMachine;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		if (buttonIndex == 11) {
			slotMachine.getGame().setToWorld();
		} else if (buttonIndex == 0) {
			slotMachine.pushSlotButton(0);
		} else if (buttonIndex == 6) {
			slotMachine.pushSlotButton(1);
		} else if (buttonIndex == 10) {
			slotMachine.pushSlotButton(2);
		} else
			return false;

		return true;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		if (axisIndex == 2) {
			if (value == 1)
				slotMachine.startSlots();
			else if (value == -1)
				slotMachine.tiltSlots();
			else
				slotMachine.untilt();
			return true;
		} else
			return false;
	}

}
