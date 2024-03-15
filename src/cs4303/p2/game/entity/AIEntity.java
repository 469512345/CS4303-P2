package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.ai.Flee;
import cs4303.p2.game.entity.ai.Goal;
import cs4303.p2.game.entity.ai.TargetSight;
import cs4303.p2.game.entity.ai.TargetXRay;
import cs4303.p2.game.entity.ai.Wander;
import cs4303.p2.game.level.AStar;
import cs4303.p2.game.level.Node;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
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

	protected long ticks;

	protected LinkedList<IntPoint> path;
	protected IntPoint endPoint;

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

	/**
	 * Whether this entity should run its AI this update
	 *
	 * @return true if this entity should run its AI this update, false otherwise
	 */
	public boolean shouldRunAI() {
		return this.ticks++ % this.game.main.CALCULATE_AI_EVERY_TICKS == 0;
	}

	@Override
	public final void update() {
		if (this.shouldRunAI()) {
			this.recalculateGoal();
			if (this.goal != null) {
				this.goal.performGoal(this);
			}
		} else {
			this.moveAlongPath();
		}
	}

	/**
	 * Recalculate the goal for this target
	 */
	public abstract void recalculateGoal();

	/**
	 * Get the nearest entity in a collection that this entity can see
	 *
	 * @param entities collection of entities
	 * @param <T>      entity type
	 *
	 * @return nearest entity that this entity can see, or null if no entities can be seen
	 */
	@Nullable
	public <T extends Entity> T nearestInLineOfSight(Collection<T> entities) {
		float nearestDistance = Float.MAX_VALUE;
		T nearestEntity = null;
		for (T entity : entities) {
			if (this.hasLineOfSight(entity)) {
				float distance = this.distanceTo(entity);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestEntity = entity;
				}
			}
		}
		return nearestEntity;
	}

	public <T extends Entity> T nearestCanSee(Collection<T> entities) {
		float nearestDistance = Float.MAX_VALUE;
		T nearestEntity = null;
		for (T entity : entities) {
			if (this.canSee(entity)) {
				float distance = this.distanceTo(entity);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestEntity = entity;
				}
			}
		}
		return nearestEntity;
	}

	public <T extends Entity> T nearestKnowsLocation(Collection<T> entities) {
		float nearestDistance = Float.MAX_VALUE;
		T nearestEntity = null;
		for (T entity : entities) {
			if (this.knowsLocationOf(entity)) {
				float distance = this.distanceTo(entity);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestEntity = entity;
				}
			}
		}
		return nearestEntity;
	}

	/**
	 * Get the distance between this entity and another entity
	 *
	 * @param other other entity
	 *
	 * @return Euclidean distance between this entity and another entity
	 */
	public float distanceTo(Entity other) {
		return this.position.dist(other.position);
	}

	@Override
	protected void drawBase() {
		super.drawBase();
		if (!this.game.main.SHOW_PATHFINDING_INFO) {
			return;
		}
		if (this.goal != null) {
			this.game.text(this.goal.getClass()
					.getSimpleName())
				.at(this.position)
				.size(5)
				.fill(Color.BLACK)
				.draw();
		} else {
			this.game.text("null")
				.at(this.position)
				.size(5)
				.fill(Color.BLACK)
				.draw();
		}
		if (this.goal instanceof Wander wander) {
			this.game.line()
				.from(this.position)
				.to(wander.node()
						.x(),
					wander.node()
						.y()
				)
				.stroke(Color.GREEN)
				.draw();
		}
		if (this.endPoint != null) {
			this.game.line()
				.from(this.position)
				.to(this.endPoint.x, this.endPoint.y)
				.stroke(Color.RED)
				.draw();
		}
		if (this.path != null && !this.path.isEmpty()) {
			IntPoint next = this.path.peekFirst();
			this.game.line()
				.from(this.position)
				.to(next.x, next.y)
				.stroke(Color.BLUE)
				.draw();
			for (IntPoint point : this.path) {
				this.game.ellipse()
					.at(point.x, point.y)
					.fill(Color.CYAN)
					.radius(1)
					.draw();
			}
		}
	}

	public void wander() {
		//If we are already wandering, then don't bother to change it
		if(this.goal instanceof Wander) {
			return;
		}
		Node closest = this.game.level.closestNodeTo(this.position.x, this.position.y, true);
		if (closest != null) {
			this.wander(closest, new ArrayList<>());
		}
	}

	public void wander(Node node, Collection<Node> visited) {
		this.goal = new Wander(node, visited);
	}

	public void target(Entity target) {
		boolean xray = switch (target.type()) {
			case HUMAN -> this.canSeeHumansThroughWalls();
			case ROBOT -> this.canSeeRobotsThroughWalls();
		};
		if (xray) {
			this.targetXray(target);
		} else {
			this.targetSight(target);
		}
	}

	public void targetSight(Entity target) {
		this.goal = new TargetSight(target, target.copyPosition());
	}

	public void targetXray(Entity target) {
		this.goal = new TargetXRay(target);
	}

	public void fleeFrom(Entity entity) {
		this.goal = new Flee(entity);
	}

	/**
	 * Move towards a specific point within line of sight. This will use A* to path-find around any obstacles in the
	 * room, and move towards it according to {@link #velocityMagnitude()}.
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 */
	public void moveTowards(float x, float y) {
		IntPoint start = IntPoint.of(this.position);
		this.endPoint = IntPoint.of(x, y);

		this.path = AStar.shortestPathBetween(
			start,
			this.endPoint,
			point -> { // Filter to only points where this enemy wouldn't collide with any obstacles
				Circle circle = Circle.of(point.x, point.y, this.radius());
				return this.game.level.collidesWithObstacle(circle) == null && this.game.level.collidesWithWall(circle) == null;
			}
		);
		this.moveAlongPath();
	}

	private void moveAlongPath() {
		if (this.path == null) {
			return;
		}

		//Euclidian distance from current position to target position
		float currentCost = this.endPoint.costTo(this.position);
		//Ignore any points in the path which are not closer than our current position
		while (!this.path.isEmpty() && this.path.peekFirst()
			.costTo(this.endPoint) > currentCost) {
			this.path.pollFirst();
		}

		IntPoint destination = this.path.peekFirst();
		if (destination == null) {
			return;
		}

		Iterator<IntPoint> iterator = this.path.iterator();

		while (iterator.hasNext() && destination.costTo(this.position) < this.velocityMagnitude()) {
			destination = iterator.next();
			iterator.remove();
		}

		this.velocity.set(destination.x - this.position.x, destination.y - this.position.y)
			.setMag(this.velocityMagnitude());
		this.move();
	}

	/**
	 * Whether this entity knows the location of another entity. This can either be by being able to see another entity,
	 * or knowing its last location as part of the {@link TargetSight#lastKnownLocation()}.
	 *
	 * @param entity entity to test
	 *
	 * @return true if this entity can see the other entity, or knows where it is according to
	 * {@link TargetSight#lastKnownLocation()}.
	 */
	public boolean knowsLocationOf(Entity entity) {
		if (!entity.isActive()) {
			return false;
		}
		if (this.canSee(entity)) {
			return true;
		}
		if (this.goal instanceof TargetSight targetSight) {
			if (!this.containsPoint(targetSight.lastKnownLocation())) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 */
	protected record IntPoint(int x, int y) implements AStar.AStarNode<IntPoint> {

		/**
		 * Resolution of int points. To save computation, we can divide the number of pixels in each axis
		 */
		private static final int INT_POINT_RESOLUTION = 7;

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
			return Math.round((float) number / INT_POINT_RESOLUTION) * INT_POINT_RESOLUTION;
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
