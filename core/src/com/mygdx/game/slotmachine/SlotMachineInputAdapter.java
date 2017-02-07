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
		} else if (keycode == Keys.SPACE) {
			slotMachine.startSlots();
		} else if (keycode == Keys.ENTER) {
			slotMachine.stopSlots();
		} else if (keycode == Keys.NUM_1) {
			slotMachine.pushSlotButton(0);
		} else if (keycode == Keys.NUM_2) {
			slotMachine.pushSlotButton(1);
		} else if (keycode == Keys.NUM_3) {
			slotMachine.pushSlotButton(2);
		} else if (keycode == Keys.UP) {
			slotMachine.tiltSlots();
		} else if (keycode == Keys.DOWN) {
			slotMachine.startSlots();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.UP) {
			slotMachine.untilt();
			return true;
		}

		return false;
	}

}
