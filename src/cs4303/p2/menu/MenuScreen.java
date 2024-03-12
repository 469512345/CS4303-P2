package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.builder.Button;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.screen.Screen;
import processing.event.MouseEvent;

import java.awt.Color;

/**
 * Menu screen, when the game opens
 */
public class MenuScreen implements Screen {

	/**
	 * Main instance
	 */
	private final Main main;
	/**
	 * Play button on the screen
	 */
	private final Button playButton;

	/**
	 * Create a Menu screen
	 *
	 * @param main main instance
	 */
	public MenuScreen(Main main) {
		this.main = main;
		this.playButton = new Button(main, "Play", main.rect());
	}

	@Override
	public void draw() {
		this.main.background(this.main.MENU_BACKGROUND_COLOR.getRGB());

		RectBuilder rect = this.main.rect();

		float thirdWidth = this.main.width / 3f;

		rect.at(thirdWidth, this.main.height / 3f)
			.size(thirdWidth, this.main.BUTTON_HEIGHT);

		this.playButton.copy(rect)
			.draw();
		rect.translate(0, -200);

		this.main.text("Robotron 4303")
			.fill(this.main.MENU_TITLE_TEXT_COLOR)
			.size(this.main.MENU_TITLE_TEXT_SIZE)
			.centredInRect(rect)
			.draw();
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.playButton.clicked(event)) {
			this.main.setScreen(new GameScreen(this.main));
		}
	}
}
