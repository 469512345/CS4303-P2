package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.level.Node;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Line;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

/**
 * Flee from another target
 *
 * @param enemy enemy to flee from
 */
public record Flee(Entity enemy) implements Goal {
	@Override
	public void performGoal(@NotNull AIEntity entity) {
		//If we can see the enemy, flee from it
		if (entity.hasLineOfSight(this.enemy)) {
			float myDistanceToEnemy = entity.distanceTo(this.enemy);

			//Try to find a node on the map that we can see and is further away from the enemy than me
			Node furthestNode = null;
			float furthestDistance = Float.MIN_VALUE;
			for (Node node : entity.game.level.nodes) {
				if (entity.hasLineOfSight(node)) {
					float distanceToEnemy = node.distanceTo(this.enemy.position);
					float distanceToMe = node.distanceTo(entity.position);
					if (distanceToEnemy > distanceToMe && distanceToEnemy > myDistanceToEnemy && distanceToEnemy > furthestDistance) {
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

				//Calculate the destination as a point 10 units from current position
				PVector destination = PVector.add(entity.position, entity.velocity);

				//Calculate the nearest intersection with a wall to the entity
				Line trajectory = Line.of(destination, entity.position);

				PVector nearestIntersection = null;
				float nearestDistance = Float.MAX_VALUE;

				for (HorizontalLine horizontalWall : entity.game.level.horizontalWalls) {
					PVector intersection = horizontalWall.intersection(trajectory);
					if (intersection != null) {
						float distance = intersection.dist(entity.position);
						if (distance < nearestDistance) {
							nearestIntersection = intersection;
							nearestDistance = distance;
						}
					}
				}
				for (VerticalLine verticalLine : entity.game.level.verticalWalls) {
					PVector intersection = verticalLine.intersection(trajectory);
					if (intersection != null) {
						float distance = intersection.dist(entity.position);
						if (distance < nearestDistance) {
							nearestIntersection = intersection;
							nearestDistance = distance;
						}
					}
				}

				//If there was any intersection, set the destination to it to keep the entity in bounds
				if (nearestIntersection != null) {
					destination = nearestIntersection;
				}
				entity.moveTowards(destination.x, destination.y);
			}
		} else {
			//If we can't see the enemy, recalculate our goal
			entity.recalculateGoal();
		}
	}
}
