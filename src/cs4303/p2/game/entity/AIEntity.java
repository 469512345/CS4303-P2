package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.ai.Goal;
import cs4303.p2.game.level.AStar;
import cs4303.p2.game.level.Node;
import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract target which has AI
 */
public abstract class AIEntity extends Entity {

	/**
	 * Current goal of this target
	 */
	protected Goal goal;

	public long ticks;

	private LinkedList<IntPoint> path;

	/**
	 * Construct an target
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public AIEntity(GameScreen game, PVector position) {
		super(game, position);
		//Initialise the tick counter to a random number in the range, so the AI for each entity isn't scheduled to run in the same tick
		this.ticks = this.game.main.random.nextInt(0, this.game.main.CALCULATE_AI_EVERY_TICKS);
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public void update() {
		if (this.goal == null) {
			this.recalculateGoal();
		}
		if (this.goal != null && this.shouldRunAI()) {
			this.goal.performGoal(this);
		}
	}

	/**
	 * Whether this entity should run its AI this update
	 *
	 * @return true if this entity should run its AI this update, false otherwise
	 */
	public boolean shouldRunAI() {
		return this.ticks++ % this.game.main.CALCULATE_AI_EVERY_TICKS == 0;
	}

	public void reRunAIOrFollowPath() {
		if (this.shouldRunAI()) {
			this.goal.performGoal(this);
		} else {
			this.moveAlongPath();
		}
	}

	/**
	 * Recalculate the goal for this target
	 */
	public abstract void recalculateGoal();

	/**
	 * Move towards a specific point within line of sight. This will use A* to path-find around any obstacles in the
	 * room, and move towards it according to {@link #velocityMagnitude()}.
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 */
	public void moveTowards(float x, float y) {
		IntPoint start = IntPoint.of(this.position);
		IntPoint end = IntPoint.of(x, y);

		LinkedList<IntPoint> path = AStar.shortestPathBetween(
			start,
			end,
			(point) -> { // Filter to only points where this enemy wouldn't collide with any obstacles
				Circle circle = Circle.of(point.x, point.y, this.radius());
				return this.game.level.collidesWithObstacle(circle) == null && this.game.level.collidesWithWall(circle) == null;
			}
		);
		//The destination was unreachable - maybe due to obstacles. Do nothing
		if (path == null) {
			return;
		}

		this.path = path;
		this.moveAlongPath();
	}

	private void moveAlongPath() {
		if (this.path == null) {
			return;
		}
		IntPoint destination = this.path.pollFirst();
		if (destination == null) {
			return;
		}

		Iterator<IntPoint> iterator = this.path.iterator();

		while (iterator.hasNext() && destination.costTo(this.position) < this.velocityMagnitude()) {
			destination = iterator.next();
		}

		this.velocity.set(destination.x - this.position.x, destination.y - this.position.y)
			.setMag(this.velocityMagnitude());
		this.move();
	}

	/**
	 *
	 */
	record IntPoint(int x, int y) implements AStar.AStarNode<IntPoint> {

		/**
		 * Resolution of int points. To save computation, we can divide the number of pixels in each axis
		 */
		private static final int INT_POINT_RESOLUTION = 1;

		@Override
		public float costTo(IntPoint other) {
			int diffX = this.x - other.x;
			int diffY = this.y - other.y;
			return (float) Math.sqrt(diffX * diffX + diffY * diffY);
		}

		public float costTo(PVector position) {
			float diffX = position.x - this.x;
			float diffY = position.y - this.y;
			return (float) Math.sqrt(diffX * diffX + diffY * diffY);
		}

		@Override
		public List<IntPoint> edges() {
			ArrayList<IntPoint> edges = new ArrayList<>(4);

			edges.add(new IntPoint(this.x + INT_POINT_RESOLUTION, this.y));
			edges.add(new IntPoint(this.x - INT_POINT_RESOLUTION, this.y));
			edges.add(new IntPoint(this.x, this.y + INT_POINT_RESOLUTION));
			edges.add(new IntPoint(this.x, this.y - INT_POINT_RESOLUTION));

			return edges;
		}

		/**
		 * Convert an integer to the resolution of {@link #INT_POINT_RESOLUTION}.
		 *
		 * @param number number convert
		 *
		 * @return converted integer, which is a multiple of {@link #INT_POINT_RESOLUTION}.
		 */
		public static int convert(int number) {
			return (number / INT_POINT_RESOLUTION) * INT_POINT_RESOLUTION;
		}

		/**
		 * Create a point, and automatically convert to the resolution of {@link #INT_POINT_RESOLUTION}.
		 *
		 * @param x x coordinate of point
		 * @param y y coordinate of point
		 *
		 * @return int point to the resolution of {@link #INT_POINT_RESOLUTION}.
		 */
		public static IntPoint of(float x, float y) {
			return new IntPoint(
				convert((int) x),
				convert((int) y)
			);
		}

		public static IntPoint of(PVector point) {
			return IntPoint.of(point.x, point.y);
		}

		public static IntPoint of(Node node) {
			return IntPoint.of(node.x(), node.y());
		}
	}

}
