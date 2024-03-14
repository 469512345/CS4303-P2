package cs4303.p2.game.subscreens;

import cs4303.p2.game.GameScreen;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.Button;
import processing.event.MouseEvent;

/**
 * Screen shown when the player dies
 */
public final class DiedScreen extends AbstractGameMenuScreen {

	/**
	 * Respawn button
	 */
	private final Button respawnButton;
	/**
	 * Restart button
	 */
	private final Button restartButton;
	/**
	 * Exit to menu button
	 */
	private final Button exitMenuButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a died screen
	 * @param game game instance
	 */
	public DiedScreen(GameScreen game) {
		super(game);
		this.respawnButton = new Button(game.main, "Respawn", game.rect()).asHUD();
		this.restartButton = new Button(game.main, "Restart game", game.rect()).asHUD();
		this.exitMenuButton = new Button(game.main, "Exit to Menu", game.rect()).asHUD();
		this.exitDesktopButton = new Button(game.main, "Exit to Desktop", game.rect()).asHUD();
	}

	@Override
	public void draw() {
		this.drawMenu(
			"You Died",
			this.respawnButton,
			this.restartButton,
			this.exitMenuButton,
			this.exitDesktopButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.respawnButton.clicked(event)) {
			this.game.respawn();
			this.game.main.setScreen(this.game);
		} else if (this.restartButton.clicked(event)) {
			this.game.main.setScreen(new GameScreen(this.game.main));
		} else if (this.exitMenuButton.clicked(event)) {
			this.game.main.setScreen(new MenuScreen(this.game.main));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.game.main.exit();
		}
	}
}
