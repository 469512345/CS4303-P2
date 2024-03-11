package cs4303.p2.util.collisions;

import processing.core.PVector;

/**
 * A line object
 */
public interface VerticalLine extends Collidable {

	/**
	 * x coordinate
	 *
	 * @return x coordinate
	 */
	float x();

	/**
	 * Minimum y coordinate
	 *
	 * @return minimum y coordinate
	 */
	float minY();

	/**
	 * Maximum y coordinate
	 *
	 * @return maximum y coordinate
	 */
	float maxY();

	@Override
	default boolean intersects(Circle circle) {
		return circle.intersects(this);
	}

	@Override
	default boolean intersects(Rectangle rectangle) {
		return rectangle.minX() > this.x() &&
			rectangle.maxX() < this.x() &&
			this.maxY() > rectangle.minY() &&
			this.minY() < rectangle.maxY();
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return this.x() == verticalLine.x() &&
			this.minY() < verticalLine.maxY() &&
			this.maxY() > verticalLine.minY();
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return this.x() > horizontalLine.minX() &&
			this.x() < horizontalLine.maxX() &&
			horizontalLine.y() > this.minY() &&
			horizontalLine.y() < this.maxY();
	}

	/**
	 * Calculate the intersection between a horizontal line and this line.
	 *
	 * @param horizontalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the horizontal line, or null if none exists
	 */
	default PVector intersection(HorizontalLine horizontalLine) {
		if (!this.intersects(horizontalLine)) {
			return null;
		}
		return new PVector(this.x(), horizontalLine.y());
	}

	@Override
	default PVector closestPoint(float x, float y) {
		float closestX = this.x();
		float closestY;
		if (y < this.minY()) {
			closestY = this.minY();
		} else if (y > this.maxY()) {
			closestY = this.maxY();
		} else {
			closestY = y;
		}
		return new PVector(closestX, closestY);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		throw new UnsupportedOperationException("Lines do not contain an area");
	}
}
