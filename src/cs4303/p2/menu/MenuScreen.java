package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.screen.Screen;
import processing.event.KeyEvent;

import java.awt.Color;

public class MenuScreen implements Screen {

	private final Main main;

	public MenuScreen(Main main) {
		this.main = main;
	}

	@Override
	public Main main() {
		return this.main;
	}

	@Override
	public void draw() {
		main.background(0);
		main.text("Robotron 4303")
			.fill(Color.RED)
			.centredHorizontally(0, main.height / 4f, main.width)
			.size(150)
			.draw();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == ' ') {
			this.main.setScreen(new GameScreen(this.main));
		}
	}
}
