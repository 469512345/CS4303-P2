package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import processing.core.PVector;

/**
 * A member of the last family
 */
public abstract class Family extends AIEntity {
	/**
	 * Construct a family member
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Family(GameScreen game, PVector position) {
		super(game, position);
	}
}
