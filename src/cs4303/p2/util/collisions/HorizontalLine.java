package cs4303.p2.util.collisions;

import cs4303.p2.util.annotation.Nullable;
import processing.core.PVector;

/**
 * A line object
 */
public interface HorizontalLine extends Collidable {

	/**
	 * y coordinate
	 *
	 * @return y coordinate
	 */
	float y();

	/**
	 * Minimum x coordinate
	 *
	 * @return minimum x coordinate
	 */
	float minX();

	/**
	 * Maximum x coordinate
	 *
	 * @return maximum x coordinate
	 */
	float maxX();

	@Override
	default boolean intersects(Circle circle) {
		return circle.intersects(this);
	}

	@Override
	default boolean intersects(Rectangle rectangle) {
		return rectangle.minY() > this.y() &&
			rectangle.maxY() < this.y() &&
			this.maxX() > rectangle.minX() &&
			this.minX() < rectangle.maxX();
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return verticalLine.x() > this.minX() &&
			verticalLine.x() < this.maxX() &&
			this.y() > verticalLine.minY() &&
			this.y() < verticalLine.maxY();
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return this.y() == horizontalLine.y() &&
			this.minX() < horizontalLine.maxX() &&
			this.maxX() > horizontalLine.minX();
	}

	/**
	 * Calculate the intersection between a vertical line and this line.
	 *
	 * @param verticalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the vertical line, or null if none exists
	 */
	default @Nullable PVector intersection(VerticalLine verticalLine) {
		if (!this.intersects(verticalLine)) {
			return null;
		}
		return new PVector(verticalLine.x(), this.y());
	}

	@Override
	default PVector closestPoint(float x, float y) {
		float closestX;
		float closestY = this.y();
		if (x < this.minX()) {
			closestX = this.minX();
		} else if (x > this.maxX()) {
			closestX = this.maxX();
		} else {
			closestX = x;
		}

		return new PVector(closestX, closestY);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		throw new UnsupportedOperationException("Lines do not contain an area");
	}
}
