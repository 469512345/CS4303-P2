package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.Entity;
import processing.core.PVector;

/**
 * Target an enemy while it is in line of sight
 *
 * @param target            target to target
 * @param lastKnownLocation last known location of the
 */
public record TargetSight(Entity target, PVector lastKnownLocation) implements Goal {
	@Override
	public void performGoal(AIEntity entity) {
		if (entity.hasLineOfSight(this.target)) {
			//Move towards the enemy and update its last known position
			entity.moveTowards(this.target.position.x, this.target.position.y);
			this.lastKnownLocation.set(this.target.position.copy());
		} else {
			if (entity.containsPoint(this.lastKnownLocation)) {
				entity.recalculateGoal();
			} else {
				entity.moveTowards(this.lastKnownLocation.x, this.lastKnownLocation.y);
			}
		}
	}
}
