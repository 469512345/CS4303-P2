package cs4303.p2.util.collisions;

import cs4303.p2.util.annotation.NotNull;
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
	default boolean intersects(@NotNull Circle circle) {
		return Collidable.circleIntersectsHorizontalLine(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.minX(), this.maxX(), this.y()
		);
	}

	@Override
	default boolean intersects(@NotNull Rectangle rectangle) {
		return Collidable.rectIntersectsHorizontalLine(
			rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height(),
			this.minX(), this.maxX(), this.y()
		);
	}

	@Override
	default boolean intersects(@NotNull VerticalLine verticalLine) {
		return Collidable.horizontalLineIntersectsVerticalLine(
			this.minX(), this.maxX(), this.y(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectsHorizontalLine(
			this.minX(), this.maxX(), this.y(),
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y()
		);
	}

	@Override
	default boolean intersects(@NotNull Line line) {
		return Collidable.horizontalLineIntersectsLine(
			this.minX(), this.maxX(), this.y(),
			line.x1(), line.y1(), line.x2(), line.y2()
		);
	}

	/**
	 * Calculate the intersection between a vertical line and this line.
	 *
	 * @param verticalLine line to calculate intersection with
	 *
	 * @return Point of intersection between this line and the vertical line, or null if none exists
	 */
	default @Nullable PVector intersection(@NotNull VerticalLine verticalLine) {
		return Collidable.horizontalLineIntersectionWithVerticalLine(
			this.minX(), this.maxX(), this.y(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
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
			this.minX(), this.y(), this.maxX(), this.y()
		);
	}

	@NotNull
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

	/**
	 * Create a new horizontal line with given parameters. This will ensure that the minimum of x1 and x2 becomes
	 * {@link #minX()}, and similarly for {@link #maxX()}.
	 *
	 * @param x1 x coordinate of one end of the line
	 * @param x2 x coordinate of the other end of the line
	 * @param y  y coordinate of line
	 *
	 * @return horizontal line with given parameters
	 */
	@NotNull
	static HorizontalLine of(float x1, float x2, float y) {
		float minX = Math.min(x1, x2);
		float maxX = Math.max(x1, x2);
		return new HorizontalLineImpl(minX, maxX, y);
	}

	/**
	 * Horizontal line record implementation
	 *
	 * @param minX minimum x coordinate
	 * @param maxX maximum x coordinate
	 * @param y    y coordinate
	 */
	record HorizontalLineImpl(
		float minX,
		float maxX,
		float y
	) implements HorizontalLine {

	}
}
