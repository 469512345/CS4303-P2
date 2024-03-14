package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.Entity;
import processing.core.PVector;

/**
 * Flee from another target
 *
 * @param enemy enemy to flee from
 */
public record Flee(Entity enemy) implements Goal {
	@Override
	public void performGoal(AIEntity entity) {
		if(entity.hasLineOfSight(this.enemy)) {
			//Move the entity in the opposite direction to the enemy
			PVector.sub(entity.position, this.enemy.position, entity.velocity);
			entity.velocity.limit(entity.velocityMagnitude());
		} else {
			entity.recalculateGoal();
		}
	}
}
