package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

/**
 * Abstract entity which has AI
 */
public abstract class AIEntity extends Entity {

	protected PVector target = null;

	/**
	 * Construct an entity
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public AIEntity(GameScreen game, PVector position) {
		super(game, position);
	}

	protected void moveToTarget() {

	}
}
