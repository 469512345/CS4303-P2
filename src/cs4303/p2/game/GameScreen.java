package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.screen.Screen;
import processing.event.KeyEvent;

import java.awt.Color;

public class GameScreen implements Screen {

	private final Main main;
	private final Room room;

	public GameScreen(Main main) {
		System.out.println("New game screen");
		this.main = main;
		this.room = new Room(main);
	}

	@Override
	public void draw() {
		main.background(Color.BLACK.getRGB());
		this.room.draw();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKey() == 'a') {
			this.main.setScreen(new MenuScreen(this.main));
		}
	}
}
