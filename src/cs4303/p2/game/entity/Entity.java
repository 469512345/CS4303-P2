package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import cs4303.p2.util.builder.EllipseBuilder;
import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

import java.awt.Color;

/**
 * Player instance
 */
public abstract class Entity implements Circle {

	/**
	 * Game instance
	 */
	protected final GameScreen game;
	/**
	 * Current position
	 */
	protected final PVector position;
	/**
	 * Current velocity
	 */
	protected final PVector velocity = new PVector();
	/**
	 * The current orientation
	 */
	protected float orientation = 0;

	/**
	 * Construct an entity
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Entity(GameScreen game, PVector position) {
		this.game = game;
		this.position = position;
	}

	/**
	 * Draw this entity on the screen
	 */
	public abstract void draw();

	/**
	 * Update this entity's position, velocity etc
	 */
	public abstract void update();

	/**
	 * Draw the entity as a circle with its eye
	 */
	protected void drawBase() {
		EllipseBuilder circle = this.game.main.ellipse();
		circle
			.copy(this)
			.fill(this.baseColor())
			.draw();
		circle
			.at(
				(float) (this.position.x + this.eyeDistance() * Math.cos(this.orientation)),
				(float) (this.position.y + this.eyeDistance() * Math.sin(this.orientation))
			)
			.radius(this.eyeRadius())
			.fill(this.eyeColor())
			.draw();
	}

	/**
	 * Colour of the entity's base
	 *
	 * @return colour of the entity's base
	 */
	protected abstract Color baseColor();

	/**
	 * Colour of the entity's eye
	 *
	 * @return colour of the entity's eye
	 */
	protected abstract Color eyeColor();

	/**
	 * Radius of the entity's eye
	 *
	 * @return radius of the entity's eye
	 */
	protected abstract float eyeRadius();

	/**
	 * Distance from the centre of the entity to the centre of its eye
	 *
	 * @return distance from the centre of the entity to the centre of its eye
	 */
	protected abstract float eyeDistance();

	/**
	 * Move the entity, respecting collisions with the map
	 */
	protected void move() {
		this.game.moveNoBounce(this.position, this.velocity, this.game.main.PLAYER_RADIUS_SQUARED);

		//Update velocity if there is currently movement
		if (this.velocity.mag() > 0) {
			this.updateOrientation();
		}
	}

	/**
	 * Update the eye's orientation. This method is very heavily based on the <a
	 * href="https://studres.cs.st-andrews.ac.uk/CS4303/Lectures/L8/FacingSketch/SmoothFacingCharacter.pde">FacingSketch
	 * included in L08</a>.
	 */
	private void updateOrientation() {
		//move a bit towards velocity:
		// turn vel into orientation
		float targetOrientation = (float) Math.atan2(this.velocity.y, this.velocity.x);

		// Will take a frame extra at the PI boundary
		if (Math.abs(targetOrientation - this.orientation) <= this.game.main.PLAYER_EYE_TURN_INCREMENT) {
			this.orientation = targetOrientation;
			return;
		}

		// if it's less than me, then how much if up to PI less, decrease otherwise increase
		if (targetOrientation < this.orientation) {
			if (this.orientation - targetOrientation < Math.PI)
				this.orientation -= this.game.main.PLAYER_EYE_TURN_INCREMENT;
			else this.orientation += this.game.main.PLAYER_EYE_TURN_INCREMENT;
		} else {
			if (targetOrientation - this.orientation < Math.PI)
				this.orientation += this.game.main.PLAYER_EYE_TURN_INCREMENT;
			else this.orientation -= this.game.main.PLAYER_EYE_TURN_INCREMENT;
		}

		// Keep in bounds
		if (this.orientation > Math.PI) this.orientation -= (float) (2 * Math.PI);
		else if (this.orientation < -Math.PI) this.orientation += (float) (2 * Math.PI);
	}

	/**
	 * Whether this entity has line of sight to a point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return true if this entity has line of sight to the point, false otherwise
	 */
	public boolean hasLineOfSight(float x, float y) {
		return this.game.lineOfSightBetween(this.position.x, this.position.y, x, y);
	}

	/**
	 * Whether this entity has line of sight to a point
	 *
	 * @param position position vector of point
	 *
	 * @return true if this entity has line of sight to the point, false otherwise
	 */
	public boolean hasLineOfSight(PVector position) {
		return this.hasLineOfSight(position.x, position.y);
	}

	/**
	 * Whether this entity has line of sight to another entity
	 *
	 * @param entity entity to test
	 *
	 * @return true if this entity has line of sight to the entity, false otherwise
	 */
	public boolean hasLineOfSight(Entity entity) {
		return this.hasLineOfSight(entity.position);
	}

	@Override
	public float centreX() {
		return this.position.x;
	}

	@Override
	public float centreY() {
		return this.position.y;
	}
}