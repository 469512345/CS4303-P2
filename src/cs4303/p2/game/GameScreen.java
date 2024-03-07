package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.level.AbstractRoom;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.screen.Screen;
import processing.event.KeyEvent;

import java.awt.Color;

public class GameScreen implements Screen {

	private final Main main;
	private final AbstractRoom containerRoom;

	public GameScreen(Main main) {
		this.main = main;
		this.containerRoom = AbstractRoom.createRoot(this.main, generateLevelInfo(1));
	}

	@Override
	public void draw() {
		main.background(Color.BLACK.getRGB());
		this.containerRoom.draw();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == 'a') {
			this.main.setScreen(new MenuScreen(this.main));
		}
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
