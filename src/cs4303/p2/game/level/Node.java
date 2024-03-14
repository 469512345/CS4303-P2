package cs4303.p2.game.level;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * A node in the pathfinding tree
 *
 * @param x     x coordinate of node
 * @param y     y coordinate of node
 * @param edges undirected edges from this node to other nodes
 */
public record Node(float x, float y, Collection<Node> edges) {

	/**
	 * Create a node at a position
	 *
	 * @param position position of node
	 */
	public Node(PVector position) {
		this(position.x, position.y, new ArrayList<>());
	}

	/**
	 * Add an edge between this node and another node. As the world graph is undirected, this will add the reverse edge
	 * at the same time
	 *
	 * @param other node to connect this node to
	 */
	public void connectTo(Node other) {
		this.edges.add(other);
		other.edges.add(this);
	}

	/**
	 * Calculate the cost to another node
	 *
	 * @param other other node
	 *
	 * @return cost between the two nodes
	 */
	public float costTo(Node other) {
		return this.distanceTo(other.x, other.y);
	}

	/**
	 * Calculate the distance to a point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return distance to the point
	 */
	public float distanceTo(float x, float y) {
		return (float) Math.sqrt(this.distanceSqTo(x, y));
	}

	/**
	 * Calculate the distance squared to a point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return distance squared to the point
	 */
	public float distanceSqTo(float x, float y) {
		float diffX = x - this.x;
		float diffY = y - this.y;

		return diffX * diffX + diffY * diffY;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
			.add("x=" + x)
			.add("y=" + y)
			.toString();
	}
}
