package cs4303.p2.util.collisions;

import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
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
	default boolean intersects(@NotNull Circle circle) {
		return Collidable.circleIntersectsVerticalLine(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull Rectangle rectangle) {
		return Collidable.rectIntersectsVerticalLine(
			rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull VerticalLine verticalLine) {
		return Collidable.verticalLineIntersectsVerticalLine(
			this.x(), this.minY(), this.maxY(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectsVerticalLine(
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y(),
			this.x(), this.minY(), this.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull Line line) {
		return Collidable.verticalLineIntersectsLine(
			this.x(), this.minY(), this.maxY(),
			line.x1(), line.y1(), line.x2(), line.y2()
		);
	}

	/**
	 * Calculate the intersection between a horizontal line and this line.
	 *
	 * @param horizontalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the horizontal line, or null if none exists
	 */
	@Nullable
	default PVector intersection(@NotNull HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectionWithVerticalLine(
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y(),
			this.x(), this.minY(), this.maxY()
		);
	}

	/**
	 * Calculate the intersection between an arbitrary line and this line.
	 *
	 * @param line line to calculate intersection with
	 *
	 * @return point of intersection between this line and the other line, or null if none exists
	 */
	@Nullable
	default PVector intersection(@NotNull Line line) {
		return Collidable.lineIntersectionWithLine(
			line.x1(), line.y1(), line.x2(), line.y2(),
			this.x(), this.minY(), this.x(), this.maxY()
		);
	}

	@NotNull
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
	 * Create a vertical line with the given parameters
	 *
	 * @param x  x coordinate of the line
	 * @param y1 y coordinate of one end of the line
	 * @param y2 y coordinate of the other end of the line
	 *
	 * @return vertical line between the given points
	 */
	@NotNull
	static VerticalLine of(float x, float y1, float y2) {
		float minY = Math.min(y1, y2);
		float maxY = Math.max(y1, y2);

		return new VerticalLineImpl(x, minY, maxY);
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
