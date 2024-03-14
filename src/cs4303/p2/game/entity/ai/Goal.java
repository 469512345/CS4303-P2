package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;

/**
 * Base interface for goals
 */
public sealed interface Goal permits Flee, Wander, TargetSight, TargetXRay {

	/**
	 * Perform the goal on the enemy, updating position and velocity accordingly
	 *
	 * @param entity
	 */
	void performGoal(AIEntity entity);

}
