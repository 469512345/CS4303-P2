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
		return this.containsPoint(verticalLine.closestPoint(this.centreX(), this.centreY()));
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return this.containsPoint(horizontalLine.closestPoint(this.centreX(), this.centreY()));
	}

	@Override
	default boolean containsPoint(float x, float y) {
		float diffX = this.centreX() - x;
		float diffY = this.centreY() - y;
		return ((diffX * diffX) + (diffY * diffY)) <= (this.radius() * this.radius());
	}

	@Override
	default PVector closestPoint(float x, float y) {
		if (this.containsPoint(x, y)) {
			return new PVector(x, y);
		}
		PVector vector = new PVector(x - this.centreX(), y - this.centreY());
		vector.setMag(this.radius());
		return vector.add(this.centreX(), this.centreY());
	}
}