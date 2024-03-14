package cs4303.p2.game.powerup;

import cs4303.p2.game.GameScreen;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PVector;

/**
 * A powerup displayed in the world
 */
public class Powerup implements Rectangle {

	private final GameScreen game;
	/**
	 * Position of the powerup
	 */
	public final PVector position;
	/**
	 * Type of the powerup
	 */
	public final PowerupType type;
	/**
	 * Whether this powerup has been collected
	 */
	private boolean collected = false;

	/**
	 * Create a powerup
	 *
	 * @param game     game instance
	 * @param position position of the powerup
	 * @param type     type of the powerup
	 */
	protected Powerup(GameScreen game, PVector position, PowerupType type) {
		this.game = game;
		this.position = position;
		this.type = type;
	}

	/**
	 * Draw the powerup on screen
	 */
	public void draw() {
		this.type.draw(this.game, this.position);
	}

	@Override
	public float minX() {
		return this.position.x;
	}

	@Override
	public float minY() {
		return this.position.y;
	}

	@Override
	public float width() {
		return this.type.width;
	}

	@Override
	public float height() {
		return this.type.height;
	}

	/**
	 * Mark this powerup as collected and therefore for removal
	 */
	public void remove() {
		this.collected = true;
	}

	/**
	 * Whether this powerup has been collected, and should be removed
	 *
	 * @return true if it has been collected already, false otherwise
	 */
	public boolean collected() {
		return this.collected;
	}
}
