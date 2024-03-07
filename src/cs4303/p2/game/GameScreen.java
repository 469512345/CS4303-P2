package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.AbstractRoom;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.util.keybind.KeyKeybind;
import cs4303.p2.util.keybind.Keybind;
import cs4303.p2.util.screen.Screen;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.Color;

public class GameScreen implements Screen {

	private final Main main;
	private final AbstractRoom containerRoom;
	private final Player player;
	private Keybind up = new KeyKeybind(87, 0); // w
	private Keybind down = new KeyKeybind(83, 0); // s
	private Keybind left = new KeyKeybind(65, 0); // a
	private Keybind right = new KeyKeybind(68, 0); // d

	public GameScreen(Main main) {
		this.main = main;
		this.containerRoom = AbstractRoom.createRoot(this.main, generateLevelInfo(1));
		this.player = new Player(this, new PVector(main.width / 2f, main.height / 2f));
	}

	@Override
	public Main main() {
		return this.main;
	}

	@Override
	public void draw() {
		main.background(Color.BLACK.getRGB());
		this.containerRoom.draw();
		this.player.draw();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (this.up.test(event)) {
			this.player.up();
		} else if (this.down.test(event)) {
			this.player.down();
		} else if (this.left.test(event)) {
			this.player.left();
		} else if (this.right.test(event)) {
			this.player.right();
		}
	}

	@Override
	public float offsetX() {
		return ((this.main.width / 2f) / this.scale()) - this.player.position.x;
	}

	@Override
	public float offsetY() {
		return ((this.main.height / 2f) / this.scale()) - this.player.position.y;
	}

	@Override
	public float scale() {
		return 2;
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
			11,
			80,
			0.8f,
			20
		);
	}
}
