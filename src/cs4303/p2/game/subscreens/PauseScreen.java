package cs4303.p2.game.subscreens;

import cs4303.p2.game.GameScreen;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.menu.OptionsScreen;
import cs4303.p2.util.builder.Button;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Screen shown to the user when they pause the game
 */
public final class PauseScreen extends AbstractGameMenuScreen {

	/**
	 * Continue playing button
	 */
	private final Button continueButton;
	/**
	 * Restart game button
	 */
	private final Button restartButton;
	/**
	 * Button for options screen
	 */
	private final Button optionsButton;
	/**
	 * Exit to main menu button
	 */
	private final Button exitMenuButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a pause screen
	 *
	 * @param game game instance
	 */
	public PauseScreen(GameScreen game) {
		super(game);

		this.continueButton = new Button(game.main, "Continue", game.rect()).asHUD();
		this.restartButton = new Button(game.main, "Restart game", game.rect()).asHUD();
		this.optionsButton = new Button(game.main, "Options", game.rect()).asHUD();
		this.exitMenuButton = new Button(game.main, "Exit to Menu", game.rect()).asHUD();
		this.exitDesktopButton = new Button(game.main, "Exit to Desktop", game.rect()).asHUD();
	}

	@Override
	public void draw() {
		this.drawMenu(
			"Paused",
			this.continueButton,
			this.restartButton,
			this.optionsButton,
			this.exitMenuButton,
			this.exitDesktopButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.continueButton.clicked(event)) {
			this.game.main.setScreen(this.game);
		} else if (this.restartButton.clicked(event)) {
			this.game.main.setScreen(new GameScreen(this.game.main));
		} else if (this.optionsButton.clicked(event)) {
			this.game.main.setScreen(new OptionsScreen(this.game.main, this));
		} else if (this.exitMenuButton.clicked(event)) {
			this.game.main.setScreen(new MenuScreen(this.game.main));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.game.main.exit();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == PConstants.ESC) {
			//Overwrite key to stop process ending
			this.game.main.key = 0;
			this.game.main.keyCode = 0;
			//Show the pause screen
			this.game.main.setScreen(this.game);
		}
	}
}
