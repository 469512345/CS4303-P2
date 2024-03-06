package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.util.screen.Screen;

public class MenuScreen implements Screen {

	private final Main main;

	public MenuScreen(Main main) {
		this.main = main;
	}

	@Override
	public void draw() {
		main.text("Hello World", 0, 0);
	}
}
