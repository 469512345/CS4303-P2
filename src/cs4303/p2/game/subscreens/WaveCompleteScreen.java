package cs4303.p2.game.subscreens;

import cs4303.p2.game.GameScreen;
import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.Button;
import processing.event.MouseEvent;

/**
 * Wave complete screen
 */
public final class WaveCompleteScreen extends AbstractGameMenuScreen {

	/**
	 * Button to move onto next wave
	 */
	private final Button nextWaveButton;
	/**
	 * Exit to main menu button
	 */
	private final Button exitMenuButton;
	/**
	 * Exit to desktop button
	 */
	private final Button exitDesktopButton;

	/**
	 * Create a wave complete screen
	 *
	 * @param game game instance
	 */
	public WaveCompleteScreen(GameScreen game) {
		super(game);
		this.nextWaveButton = new Button(game.main, "Next Wave", game.main.rect()).asHUD();
		this.exitMenuButton = new Button(game.main, "Exit to Menu", game.main.rect()).asHUD();
		this.exitDesktopButton = new Button(game.main, "Exit to Desktop", game.main.rect()).asHUD();
	}

	@Override
	public void draw() {
		this.drawMenu(
			"Wave Complete",
			this.nextWaveButton,
			this.exitMenuButton,
			this.exitDesktopButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.nextWaveButton.clicked(event)) {
			this.game.main.setScreen(this.game.nextWave());
		} else if (this.exitMenuButton.clicked(event)) {
			this.game.main.setScreen(new MenuScreen(this.game.main));
		} else if (this.exitDesktopButton.clicked(event)) {
			this.game.main.exit();
		}
	}
}
