package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.level.Node;
import processing.core.PVector;

/**
 * Flee from another target
 *
 * @param enemy enemy to flee from
 */
public record Flee(Entity enemy) implements Goal {
	@Override
	public void performGoal(AIEntity entity) {
		//If we can see the enemy, flee from it
		if (entity.hasLineOfSight(this.enemy)) {
			//Try to find a node on the map that we can see and is further away from the enemy than me
			Node furthestNode = null;
			float furthestDistance = Float.MIN_VALUE;
			for (Node node : entity.game.level.nodes) {
				if (entity.hasLineOfSight(node)) {
					float distanceToEnemy = node.distanceTo(this.enemy.position);
					float distanceToMe = node.distanceTo(entity.position);
					if (distanceToEnemy > distanceToMe && distanceToEnemy > furthestDistance) {
						furthestNode = node;
						furthestDistance = distanceToEnemy;
					}
				}
			}
			//If such a node exists, move to it!
			if (furthestNode != null) {
				entity.moveTowards(furthestNode.y(), furthestNode.y());
			} else {
				//Otherwise Move the entity in the opposite direction to the enemy
				PVector.sub(entity.position, this.enemy.position, entity.velocity);
				entity.velocity.setMag(10);

				PVector destination = PVector.add(entity.position, entity.velocity);
				entity.moveTowards(destination.x, destination.y);
			}
		} else {
			//If we can't see the enemy, recalculate our goal
			entity.recalculateGoal();
		}
	}
}
