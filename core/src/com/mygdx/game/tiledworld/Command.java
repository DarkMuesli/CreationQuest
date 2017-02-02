/**
 * 
 */
package com.mygdx.game.tiledworld;

/**
 * This interface provides the single method {@link Command#execute()}, needed
 * to define any concrete {@link Command} Class as defined by the Command
 * Programming Pattern.<br>
 * (see <code>Robert Nystrom, 2014, Game Programming Patterns</code>)
 * 
 * @author Matthias Gross
 *
 */

public interface Command {

	public void execute(Entity e);
}
