package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.level.Node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Wander around the level
 *
 * @param node    current node wandering towards
 * @param visited a collection of nodes which have already been visited
 */
public record Wander(Node node, Collection<Node> visited) implements Goal {

	@Override
	public void performGoal(AIEntity entity) {
		float distanceTo = this.node.distanceTo(entity.position);
		boolean reachedNode = distanceTo < entity.radius();
		//Node reached - pick a new one
		if (reachedNode) {
			List<Node> edges = this.node.edges();
			LinkedList<Node> connectedNodes = new LinkedList<>(edges);
			connectedNodes.removeIf(this.visited::contains);

			Node nextNode;

			if (connectedNodes.isEmpty()) {
				//We've reached a terminal leaf room. Turn back and forget which nodes we've already visited
				int index = entity.game.main.random.nextInt(0, edges.size());

				nextNode = edges.get(index);
				this.visited.clear();

			} else {
				int index = entity.game.main.random.nextInt(0, connectedNodes.size());

				nextNode = connectedNodes.get(index);
			}

			this.visited.add(this.node);
			entity.wander(nextNode, this.visited);
			entity.moveTowards(nextNode.x(), nextNode.y());
		}
		entity.moveTowards(this.node.x(), this.node.y());
	}
}
