package cs4303.p2.game;

import cs4303.p2.util.collisions.Circle;
import cs4303.p2.util.collisions.Collidable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Player instance
 */
public class Player implements Circle {

	/**
	 * How many frames ago the camera will show the player's position
	 */
	public static final int CAMERA_LAG_FRAMES = 15;
	/**
	 * Magnitude of velocity of movement
	 */
	public static final float MOVEMENT_VELOCITY = 0.8f; // pixels / frame
	/**
	 * Player radius
	 */
	public static final float PLAYER_RADIUS = 8f;
	/**
	 * Player radius squared
	 */
	public static final float PLAYER_RADIUS_SQUARED = PLAYER_RADIUS * PLAYER_RADIUS;

	/**
	 * Game instance
	 */
	private final GameScreen game;
	/**
	 * Player's position
	 */
	private final PVector position;
	/**
	 * Player's velocity
	 */
	private final PVector velocity = new PVector();
	/**
	 * Position history
	 */
	private final Queue<PVector> positionHistory = new LinkedList<>();
	/**
	 * Whether the player should move left on next update
	 */
	public boolean left = false;
	/**
	 * Whether the player should move right on next update
	 */
	public boolean right = false;
	/**
	 * Whether the player should move up on next update
	 */
	public boolean up = false;
	/**
	 * Whether the player should move down on next update
	 */
	public boolean down = false;

	/**
	 * Construct a player
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Player(GameScreen game, PVector position) {
		this.game = game;
		this.position = position;
	}

	/**
	 * Draw the player
	 */
	public void draw() {
		this.game.main
			.ellipse()
			.copy(this)
			.fill(Color.MAGENTA)
			.draw();
	}

	/**
	 * Update the player's position based on movement
	 */
	public void update() {
		if (this.positionHistory.size() > CAMERA_LAG_FRAMES) {
			this.positionHistory.poll();
		}
		//Initial velocity of 0
		float velocityX = 0;
		float velocityY = 0;

		//Each key down will add a component to velocity
		if (this.left) {
			velocityX = -1;
		}
		if (this.right) {
			velocityX = 1;
		}
		if (this.up) {
			velocityY = -1;
		}
		if (this.down) {
			velocityY = 1;
		}

		//Set the velocity
		this.velocity.set(velocityX, velocityY)
			//Set the magnitude if non-zero. stops left+up moving at sqrt(2) * speed
			.setMag(MOVEMENT_VELOCITY);
		float newX = this.position.x + this.velocity.x;
		float newY = this.position.y + this.velocity.y;

		boolean breakX = false;
		boolean breakY = false;

		for (Collidable wall : this.game.walls) {
			if (!breakX && wall.closestDistanceSqFrom(newX, this.position.y) < PLAYER_RADIUS_SQUARED) {
				newX = this.position.x;
				breakX = true;
			}
			if (!breakY && wall.closestDistanceSqFrom(this.position.x, newY) < PLAYER_RADIUS_SQUARED) {
				newY = this.position.y;
				breakY = true;
			}
			if (breakX && breakY) {
				break;
			}
		}

		this.position.set(newX, newY);

		//Add this new position to the history
		this.positionHistory.add(this.position.copy());
	}

	/**
	 * Get the lagged position of the player, for the camera to display.
	 *
	 * @return lagged position
	 */
	public PVector laggedPosition() {
		return this.positionHistory.peek();
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
		return PLAYER_RADIUS;
	}
}
