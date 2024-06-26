package cs4303.p2.game.level;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.Projectile;
import cs4303.p2.game.entity.family.Family;
import cs4303.p2.game.entity.robot.Robot;
import cs4303.p2.game.level.corridor.Corridor;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.game.level.room.Room;
import cs4303.p2.game.powerup.Powerup;
import cs4303.p2.game.powerup.PowerupType;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Line;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

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
	@NotNull
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
	 * Nodes in the graph-based representation of the world
	 */
	public final List<Node> nodes = new LinkedList<>();
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
	public final List<Family> family = new LinkedList<>();
	/**
	 * Robots in the world
	 */
	public final List<Robot> robots = new LinkedList<>();

	/**
	 * Create a new level
	 *
	 * @param game      game instance
	 * @param levelInfo level generation parameters
	 */
	public Level(GameScreen game, @NotNull LevelInfo levelInfo) {
		this.game = game;
		this.levelInfo = levelInfo;
		this.root = Room.createRoot(this.game, this.levelInfo);

		this.root.appendWalls(this.horizontalWalls, this.verticalWalls);
		this.walls.addAll(this.horizontalWalls);
		this.walls.addAll(this.verticalWalls);

		Random random = this.game.main.random;

		this.root.appendRooms(this.rooms);
		LinkedList<LeafRoom> singlyConnectedRooms = new LinkedList<>(this.rooms);
		singlyConnectedRooms.removeIf(room -> room.corridors.size() >= 2); //The starting room must only have 1 corridor
		this.startingRoom = singlyConnectedRooms.get(random.nextInt(singlyConnectedRooms.size()));

		if (this.rooms.size() == 1) {
			//Inject a dummy obstacle to act as the player, and ensure nothing spawns touching the player
			Obstacle dummyPlayer = new Obstacle(game, this.startingRoom.centre(), this.game.main.PLAYER_RADIUS);
			this.obstacles.add(dummyPlayer);
			this.addObstaclesToRegion(
				this.startingRoom,
				this.levelInfo.minObstaclesPerRoom(),
				this.levelInfo.maxObstaclesPerCorridor()
			);
			this.addHumansToRegion(this.startingRoom);
			this.addRobotsToRegion(this.startingRoom);
			this.obstacles.remove(dummyPlayer);
		} else {
			//Populate content in rooms

			//Ensure that the first room always gets robots - this guarantees all levels will have robots somewhere
			boolean firstRoom = true;
			for (LeafRoom room : this.rooms) {
				//Don't put anything in the starting room if there is more than one room
				if (room == this.startingRoom) {
					continue;
				}
				this.addObstaclesToRegion(
					room,
					this.levelInfo.minObstaclesPerRoom(),
					this.levelInfo.maxObstaclesPerRoom()
				);
				//Determine if the room should have robots or humans in it
				if (firstRoom || random.nextFloat(0, 1) < this.levelInfo.roomRobotChance()) {
					firstRoom = false;
					this.addRobotsToRegion(room);
				} else {
					this.addHumansToRegion(room);
				}
			}
		}

		//Add powerups to world
		for (int i = 0; i < this.levelInfo.powerupsInWorld(); i++) {
			//Cycle through powerup types for even distribution
			PowerupType type = PowerupType.values()[i % PowerupType.values().length];

			int roomIndex = random.nextInt(0, this.rooms.size());
			LeafRoom room = this.rooms.get(roomIndex);
			this.addPowerupToRegion(room, type);
		}

		//Populate content in corridors
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

		this.root.appendNodes(this.nodes);

	}

	/**
	 * Add some obstacles to a region of the map
	 *
	 * @param room region to populate
	 * @param min  minimum number of obstacles
	 * @param max  maximum number of obstacles
	 */
	private void addObstaclesToRegion(@NotNull Rectangle room, int min, int max) {
		Random random = this.game.main.random;
		//Get the smallest dimension of the room
		float minimumDimension = Math.min(room.height(), room.width());
		//The max radius for the obstacle should not exceed half the minimum dimension of the room
		float maxRadius = Math.min(this.levelInfo.maxObstacleRadius(), minimumDimension / 2);
		//Min radius cannot exceed max radius
		float minRadius = Math.min(this.levelInfo.minObstacleRadius(), maxRadius);

		this.populateRegion(this.obstacles, min, max,
			(game, position) -> {
				float radius = random.nextFloat(minRadius, maxRadius);
				return new Obstacle(this.game, position, radius);
			}, (obstacle, position) -> {
				float radius = obstacle.radius();
				//Position the centre of the obstacle at least one radius from the edge of the room
				position.x = random.nextFloat(room.minX() + radius, room.maxX() - radius);
				position.y = random.nextFloat(room.minY() + radius, room.maxY() - radius);
			}
		);
	}

	/**
	 * Populate a room with robots based on the level generation parameters
	 *
	 * @param room room to populate
	 */
	private void addRobotsToRegion(@NotNull Rectangle room) {
		Random random = this.game.main.random;

		LevelInfo.RobotConstructor[] constructors = this.levelInfo.robotConstructors();
		LevelInfo.RobotConstructor constructor = constructors[random.nextInt(0, constructors.length)];

		this.populateRegion(this.robots, this.levelInfo.minRobotsPerRoom(), this.levelInfo.maxRobotsPerRoom(),
			constructor,
			(robot, position) -> {
				float radius = robot.radius();
				//Position the centre of the obstacle at least one radius from the edge of the room
				position.x = random.nextFloat(room.minX() + radius, room.maxX() - radius);
				position.y = random.nextFloat(room.minY() + radius, room.maxY() - radius);
			}
		);
	}

	/**
	 * Populate a room with humans based on the level generation parameters
	 *
	 * @param room room to populate
	 */
	private void addHumansToRegion(@NotNull Rectangle room) {
		Random random = this.game.main.random;

		LevelInfo.HumanConstructor[] constructors = this.levelInfo.humanConstructors();
		LevelInfo.HumanConstructor constructor = constructors[random.nextInt(0, constructors.length)];

		this.populateRegion(this.family, this.levelInfo.minHumansPerRoom(), this.levelInfo.maxHumansPerRoom(),
			constructor,
			(human, position) -> {
				float radius = human.radius();
				//Position the centre of the obstacle at least one radius from the edge of the room
				position.x = random.nextFloat(room.minX() + radius, room.maxX() - radius);
				position.y = random.nextFloat(room.minY() + radius, room.maxY() - radius);
			}
		);
	}

	/**
	 * Adds a powerup to a room
	 *
	 * @param room room to add powerup in
	 * @param type type of powerup to add
	 */
	private void addPowerupToRegion(@NotNull Rectangle room, PowerupType type) {
		Random random = this.game.main.random;

		this.populateRegion(this.powerups, 1, 1,
			(game, position) -> new Powerup(game, position, type),
			(powerup, position) -> {
				position.x = random.nextFloat(room.minX(), room.maxX());
				position.y = random.nextFloat(room.minY(), room.maxY());
			}
		);
	}

	/**
	 * Populate a region with a random number of items
	 *
	 * @param collection         collection of items to append to
	 * @param min                minimum number to create
	 * @param max                maximum number to create
	 * @param constructor        constructor for items
	 * @param positionRandomizer function to randomise the position within the region
	 * @param <T>                type of item being populated
	 */
	private <T extends Collidable> void populateRegion(
		@NotNull Collection<T> collection,
		int min,
		int max,
		@NotNull LevelInfo.ObjectConstructor<T> constructor,
		@NotNull BiConsumer<T, PVector> positionRandomizer
	) {
		Random random = this.game.main.random;
		int objectsToAdd = random.nextInt(min, max + 1);
		//Failed attempts to place an obstacle
		int failedSpawns = 0;
		while (objectsToAdd > 0) {
			//Abort if exceeded maximum number of failed attempts
			if (failedSpawns > this.game.main.OBSTACLE_MAX_FAILED_ATTEMPTS) {
				break;
			}
			PVector position = new PVector();
			T object = constructor.construct(this.game, position);

			int failedPlacements = 0;
			boolean collides;
			do {
				positionRandomizer.accept(object, position);
				collides = this.collidesWithAnything(object);
				if (collides) {
					if (failedPlacements++ >= this.game.main.OBSTACLE_MAX_FAILED_ATTEMPTS) {
						failedSpawns++;
						break;
					}
				}
			} while (collides);

			if (!collides) {
				collection.add(object);
				objectsToAdd--;
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
			if (this.game.player.hasLineOfSight(projectile.centre())) {
				projectile.draw();
			}
		}
	}

	/**
	 * Draw powerups on screen
	 */
	private void drawPowerups() {
		for (Powerup powerup : this.powerups) {
			if (this.game.player.hasLineOfSight(powerup.centre())) {
				powerup.draw();
			}
		}
	}

	/**
	 * Draw the obstacles on the map
	 */
	private void drawObstacles() {
		for (Obstacle obstacle : this.obstacles) {
			if (this.game.player.hasLineOfSight(obstacle.centre())) {
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
			if (!projectile.expired() && projectile.canHitPlayer() && projectile.intersects(this.game.player)) {
				projectile.expire();
				this.game.die();
				//Clear the list of projectiles, so they are not there when the player respawns
				this.projectiles.clear();
				break;
			}
			if (!projectile.expired()) {
				Obstacle obstacle = this.collidesWithObstacle(projectile);
				if (obstacle != null) {
					projectile.expire();
					obstacle.explode();
				}
			}
			if (!projectile.expired()) {
				Robot robot = this.collidesWithRobot(projectile);
				if (robot != null) {
					projectile.expire();
					robot.kill();
				}
			}
			if (!projectile.expired() && this.game.main.FRIENDLY_FIRE) {
				Family family = this.collidesWithFamily(projectile);
				if (family != null) {
					projectile.expire();
					family.kill();
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
			}
			Family collidedHuman = this.collidesWithFamily(obstacle);
			Robot collidedRobot = this.collidesWithRobot(obstacle);
			if (collidedHuman != null) {
				collidedHuman.kill();
			}
			if (collidedRobot != null) {
				collidedRobot.remove();
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
		Iterator<Powerup> iterator = this.powerups.iterator();
		while (iterator.hasNext()) {
			Powerup powerup = iterator.next();

			if (powerup.intersects(this.game.player)) {
				this.game.player.applyPowerup(powerup);
			}

			if (powerup.collected()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Update the family in the world
	 */
	private void updateFamily() {
		Iterator<Family> iterator = this.family.iterator();
		while (iterator.hasNext()) {
			Family familyMember = iterator.next();
			familyMember.update();
			if (familyMember.isActive() && familyMember.intersects(this.game.player)) {
				familyMember.rescue();
			}
			if (!familyMember.isActive()) {
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
			if (robot.isActive() && robot.intersects(this.game.player)) {
				this.game.die();
			}
			Family familyMember = this.game.level.collidesWithFamily(robot);
			if (robot.isActive() && familyMember != null) {
				robot.killFamilyMember(familyMember);
			}
			if (!robot.isActive()) {
				iterator.remove();
				this.game.checkRoundOver();
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
	public void moveNoBounce(@NotNull PVector position, @NotNull PVector velocity, float radiusSquared) {
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
	public int moveBounce(@NotNull PVector position, @NotNull PVector velocity, float radius) {
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
		@NotNull PVector position,
		@NotNull PVector velocity,
		float radius,
		int count,
		float initialMagnitude,
		@NotNull Set<Collidable> surfaces
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
	public boolean lineOfSightBetween(@NotNull PVector point1, @NotNull PVector point2) {
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
	public boolean lineIntersectsWalls(Line line) {
		return this.collidesWithWall(line) != null;
	}

	/**
	 * Check if an object collides with anything already in the world
	 *
	 * @param subject object to test
	 *
	 * @return true if the object collides with anything in the world, false otherwise
	 */
	private boolean collidesWithAnything(Collidable subject) {
		return this.collidesWithObstacle(subject) != null ||
			this.collidesWithFamily(subject) != null ||
			this.collidesWithRobot(subject) != null ||
			this.collidesWithProjectile(subject) != null ||
			this.collidesWithPowerup(subject) != null;
	}

	/**
	 * Check if an object collides with anything in a collection
	 *
	 * @param subject subject to test against collection
	 * @param objects collection of collidable objects
	 * @param <T>     type of object in collection
	 *
	 * @return object which collided, or null if none
	 */
	@Nullable
	private <T extends Collidable> T collidesWithAnythingIn(Collidable subject, @NotNull Collection<T> objects) {
		for (T object : objects) {
			if (object != subject && object.intersects(subject)) {
				return object;
			}
		}
		return null;
	}

	/**
	 * Check if an object collides with any wall, returning the wall with the collision. Note this will only consider
	 * the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first wall with collision, or null if none collided
	 */
	@Nullable
	public Collidable collidesWithWall(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.walls);
	}

	/**
	 * Check if an object collides with any obstacle, returning the obstacle with the collision. Note this will only
	 * consider the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first obstacle with collision, or null if none collided
	 */
	@Nullable
	public Obstacle collidesWithObstacle(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.obstacles);
	}

	/**
	 * Check if an object collides with any family member, returning the family member with the collision. Note this
	 * will only consider the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first family member with collision, or null if none collided
	 */
	@Nullable
	private Family collidesWithFamily(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.family);
	}

	/**
	 * Check if an object collides with any robot, returning the robot with the collision. Note this will only consider
	 * the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first robot with collision, or null if none collided
	 */
	@Nullable
	private Robot collidesWithRobot(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.robots);
	}

	/**
	 * Check if an object collides with any projectile, returning the projectile with the collision. Note this will only
	 * consider the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first projectile with collision, or null if none collided
	 */
	@Nullable
	private Projectile collidesWithProjectile(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.projectiles);
	}

	/**
	 * Check if an object collides with any powerup, returning the powerup with the collision. Note this will only
	 * consider the first collision
	 *
	 * @param subject object to test
	 *
	 * @return first powerup with collision, or null if none collided
	 */
	@Nullable
	private Powerup collidesWithPowerup(Collidable subject) {
		return this.collidesWithAnythingIn(subject, this.powerups);
	}

	/**
	 * Calculate the closest node to a given point
	 *
	 * @param x                  x coordinate of point
	 * @param y                  y coordinate of point
	 * @param requireLineOfSight whether a direct line of sight between the point and the node is required
	 *
	 * @return node if it was found, or null if no nodes were found (for example if none had direct line of sight).
	 */
	@Nullable
	public Node closestNodeTo(float x, float y, boolean requireLineOfSight) {
		float smallestDistanceSq = Float.MAX_VALUE;
		Node min = null;

		for (Node node : this.nodes) {
			if (!requireLineOfSight || this.lineOfSightBetween(x, y, node.x(), node.y())) {
				float distanceSq = node.distanceSqTo(x, y);
				if (distanceSq < smallestDistanceSq) {
					smallestDistanceSq = distanceSq;
					min = node;
				}
			}
		}

		return min;
	}

}
