package cs4303.p2.game.level;

import cs4303.p2.game.GameScreen;
import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

/**
 * An obstacle on the map
 */
public class Obstacle implements Circle {

	/**
	 * Game instance
	 */
	private final GameScreen game;
	/**
	 * Position of the obstacle
	 */
	private final PVector position;
	/**
	 * Radius of this obstacle
	 */
	private final float radius;

	/**
	 * Whether this obstacle has been exploded yet
	 */
	private boolean exploded;

	/**
	 * Create an obstacle
	 *
	 * @param game     game instance
	 * @param position position
	 * @param radius   radius
	 */
	public Obstacle(GameScreen game, PVector position, float radius) {
		this.game = game;
		this.position = position;
		this.radius = radius;
	}

	/**
	 * Draw this obstacle on the screen
	 */
	public void draw() {
		this.game.ellipse()
			.copy(this)
			.fill(this.game.main.OBSTACLE_COLOR)
			.draw();
	}

	/**
	 * Explode this obstacle
	 */
	public void explode() {
		this.exploded = true;
		this.game.addScore(this.game.main.OBSTACLE_EXPLODE_SCORE);
	}

	/**
	 * Whether this obstacle has exploded
	 *
	 * @return true if this obstacle has exploded, false otherwise
	 */
	public boolean exploded() {
		return this.exploded;
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
		return this.radius;
	}
}
