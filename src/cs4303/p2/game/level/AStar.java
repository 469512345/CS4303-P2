package cs4303.p2.game.level;

import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
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
	@Nullable
	public static <T extends AStarNode<T>> LinkedList<T> shortestPathBetween(@NotNull T start, @NotNull T end) {
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
	@Nullable
	public static <T extends AStarNode<T>> LinkedList<T> shortestPathBetween(
		@NotNull T start,
		@NotNull T end,
		@NotNull Predicate<T> nodeFilter
	) {
		//If the end node doesn't pass the filter, perform a breadth first search to find a connected node that does. This won't be exact, but good enough
		if (!nodeFilter.test(end)) {
			List<T> currentEdge = end.edges();
			LinkedList<T> closed = new LinkedList<>();
			LinkedList<T> newEdge = new LinkedList<>();
			boolean foundReplacementEnd = false;
			while (!foundReplacementEnd) {
				for (T node : currentEdge) {
					if (foundReplacementEnd) {
						break;
					}
					for (T connected : node.edges()) {
						if (newEdge.contains(connected) || currentEdge.contains(connected) || closed.contains(connected)) {
							continue;
						}
						if (nodeFilter.test(connected)) {
							end = connected;
							foundReplacementEnd = true;
							break;
						} else {
							newEdge.add(connected);
						}
					}
				}
				closed.addAll(currentEdge);
				currentEdge.clear();
				currentEdge.addAll(newEdge);
				newEdge.clear();
			}
		}

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
				.min(Comparator.comparing(AStarNodeInfo::totalCost));

			if (min.isPresent()) {
				current = min.get();
			} else {
				// If the open list is empty, then there is no path to the destination node.
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
		@NotNull
		public static <T extends AStarNode<T>> AStarNodeInfo<T> next(
			@NotNull T node,
			@NotNull AStarNodeInfo<T> previous,
			T endNode
		) {
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
		@NotNull
		public static <T extends AStarNode<T>> AStarNodeInfo<T> startNode(@NotNull T startNode, T endNode) {
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
