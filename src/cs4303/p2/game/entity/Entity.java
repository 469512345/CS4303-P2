package cs4303.p2.game.entity;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Node;
import cs4303.p2.util.annotation.NotNull;
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
	public final GameScreen game;
	/**
	 * Current position
	 */
	public final PVector position;
	/**
	 * Current velocity
	 */
	public final PVector velocity = new PVector();
	/**
	 * The current orientation
	 */
	protected float orientation = 0;
	/**
	 * Whether this entity is alive
	 */
	protected boolean active = true;

	/**
	 * Construct a target
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Entity(GameScreen game, PVector position) {
		this.game = game;
		this.position = position;
	}

	/**
	 * Get a copy of the entity's current position
	 *
	 * @return copy of the entity's current position
	 */
	public PVector copyPosition() {
		return this.position.copy();
	}

	/**
	 * Draw this target on the screen
	 */
	public abstract void draw();

	/**
	 * Update this target's position, velocity etc
	 */
	public abstract void update();

	/**
	 * Draw the target as a circle with its eye
	 */
	protected void drawBase() {
		EllipseBuilder circle = this.game.ellipse();
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
	 * Colour of the target's base
	 *
	 * @return colour of the target's base
	 */
	public abstract Color baseColor();

	/**
	 * Colour of the target's eye
	 *
	 * @return colour of the target's eye
	 */
	protected abstract Color eyeColor();

	/**
	 * Radius of the target's eye
	 *
	 * @return radius of the target's eye
	 */
	public abstract float eyeRadius();

	/**
	 * Distance from the centre of the target to the centre of its eye
	 *
	 * @return distance from the centre of the target to the centre of its eye
	 */
	public float eyeDistance() {
		return this.radius() - this.eyeRadius();
	}

	/**
	 * Magnitude of the target's movement velocity
	 *
	 * @return magnitude of the target's movement velocity
	 */
	public abstract float velocityMagnitude();

	/**
	 * The type of target - robot or human
	 *
	 * @return type of target
	 */
	protected abstract EntityType type();

	/**
	 * Whether this target can see robots through walls
	 *
	 * @return true if this target can see robots through walls, false otherwise
	 */
	protected boolean canSeeRobotsThroughWalls() {
		return false;
	}

	/**
	 * Whether this target can be seen through walls by robots
	 *
	 * @return true if this target can be seen through walls by robots, false otherwise
	 */
	protected boolean canBeSeenThroughWallsByRobots() {
		return false;
	}

	/**
	 * Whether this target can see humans through walls.
	 *
	 * @return true if this target can see humans through walls, false otherwise
	 */
	protected boolean canSeeHumansThroughWalls() {
		return false;
	}

	/**
	 * Whether this target can be seen through walls by humans
	 *
	 * @return true if this target can be seen through walls by humans, false otherwise
	 */
	protected boolean canBeSeenThroughWallsByHumans() {
		return false;
	}

	/**
	 * Whether this entity is active, ie still alive and on the map
	 *
	 * @return true if this entity is active, false otherwise
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * Kill this entity, possibly adding to the score
	 */
	public abstract void kill();

	/**
	 * Remove this entity from the map without affecting the score
	 */
	public abstract void remove();

	/**
	 * Move the target, respecting collisions with the map
	 */
	public void move() {
		this.game.level.moveNoBounce(this.position, this.velocity, this.radius() * this.radius());

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
	 * Whether a target can see another target, respecting the values of {@link #canSeeHumansThroughWalls()},
	 * {@link #canSeeRobotsThroughWalls()}, {@link #canBeSeenThroughWallsByHumans()}, and
	 * {@link #canBeSeenThroughWallsByRobots()}.
	 *
	 * @param entity target to test
	 *
	 * @return true of this target can see the given target, false otherwise
	 */
	public boolean canSee(@NotNull Entity entity) {
		return entity.isActive() && (this.hasLineOfSight(entity) ||
			switch (entity.type()) {
				case HUMAN -> this.canSeeHumansThroughWalls();
				case ROBOT -> this.canSeeRobotsThroughWalls();
			} ||
			switch (this.type()) {
				case HUMAN -> entity.canBeSeenThroughWallsByHumans();
				case ROBOT -> entity.canBeSeenThroughWallsByRobots();
			});
	}

	/**
	 * Whether this enemy has line of sight to a point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return true if this enemy has line of sight to the point, false otherwise
	 */
	public boolean hasLineOfSight(float x, float y) {
		return this.game.level.lineOfSightBetween(this.position.x, this.position.y, x, y);
	}

	/**
	 * Whether this enemy has line of sight to a point
	 *
	 * @param position position vector of point
	 *
	 * @return true if this enemy has line of sight to the point, false otherwise
	 */
	public boolean hasLineOfSight(@NotNull PVector position) {
		return this.hasLineOfSight(position.x, position.y);
	}

	/**
	 * Whether this enemy has line of sight to a node
	 *
	 * @param node node
	 *
	 * @return true if this enemy has line of sight to the node, false otherwise
	 */
	public boolean hasLineOfSight(@NotNull Node node) {
		return this.hasLineOfSight(node.x(), node.y());
	}

	/**
	 * Whether this enemy has line of sight to another target
	 *
	 * @param target target to test
	 *
	 * @return true if this enemy has line of sight to the target, false otherwise
	 */
	public boolean hasLineOfSight(@NotNull Entity target) {
		return target.isActive() && this.hasLineOfSight(target.position);
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
