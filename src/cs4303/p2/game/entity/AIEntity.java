package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.ai.Goal;
import cs4303.p2.game.entity.ai.Patrol;
import processing.core.PVector;

/**
 * Abstract entity which has AI
 */
public abstract class AIEntity extends Entity {

	/**
	 * Current goal of this entity
	 */
	protected Goal goal = new Patrol();

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
