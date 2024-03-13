package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.room.AbstractRoom;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.builder.TextBuilder;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Line;
import cs4303.p2.util.collisions.VerticalLine;
import cs4303.p2.util.screen.Screen;
import processing.core.PVector;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Screen shown to the user when playing the game
 */
public class GameScreen implements Screen {

	/**
	 * Main instance
	 */
	public final Main main;
	/**
	 * Root of the room tree
	 */
	private final AbstractRoom level;
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
	 * Projectiles currently active in the world
	 */
	private final List<Projectile> projectiles = new LinkedList<>();
	/**
	 * The room that the player starts in
	 */
	private final LeafRoom startingRoom;
	/**
	 * Player instance
	 */
	private final Player player;
	/**
	 * The current zoom level
	 */
	private float scale;

	/**
	 * Current score
	 */
	private int score = 0;
	/**
	 * Current lives
	 */
	private int lives;
	/**
	 * Current wave number
	 */
	private int wave = 0;

	/**
	 * Create the game instance
	 *
	 * @param main main instance
	 */
	public GameScreen(Main main) {
		this.main = main;
		this.level = AbstractRoom.createRoot(this, this.generateLevelInfo());
		this.level.appendWalls(this.horizontalWalls, this.verticalWalls);
		this.walls.addAll(this.horizontalWalls);
		this.walls.addAll(this.verticalWalls);

		this.level.appendRooms(this.rooms);
		LinkedList<LeafRoom> singlyConnectedRooms = new LinkedList<>(this.rooms);
		singlyConnectedRooms.removeIf(room -> room.corridors.size() != 1); //The starting room must only have 1 corridor
		this.startingRoom = singlyConnectedRooms.get(main.random.nextInt(singlyConnectedRooms.size()));

		this.player = new Player(this, new PVector(this.startingRoom.centreX(), this.startingRoom.centreY()));
		this.lives = this.main.STARTING_LIVES;
		this.scale = this.main.INITIAL_ZOOM;
	}

	@Override
	public void draw() {
		this.update();
		this.main.background(this.main.GAME_BACKGROUND_COLOR.getRGB());
		this.level.draw();
		this.drawWalls();
		this.drawProjectiles();
		this.player.draw();
		this.drawHUD();
	}

	/**
	 * Draw the walls
	 */
	private void drawWalls() {
		LineBuilder line = this.main.line()
			.stroke(this.main.WALL_COLOR)
			.strokeWeight(this.main.WALL_STROKE_WIDTH);
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
			projectile.draw();
		}
	}

	/**
	 * Draw the HUD
	 */
	private void drawHUD() {
		TextBuilder text = this.main.text("Score: " + this.score)
			.fill(this.main.HUD_TEXT_COLOR)
			.asHUD();
		text.centredHorizontally(0, this.main.HUD_TEXT_HEIGHT, this.main.width)
			.size(this.main.HUD_SCORE_TEXT_SIZE)
			.draw();
		float halfWidth = this.main.width / 2f;
		text.text("Wave: " + this.wave)
			.centredHorizontally(0, this.main.HUD_TEXT_HEIGHT, halfWidth)
			.size(this.main.HUD_OTHER_TEXT_SIZE)
			.draw();
		text.text("Lives: " + this.lives)
			.centredHorizontally(halfWidth, this.main.HUD_TEXT_HEIGHT, halfWidth)
			.draw();
	}

	/**
	 * Update the physics of all objects
	 */
	public void update() {
		this.player.update();
		this.updateProjectiles();
	}

	/**
	 * Update any projectiles on screen
	 */
	private void updateProjectiles() {
		Iterator<Projectile> iterator = this.projectiles.iterator();
		while (iterator.hasNext()) {
			Projectile projectile = iterator.next();
			projectile.update();
			if (projectile.expired()) {
				iterator.remove();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == ' ') {
			this.main.setScreen(new MenuScreen(this.main));
			return;
		}
		this.handleKeyOrMouseDown(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.handleKeyOrMouseUp(event);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		this.handleKeyOrMouseDown(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		this.handleKeyOrMouseUp(event);
	}

	/**
	 * Handle a key down or mouse down event
	 *
	 * @param event key event or mouse event
	 */
	private void handleKeyOrMouseDown(Event event) {
		if (this.main.KEYBIND_ZOOM_IN.test(event)) {
			this.zoomIn();
		} else if (this.main.KEYBIND_ZOOM_OUT.test(event)) {
			this.zoomOut();
		} else if (this.main.KEYBIND_FIRE.test(event)) {
			this.fire();
		} else {
			this.handleUpOrDownEvent(event, true);
		}
	}

	/**
	 * Handle a key or mouse up event
	 *
	 * @param event key or mouse event
	 */
	private void handleKeyOrMouseUp(Event event) {
		this.handleUpOrDownEvent(event, false);
	}

	/**
	 * Handle a key up or key down event, and update the player's movement accordingly
	 *
	 * @param event event
	 * @param down  true if the key / mouse is being pressed, false otherwise
	 */
	private void handleUpOrDownEvent(Event event, boolean down) {
		if (this.main.KEYBIND_MOVE_UP.test(event)) {
			this.player.up = down;
		} else if (this.main.KEYBIND_MOVE_DOWN.test(event)) {
			this.player.down = down;
		} else if (this.main.KEYBIND_MOVE_LEFT.test(event)) {
			this.player.left = down;
		} else if (this.main.KEYBIND_MOVE_RIGHT.test(event)) {
			this.player.right = down;
		}
	}

	/**
	 * Zoom the camera in
	 */
	private void zoomIn() {
		this.scale *= this.main.ZOOM_FACTOR;
	}

	/**
	 * Zoom the camera out
	 */
	private void zoomOut() {
		this.scale /= this.main.ZOOM_FACTOR;
	}

	/**
	 * Fire a projectile from the player
	 */
	private void fire() {
		PVector position = this.player.copyPosition();
		PVector velocity = new PVector(this.main.mouseX, this.main.mouseY);
		this.unconvert(velocity); // Convert the screen coordinates back to world coordinates
		velocity.sub(position)
			.setMag(this.main.PLAYER_PROJECTILE_MOVEMENT_VELOCITY);
		Projectile projectile = new Projectile(this, position, velocity);
		this.projectiles.add(projectile);
	}

	/**
	 * Provide an X offset to centre the player on the screen
	 *
	 * @return x offset to centre the player
	 */
	@Override
	public float offsetX() {
		return ((this.main.width / 2f) / this.scale()) - this.player.laggedPosition().x;
	}

	/**
	 * Provide a Y offset to centre the player on the screen
	 *
	 * @return y offset to centre the player
	 */
	@Override
	public float offsetY() {
		return ((this.main.height / 2f) / this.scale()) - this.player.laggedPosition().y;
	}

	/**
	 * Provide a zoom to the view
	 *
	 * @return scale factor for zoom on viewport
	 */
	@Override
	public float scale() {
		return this.scale;
	}

	/**
	 * Generate level parameters for the current wave
	 *
	 * @return level parameters
	 */
	public LevelInfo generateLevelInfo() {
		float width = this.main.width;
		float height = this.main.height;
		float minWidth = width / 10f;
		float minHeight = height / 6f;
		return new LevelInfo(
			width,
			height,
			minWidth,
			minHeight,
			3 * minWidth,
			3 * minHeight,
			16,
			80,
			0.8f,
			30
		);
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
		return this.moveBounce(position, velocity, radius, radius * radius, 0, velocity.mag(), new HashSet<>());
	}

	public int moveBounce(
		PVector position,
		PVector velocity,
		float radius,
		float radiusSquared,
		int count,
		float initialMagnitude,
		Set<Collidable> surfaces
	) {
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
			return this.moveBounce(position, velocity, radius, radiusSquared, count + 1, initialMagnitude, surfaces);
		}

		//If no bounces, then position is the new location, and restore the velocity to its initial
		// magnitude (if any bounces occurred then the magnitude would've been modified).
		position.set(newX, newY);
		velocity.setMag(initialMagnitude);
		//Return how many bounces occurred
		return count;
	}

}
