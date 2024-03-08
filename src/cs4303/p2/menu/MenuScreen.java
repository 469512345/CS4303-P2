package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.builder.Button;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.screen.Screen;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.Color;

public class MenuScreen implements Screen {

	private final Main main;
	private final Button playButton;

	public MenuScreen(Main main) {
		this.main = main;
		this.playButton = new Button(main, "Play", main.rect());
	}

	@Override
	public Main main() {
		return this.main;
	}

	@Override
	public void draw() {
		main.background(0);

		RectBuilder rect = main.rect();

		float thirdWidth = main.width / 3f;

		rect.at(thirdWidth, main.height / 3f)
			.size(thirdWidth, main.BUTTON_HEIGHT);

		this.playButton.copy(rect).draw();
		rect.translate(0, -200);

		main.text("Robotron 4303")
			.fill(Color.RED)
			.centredInRect(rect)
			.size(150)
			.draw();
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if(playButton.clicked(event)) {
			this.main.setScreen(new GameScreen(this.main));
		}
	}
}
