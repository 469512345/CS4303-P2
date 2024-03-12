package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.room.AbstractRoom;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.builder.TextBuilder;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.VerticalLine;
import cs4303.p2.util.screen.Screen;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Screen shown to the user when playing the game
 */
public class GameScreen implements Screen {

	/**
	 * Main instance
	 */
	protected final Main main;
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

	private int score = 0;
	private int lives;
	private int wave = 0;

	/**
	 * Create the game instance
	 *
	 * @param main main instance
	 */
	public GameScreen(Main main) {
		this.main = main;
		this.level = AbstractRoom.createRoot(this.main, this.generateLevelInfo(1));
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
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == ' ') {
			this.main.setScreen(new MenuScreen(this.main));
			return;
		}
		if (this.main.KEYBIND_ZOOM_IN.test(event)) {
			this.zoomIn();
		} else if (this.main.KEYBIND_ZOOM_OUT.test(event)) {
			this.zoomOut();
		} else if (this.main.KEYBIND_FIRE.test(event)) {
			this.fire();
		} else {
			this.handleKeyUpDownEvent(event, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.handleKeyUpDownEvent(event, false);
	}

	/**
	 * Handle a key up or key down event, and update the player's movement accordingly
	 *
	 * @param event key event
	 * @param value true if the key is being pressed, false otherwise
	 */
	private void handleKeyUpDownEvent(KeyEvent event, boolean value) {
		if (this.main.KEYBIND_MOVE_UP.test(event)) {
			this.player.up = value;
		} else if (this.main.KEYBIND_MOVE_DOWN.test(event)) {
			this.player.down = value;
		} else if (this.main.KEYBIND_MOVE_LEFT.test(event)) {
			this.player.left = value;
		} else if (this.main.KEYBIND_MOVE_RIGHT.test(event)) {
			this.player.right = value;
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
	 * Generate level parameters for a level by number
	 *
	 * @param levelNumber level number
	 *
	 * @return level parameters
	 */
	public LevelInfo generateLevelInfo(int levelNumber) {
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
}
