package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.level.AStar;
import cs4303.p2.game.level.Node;

import java.util.LinkedList;

/**
 * Target another target, with XRay vision, allowing to track even when outside of line of sight
 *
 * @param target target to track
 */
public record TargetXRay(Entity target) implements Goal {
	@Override
	public void performGoal(AIEntity entity) {
		//If we can see the target, then move onto targetting with line of sight
		if (entity.hasLineOfSight(this.target)) {
			entity.moveTowards(this.target.position.x, this.target.position.y);
			return;
		}

		Node startNode = entity.game.level.closestNodeTo(entity.position.x, entity.position.y, true);
		if (startNode == null) { // Shouldn't ever happen, but exit if unable to find start node in line of sight
			return;
		}
		Node endNode = entity.game.level.closestNodeTo(this.target.position.x, this.target.position.y, true);
		if (endNode == null) { // Shouldn't ever happen, but exit if unable to find end node in line of sight
			return;
		}

		LinkedList<Node> path = AStar.shortestPathBetween(startNode, endNode);
		if (path == null) { // Shouldn't ever happen, but exit if unable to find end node in line of sight
			return;
		}

		Node target = startNode;

		//Keep removing any nodes we have line of sight to. The closest node from above may not be the most direct path
		while (!path.isEmpty() && entity.hasLineOfSight(path.peekFirst())) {
			target = path.pollFirst();
		}

		entity.moveTowards(target.x(), target.y());
	}
}
