package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.game.GameScreen;
import cs4303.p2.util.builder.Button;
import processing.event.MouseEvent;

/**
 * Menu screen, when the game opens
 */
public class MenuScreen extends AbstractMenuScreen {

	/**
	 * Play button on the screen
	 */
	private final Button playButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a Menu screen
	 *
	 * @param main main instance
	 */
	public MenuScreen(Main main) {
		super(main);
		this.playButton = new Button(main, "Play", main.rect());
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
			this.exitDesktopButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.playButton.clicked(event)) {
			this.main.setScreen(new GameScreen(this.main));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.main.exit();
		}
	}
}
