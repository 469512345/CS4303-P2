package cs4303.p2.util.collisions;

import processing.core.PVector;

/**
 * This interface represents a collidable object which can be described as a circle
 */
public interface Circle extends Collidable {

	/**
	 * X coordinate of centre of the circle
	 *
	 * @return X coordinate of centre of the circle
	 */
	float centreX();

	/**
	 * Y coordinate of centre of the circle
	 *
	 * @return Y coordinate of centre of the circle
	 */
	float centreY();

	/**
	 * Radius of the circle
	 *
	 * @return Radius of the circle
	 */
	float radius();

	@Override
	default boolean intersects(Circle other) {
		return Collidable.circleIntersectsCircle(
			this.centreX(), this.centreY(), this.radius(),
			other.centreX(), other.centreY(), other.radius()
		);
	}

	@Override
	default boolean intersects(Rectangle other) {
		return Collidable.circleIntersectsRect(
			this.centreX(), this.centreY(), this.radius(),
			other.minX(), other.minY(), other.width(), other.height()
		);
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return Collidable.circleIntersectsVerticalLine(
			this.centreX(), this.centreY(), this.radius(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return Collidable.circleIntersectsHorizontalLine(
			this.centreX(), this.centreY(), this.radius(),
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y()
		);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return Collidable.circleContainsPoint(
			this.centreX(), this.centreY(), this.radius(),
			x, y
		);
	}

	@Override
	default PVector closestPoint(float x, float y) {
		return Collidable.circleClosestPoint(
			this.centreX(), this.centreY(), this.radius(),
			x, y
		);
	}

	/**
	 * Create a new circle instance given its parameters
	 *
	 * @param centreX x coordinate of centre of circle
	 * @param centreY y coordinate of centre of circle
	 * @param radius  radius of circle
	 *
	 * @return circle with provided centre and radius
	 */
	static Circle of(float centreX, float centreY, float radius) {
		return new CircleImpl(centreX, centreY, radius);
	}

	/**
	 * Circle record implementation
	 *
	 * @param centreX x coordinate of centre of circle
	 * @param centreY y coordinate of centre of circle
	 * @param radius  radius of circle
	 */
	record CircleImpl(
		float centreX,
		float centreY,
		float radius
	) implements Circle {

	}
}