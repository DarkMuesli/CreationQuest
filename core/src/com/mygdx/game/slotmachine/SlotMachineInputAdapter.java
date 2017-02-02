package com.mygdx.game.slotmachine;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class SlotMachineInputAdapter extends InputAdapter {

	private SlotMachine slotMachine;

	public SlotMachineInputAdapter(SlotMachine slotMachine) {
		this.slotMachine = slotMachine;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.TAB) {
			slotMachine.getGame().setToWorld();
			return true;
		} else
			return false;
	}

}
