package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import processing.core.PVector;

/**
 * Base class for robots
 */
public abstract class Robot extends AIEntity {
	/**
	 * Construct an enemy
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Robot(GameScreen game, PVector position) {
		super(game, position);
	}
}
