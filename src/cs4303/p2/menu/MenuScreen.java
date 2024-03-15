package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.builder.Button;
import processing.event.MouseEvent;

import processing.event.KeyEvent;

import java.awt.*;

/**
 * Menu screen, when the game opens
 */
public class MenuScreen extends AbstractMenuScreen {

	/**
	 * Play button on the screen
	 */
	@NotNull
	private final Button playButton;
	/**
	 * Options button
	 */
	@NotNull
	private final Button optionsButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a Menu screen
	 *
	 * @param main main instance
	 */
	public MenuScreen(@NotNull Main main) {
		super(main);

		this.playButton = new Button(main, "Play", main.rect());
		this.optionsButton = new Button(main, "Options", main.rect());
		this.exitDesktopButton = new Button(this.main, "Exit to Desktop", this.main.rect()).asHUD();
	}

	@Override
	protected void drawBackground() {
		this.main.background(this.main.MENU_BACKGROUND_COLOR.getRGB());
	}

	@Override
	public void draw() {
		this.drawMenu(
			"Robotron 4303",
			this.playButton,
			this.optionsButton,
			this.exitDesktopButton
		);
		this.text("Press any key to begin...")
				.fill(Color.WHITE)
				.size(50)
				.centredHorizontally(0, this.main.height * 0.75f, this.main.width)
				.draw();
	}

	@Override
	public void mouseClicked(@NotNull MouseEvent event) {
		if (this.playButton.clicked(event)) {
			this.main.setScreen(new GameScreen(this.main));
		} else if (this.optionsButton.clicked(event)) {
			this.main.setScreen(new OptionsScreen(this.main, this));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.main.exit();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		GameScreen gameScreen = new GameScreen(this.main);
		this.main.setScreen(gameScreen);
	}
}
