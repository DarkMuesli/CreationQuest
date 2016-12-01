package com.mygdx.game;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;

/**
 * Singleton Manager Class to handle trigger-events.
 * 
 * @author mgadm
 *
 */
public class EventManager implements Observer {

	private static EventManager instance;

	private EventManager() {
	}

	/**
	 * @return the single instance of this class.
	 */
	public static EventManager instance() {
		if (instance == null)
			instance = new EventManager();
		return instance;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player && arg instanceof MapObject)
			if (((MapObject) arg).getProperties().get("type").equals("Trigger"))
				System.out.println("Triggered");
	}

	/**
	 * Checks all available trigger-objects on the map. If the player is inside
	 * one, the corresponding event is fired.
	 * 
	 * @param triggerObjects
	 *            {@link List} of available trigger objects
	 * @param player
	 *            reference to the {@link Player} instance
	 */
	public void checkTrigger(List<MapObject> triggerObjects, Player player) {
		for (MapObject obj : triggerObjects) {
			MapProperties objProp = obj.getProperties();

			Rectangle objPixelPos = new Rectangle(objProp.get("x", float.class), objProp.get("y", float.class),
					objProp.get("width", float.class), objProp.get("height", float.class));

			if (objPixelPos.contains(player.getPixelCenter())) {

				triggerEvent(objProp.get("Trigger", String.class));

			}
		}
	}

	/**
	 * Fires the event with the given name.
	 * 
	 * @param eventName
	 *            Name of the Event to fire
	 */
	public void triggerEvent(String eventName) {
		switch (eventName) {
		case "PlayMusic":
			System.out.println("Triggered! Jetzt wird Musik gespielt!");
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/tng_chime_clean.mp3"));
			long id = sound.play();
			sound.setPitch(id, 1f + ((float) Math.random() * 0.1f) - 0.05f);
			break;

		default:
			System.out.println("Keine Musik...");
			break;
		}
	}

}
