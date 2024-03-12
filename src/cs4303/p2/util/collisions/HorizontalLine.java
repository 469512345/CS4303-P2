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
		return Collidable.circleIntersectsHorizontalLine(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.minX(), this.maxX(), this.y()
		);
	}

	@Override
	default boolean intersects(Rectangle rectangle) {
		return Collidable.rectIntersectsHorizontalLine(
			rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height(),
			this.minX(), this.maxX(), this.y()
		);
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return Collidable.horizontalLineIntersectsVerticalLine(
			this.minX(), this.maxX(), this.y(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectsHorizontalLine(
			this.minX(), this.maxX(), this.y(),
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y()
		);
	}

	/**
	 * Calculate the intersection between a vertical line and this line.
	 *
	 * @param verticalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the vertical line, or null if none exists
	 */
	default @Nullable PVector intersection(VerticalLine verticalLine) {
		return Collidable.horizontalLineIntersectionWithVerticalLine(
			this.minX(), this.maxX(), this.y(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default PVector closestPoint(float x, float y) {
		return Collidable.horizontalLineClosestPoint(
			this.minX(), this.maxX(), this.y(),
			x, y
		);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return Collidable.horizontalLineContainsPoint(
			this.minX(), this.maxX(), this.y(),
			x, y
		);
	}
}
