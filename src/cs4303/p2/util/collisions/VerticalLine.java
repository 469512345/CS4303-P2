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
		return Collidable.circleIntersectsVerticalLine(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default boolean intersects(Rectangle rectangle) {
		return Collidable.rectIntersectsVerticalLine(
			rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return Collidable.verticalLineIntersectsVerticalLine(
			this.x(), this.minY(), this.maxY(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectsVerticalLine(
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y(),
			this.x(), this.minY(), this.maxY()
		);
	}

	/**
	 * Calculate the intersection between a horizontal line and this line.
	 *
	 * @param horizontalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the horizontal line, or null if none exists
	 */
	default PVector intersection(HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectionWithVerticalLine(
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default PVector closestPoint(float x, float y) {
		return Collidable.verticalLineClosestPoint(
			this.x(), this.minY(), this.maxY(),
			x, y
		);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return Collidable.verticalLineContainsPoint(
			this.x(), this.minY(), this.maxY(),
			x, y
		);
	}

	/**
	 * Vertical line record implementation
	 *
	 * @param x    x coordinate
	 * @param minY minimum y coordinate
	 * @param maxY maximum y coordinate
	 */
	record VerticalLineImpl(
		float x,
		float minY,
		float maxY
	) implements VerticalLine {

	}
}
