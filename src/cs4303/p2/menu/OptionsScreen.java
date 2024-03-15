package cs4303.p2.menu;

import cs4303.p2.Main;
import cs4303.p2.util.builder.Button;
import cs4303.p2.util.screen.Screen;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Options screen for the user to change simple options
 */
public class OptionsScreen extends AbstractMenuScreen {

	/**
	 * The screen to return to
	 */
	private final Screen returnScreen;
	/**
	 * Button for toggling friendly fire
	 */
	private final Button toggleFriendlyFireButton;
	/**
	 *
	 */
	private final Button toggleShowPathfindingInfoButton;
	/**
	 * Button to return back to the previous screen
	 */
	private final Button returnButton;

	/**
	 * Create a menu screen
	 *
	 * @param main         main instance
	 * @param returnScreen screen to return to once done
	 */
	public OptionsScreen(Main main, Screen returnScreen) {
		super(main);

		this.returnScreen = returnScreen;

		this.toggleFriendlyFireButton = new Button(this.main, "Friendly fire", this.main.rect());
		this.toggleShowPathfindingInfoButton = new Button(this.main, "Show Pathfinding Info", this.main.rect());
		this.returnButton = new Button(this.main, "Back", this.main.rect());
	}

	@Override
	protected void drawBackground() {
		this.main.background(this.main.MENU_BACKGROUND_COLOR.getRGB());
	}

	@Override
	public void draw() {
		String friendlyFire = this.main.FRIENDLY_FIRE ? "Enabled" : "Disabled";
		String showPathfindingInfo = this.main.SHOW_PATHFINDING_INFO ? "Enabled" : "Disabled";
		this.toggleFriendlyFireButton.text("Friendly fire: " + friendlyFire);
		this.toggleShowPathfindingInfoButton.text("Pathfinding info: " + showPathfindingInfo);
		this.drawMenu(
			"Options",
			this.toggleFriendlyFireButton,
			this.toggleShowPathfindingInfoButton,
			this.returnButton
		);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.toggleFriendlyFireButton.clicked(event)) {
			this.main.FRIENDLY_FIRE = !this.main.FRIENDLY_FIRE;
		} else if (this.toggleShowPathfindingInfoButton.clicked(event)) {
			this.main.SHOW_PATHFINDING_INFO = !this.main.SHOW_PATHFINDING_INFO;
		} else if (this.returnButton.clicked(event)) {
			this.main.setScreen(this.returnScreen);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == PConstants.ESC) {
			this.main.key = 0;
			this.main.keyCode = 0;
			this.main.setScreen(this.returnScreen);
		}
	}
}
