package de.thkoeln.creationquest.slotmachine;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class SlotMachineInputAdapter extends InputAdapter {

    private SlotMachine slotMachine;

    SlotMachineInputAdapter(SlotMachine slotMachine) {
        this.slotMachine = slotMachine;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.TAB) {
            slotMachine.getGame().setToWorld();
            return true;
        } else if (keycode == Keys.SPACE) {
            slotMachine.startReels();
        } else if (keycode == Keys.ENTER) {
            slotMachine.stopReels();
        } else if (keycode == Keys.NUM_1) {
            slotMachine.pushReelButton(0);
        } else if (keycode == Keys.NUM_2) {
            slotMachine.pushReelButton(1);
        } else if (keycode == Keys.NUM_3) {
            slotMachine.pushReelButton(2);
        } else if (keycode == Keys.UP) {
            slotMachine.tiltReels();
        } else if (keycode == Keys.DOWN) {
            slotMachine.startReels();
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
