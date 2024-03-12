package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.room.AbstractRoom;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.VerticalLine;
import cs4303.p2.util.keybind.KeyKeybind;
import cs4303.p2.util.keybind.Keybind;
import cs4303.p2.util.keybind.MouseKeybind;
import cs4303.p2.util.screen.Screen;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.Color;
import java.awt.event.MouseEvent;
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
	public final List<Collidable> walls = new LinkedList<>();
	/**
	 * Rooms
	 */
	private final List<LeafRoom> rooms = new LinkedList<>();
	/**
	 * Player instance
	 */
	private final Player player;
	/**
	 * Keybind for move up
	 */
	private Keybind up = new KeyKeybind(87, 0); // w
	/**
	 * Keybind for move down
	 */
	private Keybind down = new KeyKeybind(83, 0); // s
	/**
	 * Keybind for move left
	 */
	private Keybind left = new KeyKeybind(65, 0); // a
	/**
	 * Keybind for move right
	 */
	private Keybind right = new KeyKeybind(68, 0); // d
	/**
	 * Keybind for fire
	 */
	private Keybind fire = new MouseKeybind(MouseEvent.BUTTON1, 0); // left click
	/**
	 * Keybind for zoom in
	 */
	private Keybind zoomIn = new KeyKeybind(61, KeyEvent.CTRL); // CTRL + +
	/**
	 * Keybind for zoom out
	 */
	private Keybind zoomOut = new KeyKeybind(45, KeyEvent.CTRL); // CTRL + -
	/**
	 * The current zoom level
	 */
	private float scale = 2;

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
		singlyConnectedRooms.removeIf(room -> room.corridors.size() != 1);
		LeafRoom startingRoom = singlyConnectedRooms.get(main.random.nextInt(singlyConnectedRooms.size()));

		this.player = new Player(this, new PVector(startingRoom.centreX(), startingRoom.centreY()));
	}

	@Override
	public void draw() {
		this.update();
		this.main.background(Color.BLACK.getRGB());
		this.level.draw();
		this.player.draw();
		LineBuilder line = this.main.line()
			.stroke(Color.GREEN)
			.strokeWeight(1);
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
		if (this.zoomIn.test(event)) {
			this.zoomIn();
		} else if (this.zoomOut.test(event)) {
			this.zoomOut();
		} else if (this.fire.test(event)) {
			this.fire();
		} else {
			this.handleKeyUpDownEvent(event, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.handleKeyUpDownEvent(event, false);
	}

	private void handleKeyUpDownEvent(KeyEvent event, boolean value) {
		if (this.up.test(event)) {
			this.player.up = value;
		} else if (this.down.test(event)) {
			this.player.down = value;
		} else if (this.left.test(event)) {
			this.player.left = value;
		} else if (this.right.test(event)) {
			this.player.right = value;
		}
	}

	private void zoomIn() {
		this.scale *= 1.1f;
	}

	private void zoomOut() {
		this.scale /= 1.1f;
	}

	private void fire() {

	}

	@Override
	public float offsetX() {
		return ((this.main.width / 2f) / this.scale()) - this.player.laggedPosition().x;
	}

	@Override
	public float offsetY() {
		return ((this.main.height / 2f) / this.scale()) - this.player.laggedPosition().y;
	}

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
