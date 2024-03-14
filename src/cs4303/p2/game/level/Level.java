package cs4303.p2.game.level;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.Projectile;
import cs4303.p2.game.entity.family.Family;
import cs4303.p2.game.entity.robot.Robot;
import cs4303.p2.game.level.corridor.Corridor;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.game.level.room.Room;
import cs4303.p2.game.powerup.Powerup;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Line;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A level in the game
 */
public class Level {

	/**
	 * Game instance
	 */
	private final GameScreen game;
	/**
	 * Parameters describing level generation
	 */
	private final LevelInfo levelInfo;
	/**
	 * Root of the room tree
	 */
	private final Room root;
	/**
	 * Horizontal walls
	 */
	public final List<HorizontalLine> horizontalWalls = new LinkedList<>();
	/**
	 * Vertical walls
	 */
	public final List<VerticalLine> verticalWalls = new LinkedList<>();
	/**
	 * All of the walls on the level
	 */
	public final List<Collidable> walls = new LinkedList<>();
	/**
	 * Rooms
	 */
	private final List<LeafRoom> rooms = new LinkedList<>();
	/**
	 * Corridors
	 */
	private final List<Corridor> corridors = new LinkedList<>();
	/**
	 * Projectiles currently active in the world
	 */
	public final List<Projectile> projectiles = new LinkedList<>();
	/**
	 * The room that the player starts in
	 */
	public final LeafRoom startingRoom;
	/**
	 * Obstacles in the world
	 */
	private final List<Obstacle> obstacles = new LinkedList<>();
	/**
	 * Powerups in the world
	 */
	private final List<Powerup> powerups = new LinkedList<>();
	/**
	 * Family members in the world
	 */
	private final List<Family> family = new LinkedList<>();
	/**
	 * Robots in the world
	 */
	private final List<Robot> robots = new LinkedList<>();

	/**
	 * Create a new level
	 *
	 * @param game      game instance
	 * @param levelInfo level generation parameters
	 */
	public Level(GameScreen game, LevelInfo levelInfo) {
		this.game = game;
		this.levelInfo = levelInfo;
		this.root = Room.createRoot(this.game, this.levelInfo);

		this.root.appendWalls(this.horizontalWalls, this.verticalWalls);
		this.walls.addAll(this.horizontalWalls);
		this.walls.addAll(this.verticalWalls);

		Random random = this.game.main.random;

		this.root.appendRooms(this.rooms);
		LinkedList<LeafRoom> singlyConnectedRooms = new LinkedList<>(this.rooms);
		singlyConnectedRooms.removeIf(room -> room.corridors.size() != 1); //The starting room must only have 1 corridor
		this.startingRoom = singlyConnectedRooms.get(random.nextInt(singlyConnectedRooms.size()));

		for (LeafRoom room : this.rooms) {
			this.addObstaclesToRegion(room, this.levelInfo.minObstaclesPerRoom(), levelInfo.maxObstaclesPerRoom());
		}

		this.root.appendCorridors(this.corridors);

		for (Corridor corridor : this.corridors) {
			for (Rectangle segment : corridor.segments) {
				this.addObstaclesToRegion(
					segment,
					this.levelInfo.minObstaclesPerCorridor(),
					levelInfo.maxObstaclesPerCorridor()
				);
			}
		}
	}

	/**
	 * Add some obstacles to a region of the map
	 *
	 * @param region region to populate
	 * @param min    minimum number of obstacles
	 * @param max    maximum number of obstacles
	 */
	private void addObstaclesToRegion(Rectangle region, int min, int max) {
		Random random = this.game.main.random;
		int obstaclesToAdd = random.nextInt(min, max + 1);
		//Failed attempts to place an obstacle
		int failedAttempts = 0;
		while (obstaclesToAdd > 0) {
			//Abort if exceeded maximum number of failed attempts
			if (failedAttempts > this.game.main.OBSTACLE_MAX_FAILED_ATTEMPTS) {
				break;
			}
			//Get the smallest dimension of the room
			float minimumDimension = Math.min(region.height(), region.width());
			//The max radius for the obstacle should not exceed half the minimum dimension of the room
			float maxRadius = Math.min(this.levelInfo.maxObstacleRadius(), minimumDimension / 2);
			//Min radius cannot exceed max radius
			float minRadius = Math.min(this.levelInfo.minObstacleRadius(), maxRadius);
			float radius = random.nextFloat(minRadius, maxRadius);
			//Position the centre of the obstacle at least one radius from the edge of the room
			float positionX = random.nextFloat(region.minX() + radius, region.maxX() - radius);
			float positionY = random.nextFloat(region.minY() + radius, region.maxY() - radius);
			Obstacle obstacle = new Obstacle(this.game, new PVector(positionX, positionY), radius);

			//If this new obstacle intersects any existing obstacles then don't add it
			boolean conflictsWithExisting = false;
			for (Obstacle existingObstacle : this.obstacles) {
				if (obstacle.intersects(existingObstacle)) {
					conflictsWithExisting = true;
					break;
				}
			}
			if (conflictsWithExisting) {
				failedAttempts++;
			} else {
				this.obstacles.add(obstacle);
				obstaclesToAdd--;
			}
		}
	}

	/**
	 * Draw the level
	 */
	public void draw() {
		this.root.draw();
		this.drawWalls();
		this.drawProjectiles();
		this.drawFamily();
		this.drawRobots();
		this.drawPowerups();
		this.drawObstacles();
	}

	/**
	 * Draw the walls
	 */
	private void drawWalls() {
		LineBuilder line = this.game.line()
			.stroke(this.game.main.WALL_COLOR)
			.strokeWeight(this.game.main.WALL_STROKE_WIDTH);
		for (HorizontalLine horizontalWall : this.horizontalWalls) {
			line
				.from(horizontalWall.minX(), horizontalWall.y())
				.to(horizontalWall.maxX(), horizontalWall.y())
				.draw();
		}
		for (VerticalLine verticalWall : this.verticalWalls) {
			line.from(verticalWall.x(), verticalWall.minY())
				.to(verticalWall.x(), verticalWall.maxY())
				.draw();
		}
	}

	/**
	 * Draw the projectiles on screen
	 */
	private void drawProjectiles() {
		for (Projectile projectile : this.projectiles) {
			if(this.game.player.hasLineOfSight(projectile.centre())) {
				projectile.draw();
			}
		}
	}

	/**
	 * Draw powerups on screen
	 */
	private void drawPowerups() {
		for (Powerup powerup : this.powerups) {
			if(this.game.player.hasLineOfSight(powerup.centre())) {
				powerup.draw();
			}
		}
	}

	/**
	 * Draw the obstacles on the map
	 */
	private void drawObstacles() {
		for (Obstacle obstacle : this.obstacles) {
			if(this.game.player.hasLineOfSight(obstacle.centre())) {
				obstacle.draw();
			}
		}
	}

	/**
	 * Draw the family members on screen
	 */
	private void drawFamily() {
		for (Family familyMember : this.family) {
			if (this.game.player.canSee(familyMember)) {
				familyMember.draw();
			}
		}
	}

	/**
	 * Draw the robots on screen
	 */
	private void drawRobots() {
		for (Robot robot : this.robots) {
			if (this.game.player.canSee(robot)) {
				robot.draw();
			}
		}
	}

	/**
	 * Update this level
	 */
	public void update() {
		this.updateProjectiles();
		this.updatePowerups();
		this.updateObstacles();
		this.updateFamily();
		this.updateRobots();
	}

	/**
	 * Update any projectiles on screen
	 */
	private void updateProjectiles() {
		Iterator<Projectile> iterator = this.projectiles.iterator();
		while (iterator.hasNext()) {
			Projectile projectile = iterator.next();
			projectile.update();
			if (!projectile.expired() && projectile.intersects(this.game.player) && projectile.canHitPlayer()) {
				projectile.expire();
				this.game.die();
				//Clear the list of projectiles, so they are not there when the player respawns
				this.projectiles.clear();
				break;
			}
			for (Obstacle obstacle : this.obstacles) {
				if (projectile.intersects(obstacle)) {
					projectile.expire();
					obstacle.explode();
				}
			}
			if (projectile.expired()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Update the obstacles in the world, removing any which have been exploded
	 */
	private void updateObstacles() {
		Iterator<Obstacle> iterator = this.obstacles.iterator();
		while (iterator.hasNext()) {
			Obstacle obstacle = iterator.next();
			if (obstacle.intersects(this.game.player)) {
				this.game.die();
				obstacle.explode();
			}
			if (obstacle.exploded()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Update the powerups in the world, removing any which have been collected
	 */
	private void updatePowerups() {
		this.powerups.removeIf(Powerup::collected);
	}

	/**
	 * Update the family in the world
	 */
	private void updateFamily() {
		Iterator<Family> iterator = this.family.iterator();
		while (iterator.hasNext()) {
			Family familyMember = iterator.next();
			familyMember.update();
			if (familyMember.rescued()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Update any robots in the world
	 */
	private void updateRobots() {
		Iterator<Robot> iterator = this.robots.iterator();
		while (iterator.hasNext()) {
			Robot robot = iterator.next();
			robot.update();
			if (robot.dead()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Move a circular object with collision detection. This method will update the position accordingly based on the
	 * velocity and any collision detection that occurs. If a collision occurs, the object will be prevented from moving
	 * in the direction that the collision occurs in
	 *
	 * @param position      current position, will be modified if movement was possible
	 * @param velocity      current velocity, will not be changed
	 * @param radiusSquared radius squared of the circular object being moved
	 */
	public void moveNoBounce(PVector position, PVector velocity, float radiusSquared) {
		float newX = position.x + velocity.x;
		float newY = position.y + velocity.y;

		boolean breakX = false;
		boolean breakY = false;

		for (Collidable wall : this.walls) {
			if (!breakX && wall.closestDistanceSqFrom(newX, position.y) < radiusSquared) {
				newX = position.x;
				breakX = true;
			}
			if (!breakY && wall.closestDistanceSqFrom(position.x, newY) < radiusSquared) {
				newY = position.y;
				breakY = true;
			}
			if (breakX && breakY) {
				break;
			}
		}

		position.set(newX, newY);
	}

	/**
	 * Move a circular object with collision detection, bouncing if collisions occur. This method will update the
	 * position and velocity accordingly.
	 *
	 * @param position current position, will be modified if movement was possible
	 * @param velocity current velocity, will be modified if a bounce occurred
	 * @param radius   radius of circular object
	 *
	 * @return the number of bounces which were encountered
	 */
	public int moveBounce(PVector position, PVector velocity, float radius) {
		return this.moveBounce(position, velocity, radius, 0, velocity.mag(), new HashSet<>());
	}

	/**
	 * Inner recursive bounce implementation
	 *
	 * @param position         current position
	 * @param velocity         current velocity
	 * @param radius           radius of object
	 * @param count            how many bounces have happened so far
	 * @param initialMagnitude the original magnitude of the velocity
	 * @param surfaces         a collection of surfaces which have been bounced off in this iteration
	 *
	 * @return the number of bounces which were encountered
	 */
	private int moveBounce(
		PVector position,
		PVector velocity,
		float radius,
		int count,
		float initialMagnitude,
		Set<Collidable> surfaces
	) {
		//Add the radius to the length of the velocity when checking trajectory
		float mag = velocity.mag();
		velocity.setMag(mag + radius);

		float newX = position.x + velocity.x;
		float newY = position.y + velocity.y;

		Line trajectory = Line.of(position.x, position.y, newX, newY);

		//Axis of the bounce (if any)
		Axis bounceAxis = null;
		//Point at which a bounce is occurring
		PVector bouncePoint = null;

		for (HorizontalLine wall : this.horizontalWalls) {
			PVector intersection;
			if (!surfaces.contains(wall) && (intersection = wall.intersection(trajectory)) != null) {
				bounceAxis = Axis.HORIZONTAL;
				surfaces.add(wall);
				bouncePoint = intersection;
				break;
			}
		}
		if (bounceAxis == null) {
			for (VerticalLine wall : this.verticalWalls) {
				PVector intersection;
				if (!surfaces.contains(wall) && (intersection = wall.intersection(trajectory)) != null) {
					bounceAxis = Axis.VERTICAL;
					surfaces.add(wall);
					bouncePoint = intersection;
					break;
				}
			}
		}

		if (bounceAxis != null) {
			float magnitude = velocity.mag();

			//The bounce point doesn't take into account the radius of the particle,
			// so offset it by the radius in the opposite direction of velocity
			velocity.setMag(radius);
			bouncePoint.sub(velocity);

			//Calculate the distance between the starting position and the bounce point
			float diffX = bouncePoint.x - position.x;
			float diffY = bouncePoint.y - position.y;
			float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);

			//Reduce the magnitude of the velocity, so it will travel the remaining distance after the bounce
			velocity.setMag(magnitude - distance);

			//Adjust the direction of the velocity to perform the bounce
			if (bounceAxis == Axis.HORIZONTAL) {
				velocity.set(velocity.x, -velocity.y);
			} else {
				velocity.set(-velocity.x, velocity.y);
			}
			//Update the position to the bounce point
			position.set(bouncePoint);

			//Recursively try to perform more bounces
			return this.moveBounce(position, velocity, radius, count + 1, initialMagnitude, surfaces);
		}

		//If no bounces, then position is the new location, and restore the velocity to its initial
		// magnitude (if any bounces occurred then the magnitude would've been modified).
		position.set(newX, newY);
		velocity.setMag(initialMagnitude);
		//Return how many bounces occurred
		return count;
	}

	/**
	 * Calculate whether there is line of sight between two points
	 *
	 * @param point1 first point
	 * @param point2 second point
	 *
	 * @return true if the two points have line of sight, false if there is a wall in the way
	 */
	public boolean lineOfSightBetween(PVector point1, PVector point2) {
		return !this.lineIntersectsWalls(Line.of(point1, point2));
	}

	/**
	 * Calculate whether there is line of sight between two points
	 *
	 * @param x1 x coordinate of point 1
	 * @param y1 y coordinate of point 1
	 * @param x2 x coordinate of point 2
	 * @param y2 y coordinate of point 2
	 *
	 * @return true if the two points have line of sight, false if there is a wall in the way
	 */
	public boolean lineOfSightBetween(
		float x1, float y1,
		float x2, float y2
	) {
		return !this.lineIntersectsWalls(Line.of(x1, y1, x2, y2));
	}

	/**
	 * Calculate if a line intersects any walls
	 *
	 * @param line line to test
	 *
	 * @return true if it intersects any wall, false otherwise
	 */
	private boolean lineIntersectsWalls(Line line) {
		for (Collidable wall : this.walls) {
			if (wall.intersects(line)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove any obstacles touching a given object
	 *
	 * @param object object to test for collisions with
	 */
	public void removeObstaclesTouching(Collidable object) {
		this.obstacles.removeIf(obstacle -> obstacle.intersects(object));
	}
}
