package cs4303.p2.game.subscreens;

import cs4303.p2.game.GameScreen;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.Button;
import processing.event.MouseEvent;

/**
 * Screen shown to the player when they die
 */
public final class GameOverScreen extends AbstractGameMenuScreen {

	/**
	 * Play again button
	 */
	private final Button playAgainButton;
	/**
	 * Exit to menu button
	 */
	private final Button exitMenuButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a game over screen
	 *
	 * @param game game instance
	 */
	public GameOverScreen(GameScreen game) {
		super(game);
		this.playAgainButton = new Button(game.main, "Play Again", game.rect()).asHUD();
		this.exitMenuButton = new Button(game.main, "Exit to Menu", game.rect()).asHUD();
		this.exitDesktopButton = new Button(game.main, "Exit to Desktop", game.rect()).asHUD();
	}

	@Override
	public void draw() {
		this.drawMenu(
			"Game Over",
			this.playAgainButton,
			this.exitMenuButton,
			this.exitDesktopButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.playAgainButton.clicked(event)) {
			this.game.main.setScreen(new GameScreen(this.game.main));
		} else if (this.exitMenuButton.clicked(event)) {
			this.game.main.setScreen(new MenuScreen(this.game.main));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.game.main.exit();
		}
	}
}
