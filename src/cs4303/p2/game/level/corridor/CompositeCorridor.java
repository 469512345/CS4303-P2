package cs4303.p2.game.level.corridor;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.awt.Color;
import java.util.Collection;

/**
 * A corridor between two rooms. This will aim to be straight if possible, but will otherwise consist of 3 segments.
 */
public final class CompositeCorridor extends AbstractCorridor {

	private final PVector point1;
	private final PVector point2;
	private final PVector point3;
	private final PVector point4;

	private final Rectangle segment1;
	private final Rectangle segment2;
	private final Rectangle segment3;

	/**
	 * Create a new corridor
	 *
	 * @param game  game instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis of the corridor
	 * @param width width of the corridor
	 */
	public CompositeCorridor(
		GameScreen game, LeafRoom room1, LeafRoom room2, Axis axis, float width,
		PVector point1,
		PVector point2,
		PVector point3,
		PVector point4
	) {
		super(game, room1, room2, axis, width);
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.point4 = point4;

		this.points.add(point1);
		this.points.add(point2);
		this.points.add(point3);
		this.points.add(point4);

		this.segment1 = lineToRect(point1, point2, this.margin);
		this.segment2 = lineToRect(point2, point3, this.margin);
		this.segment3 = lineToRect(point3, point4, this.margin);

		this.segments.add(this.segment1);
		this.segments.add(this.segment2);
		this.segments.add(this.segment3);
	}

	/**
	 * Draw to screen
	 */
	@Override
	public void draw() {
		RectBuilder rect = this.game.main.rect()
			.fill(Color.WHITE);

		rect.copy(this.segment1)
			.draw();
		rect.copy(this.segment2)
			.draw();
		rect.copy(this.segment3)
			.draw();
	}

	/**
	 * Calculate the walls of this corridor and append them to the list of walls
	 *
	 * @param horizontalWalls collection of horizontal walls to append to
	 * @param verticalWalls   collection of vertical walls to append to
	 */
	@Override
	public void appendWalls(Collection<HorizontalLine> horizontalWalls, Collection<VerticalLine> verticalWalls) {
		if (this.axis == Axis.VERTICAL) {
			float sign = Math.signum(this.point4.y - this.point1.y);
			HorizontalLine seg1Top = HorizontalLine.of(
				this.room1.maxX,
				this.point2.x + sign * this.margin,
				this.point2.y - this.margin
			);
			HorizontalLine seg1Bottom = HorizontalLine.of(
				this.room1.maxX,
				this.point2.x - sign * this.margin,
				this.point2.y + this.margin
			);

			VerticalLine seg2Left = VerticalLine.of(
				this.point2.x - this.margin,
				this.point2.y + sign * this.margin,
				this.point3.y + sign * this.margin
			);
			VerticalLine seg2Right = VerticalLine.of(
				this.point2.x + this.margin,
				this.point2.y - sign * this.margin,
				this.point3.y - sign * this.margin
			);

			HorizontalLine seg3Top = HorizontalLine.of(
				this.point3.x + sign * this.margin,
				this.room2.minX,
				this.point3.y - this.margin
			);
			HorizontalLine seg3Bottom = HorizontalLine.of(
				this.point3.x - sign * this.margin,
				this.room2.minX,
				this.point3.y + this.margin
			);

			horizontalWalls.add(seg1Top);
			horizontalWalls.add(seg1Bottom);

			verticalWalls.add(seg2Left);
			verticalWalls.add(seg2Right);

			horizontalWalls.add(seg3Top);
			horizontalWalls.add(seg3Bottom);
		} else {
			float sign = Math.signum(this.point4.x - this.point1.x);

			VerticalLine seg1Left = VerticalLine.of(
				this.point2.x - this.margin,
				this.room1.maxY,
				this.point2.y + sign * this.margin
			);
			VerticalLine seg1Right = VerticalLine.of(
				this.point2.x + this.margin,
				this.room1.maxY,
				this.point2.y - sign * this.margin
			);

			HorizontalLine seg2Top = HorizontalLine.of(
				this.point2.x + sign * this.margin,
				this.point3.x + sign * this.margin,
				this.point2.y - this.margin
			);

			HorizontalLine seg2Bottom = HorizontalLine.of(
				this.point2.x - sign * this.margin,
				this.point3.x - sign * this.margin,
				this.point2.y + this.margin
			);

			VerticalLine seg3Left = VerticalLine.of(
				this.point3.x - this.margin,
				this.point3.y + sign * this.margin,
				this.room2.minY
			);
			VerticalLine seg3Right = VerticalLine.of(
				this.point3.x + this.margin,
				this.point3.y - sign * this.margin,
				this.room2.minY
			);

			verticalWalls.add(seg1Left);
			verticalWalls.add(seg1Right);

			horizontalWalls.add(seg2Top);
			horizontalWalls.add(seg2Bottom);

			verticalWalls.add(seg3Left);
			verticalWalls.add(seg3Right);

		}
	}
}
