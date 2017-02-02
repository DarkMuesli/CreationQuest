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
			return true;
		} else
			return false;
	}

}
