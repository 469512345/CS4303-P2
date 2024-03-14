package cs4303.p2.game.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Predicate;

/**
 * A* implementation
 */
public class AStar {

	/**
	 * Interface for nodes which can be used in A* search
	 *
	 * @param <T> self
	 */
	public interface AStarNode<T extends AStarNode<T>> {

		/**
		 * Calculate the cost between this node and another node
		 *
		 * @param other other node
		 *
		 * @return cost between this node and the other node
		 */
		float costTo(T other);

		/**
		 * Get the nodes connected to this node
		 *
		 * @return connected nodes
		 */
		List<T> edges();

	}

	/**
	 * Calculate the shortest path between two nodes using A* search
	 *
	 * @param start start node
	 * @param end   end node
	 * @param <T>   type of node being search through
	 *
	 * @return list containing the nodes in ascending order (i.e., start node at front of list, end node last in list)
	 */
	public static <T extends AStarNode<T>> LinkedList<T> shortestPathBetween(T start, T end) {
		return shortestPathBetween(start, end, (node) -> true);
	}

	/**
	 * Calculate the shortest path between two nodes using A* search
	 *
	 * @param start      start node
	 * @param end        end node
	 * @param nodeFilter filter for nodes
	 * @param <T>        type of node being search through
	 *
	 * @return list containing the nodes in ascending order (i.e., start node at front of list, end node last in list)
	 */
	public static <T extends AStarNode<T>> LinkedList<T> shortestPathBetween(T start, T end, Predicate<T> nodeFilter) {
		ArrayList<AStarNodeInfo<T>> open = new ArrayList<>();
		ArrayList<AStarNodeInfo<T>> closed = new ArrayList<>();

		AStarNodeInfo<T> current = AStarNodeInfo.startNode(start, end);
		open.add(current);

		//Keep going until we have reached the end node
		while (!current.node.equals(end)) {
			open.remove(current);
			closed.add(current);
			for (T connected : current.node.edges()) {
				if (!nodeFilter.test(connected)) {
					continue;
				}
				boolean alreadySeen = false;
				for (AStarNodeInfo<T> closedNode : closed) {
					if (closedNode.node.equals(connected)) {
						alreadySeen = true;
						break;
					}
				}
				if (!alreadySeen) {
					for (AStarNodeInfo<T> openNode : open) {
						if (openNode.node.equals(connected)) {
							alreadySeen = true;
							break;
						}
					}
				}
				if (!alreadySeen) {
					open.add(AStarNodeInfo.next(connected, current, end));
				}
			}

			//Find the node in the open list with the lowest total cost
			Optional<AStarNodeInfo<T>> min = open.stream()
				.min((a, b) -> Float.compare(a.totalCost(), b.totalCost()));
			if (min.isPresent()) {
				current = min.get();
			} else {
				// If the open list is empty, then there is no path to the destination node.
				// This should never happen given the tree based room generation,
				// but this algorithm should still protect against this case
				return null;
			}
		}

		LinkedList<T> path = new LinkedList<>();
		while (current.previous != null) {
			path.addFirst(current.node);
			current = current.previous;
		}
		path.addFirst(current.node);

		return path;
	}

	/**
	 * A node used in the A* algorithm
	 *
	 * @param node
	 * @param previous
	 * @param heuristicCost
	 * @param edgeCost
	 * @param costSoFar
	 * @param <T>           type of node used in search
	 */
	record AStarNodeInfo<T extends AStarNode<T>>(
		T node,
		AStarNodeInfo<T> previous,
		float heuristicCost,
		float edgeCost,
		float costSoFar
	) {

		/**
		 * Get the total cost for this node
		 *
		 * @return total cost for this node
		 */
		public float totalCost() {
			return this.heuristicCost + this.edgeCost + this.costSoFar;
		}

		/**
		 * Create a node info wrapper for a new node
		 *
		 * @param node     new node
		 * @param previous previous node from the new node
		 * @param endNode  end node
		 * @param <T>      type of node
		 *
		 * @return node info wrapper for new node
		 */
		public static <T extends AStarNode<T>> AStarNodeInfo<T> next(T node, AStarNodeInfo<T> previous, T endNode) {
			return new AStarNodeInfo<>(
				node,
				previous,
				node.costTo(endNode),
				node.costTo(previous.node),
				previous.totalCost()
			);
		}

		/**
		 * Create the info for the start node in A*
		 *
		 * @param startNode start node
		 * @param endNode   end node
		 * @param <T>       type of node
		 *
		 * @return A* start node
		 */
		public static <T extends AStarNode<T>> AStarNodeInfo<T> startNode(T startNode, T endNode) {
			return new AStarNodeInfo<>(
				startNode,
				null,
				startNode.costTo(endNode),
				0,
				0
			);
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", AStarNodeInfo.class.getSimpleName() + "[", "]")
				.add("node=" + this.node)
				.add("heuristicCost=" + this.heuristicCost)
				.add("edgeCost=" + this.edgeCost)
				.add("costSoFar=" + this.costSoFar)
				.add("totalCost=" + this.totalCost())
				.toString();
		}
	}

}
