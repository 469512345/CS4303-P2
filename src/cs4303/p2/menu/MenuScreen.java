package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.screen.Screen;
import processing.event.KeyEvent;

public class MenuScreen implements Screen {

	private final Main main;

	public MenuScreen(Main main) {
		this.main = main;
	}

	@Override
	public void draw() {
		main.background(0);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == 'a') {
			this.main.setScreen(new GameScreen(this.main));
		}
	}
}
