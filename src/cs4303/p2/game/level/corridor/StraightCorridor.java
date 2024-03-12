package cs4303.p2.game.level.corridor;

import cs4303.p2.Main;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.awt.Color;
import java.util.Collection;

/**
 * A corridor between two rooms. This will aim to be straight if possible, but will otherwise consist of 3 segments.
 */
public final class StraightCorridor extends AbstractCorridor implements Rectangle {

	/**
	 * First point - min x or min y
	 */
	private final PVector point1;
	/**
	 * Second point - max x or max y
	 */
	private final PVector point2;

	/**
	 * Create a new corridor
	 *
	 * @param main  main instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis of the corridor
	 * @param width width of the corridor
	 */
	public StraightCorridor(
		Main main, LeafRoom room1, LeafRoom room2, Axis axis, float width, PVector point1, PVector point2
	) {
		super(main, room1, room2, axis, width);
		this.point1 = point1;
		this.point2 = point2;

		this.points.add(point1);
		this.points.add(point2);

		this.segments.add(this);
	}

	/**
	 * Draw to screen
	 */
	@Override
	public void draw() {
		this.main.rect()
			.fill(Color.WHITE)
			.copy(this)
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
		if (this.axis == Axis.HORIZONTAL) {
			VerticalLine leftWall = VerticalLine.of(
				this.minX(),
				this.room1.maxY,
				this.room2.minY
			);
			VerticalLine rightWall = VerticalLine.of(
				this.maxX(),
				this.room1.maxY,
				this.room2.minY
			);
			verticalWalls.add(leftWall);
			verticalWalls.add(rightWall);
		} else {
			HorizontalLine topWall = HorizontalLine.of(
				this.room1.maxX,
				this.room2.minX,
				this.minY()
			);
			HorizontalLine bottomWall = HorizontalLine.of(
				this.room1.maxX,
				this.room2.minX,
				this.maxY()
			);
			horizontalWalls.add(topWall);
			horizontalWalls.add(bottomWall);
		}
	}

	@Override
	public float minX() {
		return this.point1.x - this.margin;
	}

	@Override
	public float minY() {
		return this.point1.y - this.margin;
	}

	@Override
	public float width() {
		return this.point2.x - this.point1.x + this.width;
	}

	@Override
	public float height() {
		return this.point2.y - this.point1.y + this.width;
	}
}
