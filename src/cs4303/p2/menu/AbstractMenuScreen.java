package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.util.builder.Button;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.screen.Screen;

import java.awt.Color;

/**
 * Abstract menu screen. A menu screen has title text and a number of buttons under it
 */
public abstract class AbstractMenuScreen implements Screen {

	/**
	 * Main instance
	 */
	protected final Main main;

	/**
	 * Create a menu screen
	 *
	 * @param main main instance
	 */
	protected AbstractMenuScreen(Main main) {
		this.main = main;
	}

	/**
	 * Draw the background of the menu
	 */
	protected abstract void drawBackground();

	/**
	 * Draw the menu
	 *
	 * @param text    title text for menu
	 * @param buttons buttons to show on the screen
	 */
	protected final void drawMenu(String text, Button... buttons) {
		this.drawBackground();
		float thirdWidth = this.main.width / 3f;

		RectBuilder rect = this.main.rect()
			.at(thirdWidth, this.titleY())
			.size(thirdWidth, this.main.BUTTON_HEIGHT);

		this.main.text(text)
			.fill(this.titleTextColour())
			.size(this.titleTextSize())
			.centredInRect(rect)
			.asHUD()
			.draw();

		rect.translate(0, this.titleButtonSpacing());

		for (Button button : buttons) {
			button.copy(rect)
				.draw();
			rect.translate(0, this.buttonButtonSpacing());
		}
	}

	/**
	 * Y coordinate of the title text
	 *
	 * @return y coordinate of the title text
	 */
	protected float titleY() {
		return this.main.height / 3f;
	}

	/**
	 * Text size of title text
	 *
	 * @return text size of title text
	 */
	protected float titleTextSize() {
		return this.main.MENU_TITLE_TEXT_SIZE;
	}

	/**
	 * Colour of title text
	 *
	 * @return colour of title text
	 */
	protected Color titleTextColour() {
		return this.main.MENU_TITLE_TEXT_COLOR;
	}

	/**
	 * Spacing between the buttons
	 *
	 * @return spacing between the buttons in pixels
	 */
	protected float buttonButtonSpacing() {
		return this.main.BUTTON_SPACING;
	}

	/**
	 * Spacing between the title and the buttons in pixels
	 *
	 * @return spacing between the title and the buttons in pixels
	 */
	protected float titleButtonSpacing() {
		return this.main.BUTTON_SPACING * 2;
	}

}
