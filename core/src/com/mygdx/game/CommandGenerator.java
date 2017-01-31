package com.mygdx.game;

/**
 * Interface to be implemented by Structures which can determine an entity's
 * next operation like InputHandlers or AI.
 * 
 * @author mgadm
 *
 */
public interface CommandGenerator {

	/**
	 * This Method should generate a {@link Command} Object to represent the
	 * entity's next operation to execute once (i.E. to interact with another
	 * Entity) as opposed to continuous operations (like moving etc). Should be
	 * called once during the update method of an entity.
	 * 
	 * @param deltaTime
	 *            The time elapsed since the last update call.
	 * @return
	 */
	Command updateOnceCommand(float deltaTime);

	/**
	 * This Method should generate a {@link Command} Object to represent the
	 * entity's next operation to execute continuously (i.E. movement) as
	 * opposed to one-time operations (like interacting etc). Should be called
	 * once during the update method of an entity.
	 * 
	 * @param deltaTime
	 *            The time elapsed since the last update call.
	 * @return
	 */
	Command updateContCommand(float deltaTime);
}
