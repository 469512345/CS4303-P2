package cs4303.p2.game.level.corridor;

import cs4303.p2.Main;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.TextBuilder;
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

	/**
	 * Create a new corridor
	 *
	 * @param main   main instance
	 * @param room1  first room
	 * @param room2  second room
	 * @param axis   axis of the corridor
	 * @param width width of the corridor
	 */
	public CompositeCorridor(
		Main main, LeafRoom room1, LeafRoom room2, Axis axis, float width,
		PVector point1,
		PVector point2,
		PVector point3,
		PVector point4
	) {
		super(main, room1, room2, axis, width);
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.point4 = point4;

		this.points.add(point1);
		this.points.add(point2);
		this.points.add(point3);
		this.points.add(point4);

		this.segments.add(lineToRect(point1, point2, this.margin));
		this.segments.add(lineToRect(point2, point3, this.margin));
		this.segments.add(lineToRect(point3, point4, this.margin));
	}

	/**
	 * Draw to screen
	 */
	@Override
	public void draw() {
		RectBuilder rect = this.main.rect()
			.fill(Color.WHITE);

		TextBuilder text = this.main.text(null)
			.size(40)
			.fill(Color.BLACK);

		for (Rectangle segment : this.segments) {
			rect.copy(segment)
				.draw();

			if (segment.intersects(this.room1)) {
				text.text("1")
					.centredInRect(segment.intersection(this.room1))
					.draw();
			}
			if (segment.intersects(this.room2)) {
				text.text("2")
					.centredInRect(segment.intersection(this.room2))
					.draw();
			}
		}
	}

	/**
	 * Calculate the walls of this corridor and append them to the list of walls
	 *
	 * @param horizontalWalls collection of horizontal walls to append to
	 * @param verticalWalls   collection of vertical walls to append to
	 */
	@Override
	public void appendWalls(Collection<HorizontalLine> horizontalWalls, Collection<VerticalLine> verticalWalls) {
		for (Rectangle segment : this.segments) {
			HorizontalLine bottomWall = segment.bottomEdge();
			VerticalLine rightWall = segment.rightEdge();
			HorizontalLine topWall = segment.topEdge();
			VerticalLine leftWall = segment.leftEdge();

			if (bottomWall != null) {
				horizontalWalls.add(bottomWall);
			}
			if (rightWall != null) {
				verticalWalls.add(rightWall);
			}
			if (topWall != null) {
				horizontalWalls.add(topWall);
			}
			if (leftWall != null) {
				verticalWalls.add(leftWall);
			}
		}
	}
}
