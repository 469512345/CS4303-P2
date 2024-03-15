package cs4303.p2.game.subscreens;

import cs4303.p2.game.GameScreen;
import cs4303.p2.menu.AbstractMenuScreen;
import cs4303.p2.util.annotation.NotNull;

import java.awt.Color;

/**
 * Abstract menu screen during the game. These screens will show the game in the background with an overlay. The game
 * will not update while a menu screen is open.
 */
public abstract class AbstractGameMenuScreen extends AbstractMenuScreen {

	/**
	 * Game instance
	 */
	@NotNull
	protected final GameScreen game;

	/**
	 * Create an abstract game menu screen
	 *
	 * @param game game instance
	 */
	public AbstractGameMenuScreen(@NotNull GameScreen game) {
		super(game.main);
		this.game = game;
	}

	@Override
	protected void drawBackground() {
		this.game.justDraw();
		this.rect()
			.asHUD()
			.at(0, 0)
			.size(this.main.width, this.main.height)
			.fill(this.overlayColor())
			.draw();
	}

	/**
	 * Colour of the overlay over the game background
	 *
	 * @return colour of the overlay over the game background
	 */
	@NotNull
	protected Color overlayColor() {
		return this.main.OVERLAY_COLOR;
	}

	//Apply any viewport transformations that the game is applying
	@Override
	public float scale() {
		return this.game.scale();
	}

	@Override
	public float offsetX() {
		return this.game.offsetX();
	}

	@Override
	public float offsetY() {
		return this.game.offsetY();
	}
}
