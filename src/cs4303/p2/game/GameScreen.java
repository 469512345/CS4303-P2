package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.AbstractRoom;
import cs4303.p2.game.level.LeafRoom;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.Wall;
import cs4303.p2.menu.MenuScreen;
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
	 * Walls
	 */
	public final List<Wall> walls = new LinkedList<>();
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
		this.level = AbstractRoom.createRoot(this.main, generateLevelInfo(1));
		this.level.appendWalls(this.walls);

		this.level.appendRooms(this.rooms);
		LinkedList<LeafRoom> singlyConnectedRooms = new LinkedList<>(this.rooms);
		singlyConnectedRooms.removeIf(room -> room.corridors.size() != 1);
		System.out.println(singlyConnectedRooms);
		LeafRoom startingRoom = singlyConnectedRooms.get(main.random.nextInt(singlyConnectedRooms.size()));
		System.out.println(startingRoom);

		this.player = new Player(this, new PVector(startingRoom.centreX(), startingRoom.centreY()));
	}

	@Override
	public void draw() {
		this.update();
		main.background(Color.BLACK.getRGB());
		this.level.draw();
		this.player.draw();
		for (Wall wall : this.walls) {
			this.main.line()
				.from(wall.p1X(), wall.p1Y())
				.to(wall.p2X(), wall.p2Y())
				.stroke(Color.GREEN)
				.strokeWeight(1)
				.draw();
		}
	}

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
			handleKeyUpDownEvent(event, true);
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

	public LevelInfo generateLevelInfo(int levelNumber) {
		float width = main.width;
		float height = main.height;
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
