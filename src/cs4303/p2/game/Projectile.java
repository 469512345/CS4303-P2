package cs4303.p2.game;

import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

/**
 * A player's projectile
 */
public class Projectile implements Circle {

	/**
	 * Game instance
	 */
	private final GameScreen game;
	/**
	 * Current position
	 */
	private final PVector position;
	/**
	 * Current velocity
	 */
	private final PVector velocity;
	/**
	 * Number of bounces remaining
	 */
	private int bouncesRemaining;
	/**
	 * Whether this projectile has expired, either through hitting something, or running out of bounces.
	 */
	private boolean expired = false;

	/**
	 * Create a new projectile
	 *
	 * @param game     game instance
	 * @param position initial position
	 * @param velocity initial velocity
	 */
	public Projectile(GameScreen game, PVector position, PVector velocity) {
		this.game = game;
		this.position = position;
		this.velocity = velocity;

		this.bouncesRemaining = this.game.main.PLAYER_PROJECTILE_MAX_BOUNCES;
	}

	/**
	 * Draw this projectile
	 */
	public void draw() {
		this.game.main.ellipse()
			.copy(this)
			.fill(this.game.main.PLAYER_PROJECTILE_COLOR)
			.noStroke()
			.draw();
	}

	/**
	 * Update this projectile's position
	 */
	public void update() {
		this.bouncesRemaining -= this.game.moveBounce(
			this.position,
			this.velocity,
			this.game.main.PLAYER_PROJECTILE_RADIUS_SQUARED
		);
		if (this.bouncesRemaining < 0) {
			this.expired = true;
		}
	}

	/**
	 * Whether this projectile has expired, either through hitting an enemy or reaching the maximum number of bounces
	 *
	 * @return whether this projectile has expired
	 */
	public boolean expired() {
		return this.expired;
	}

	@Override
	public float centreX() {
		return this.position.x;
	}

	@Override
	public float centreY() {
		return this.position.y;
	}

	@Override
	public float radius() {
		return this.game.main.PLAYER_PROJECTILE_RADIUS;
	}
}
