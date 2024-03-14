package cs4303.p2.game.level.corridor;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.Node;
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
public final class CompositeCorridor extends Corridor {

	/**
	 * Node connecting to the bottom or right of room 1
	 */
	private final Node node1;
	/**
	 * Node of first corner
	 */
	private final Node node2;
	/**
	 * Node of second corner
	 */
	private final Node node3;
	/**
	 * Node connecting to the top or left of room s
	 */
	private final Node node4;

	/**
	 * Segment between node 1 and node 2
	 */
	private final Rectangle segment1;
	/**
	 * Segment between node 2 and node 3
	 */
	private final Rectangle segment2;
	/**
	 * Segment between node 3 and node 4
	 */
	private final Rectangle segment3;

	/**
	 * Create a new corridor
	 *
	 * @param game  game instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis of the corridor
	 * @param width width of the corridor
	 * @param node1 position of first node
	 * @param node2 position of second node
	 * @param node3 position of third node
	 * @param node4 position of last node
	 */
	public CompositeCorridor(
		GameScreen game, LeafRoom room1, LeafRoom room2, Axis axis, float width,
		PVector node1,
		PVector node2,
		PVector node3,
		PVector node4
	) {
		super(game, room1, room2, axis, width);
		this.node1 = new Node(node1);
		this.node2 = new Node(node2);
		this.node3 = new Node(node3);
		this.node4 = new Node(node4);

		this.node1.connectTo(this.node2);
		this.node2.connectTo(this.node3);
		this.node3.connectTo(this.node4);

		this.nodes.add(this.node1);
		this.nodes.add(this.node2);
		this.nodes.add(this.node3);
		this.nodes.add(this.node4);

		this.segment1 = lineToRect(node1, node2, this.margin);
		this.segment2 = lineToRect(node2, node3, this.margin);
		this.segment3 = lineToRect(node3, node4, this.margin);

		this.segments.add(this.segment1);
		this.segments.add(this.segment2);
		this.segments.add(this.segment3);
	}

	/**
	 * Draw to screen
	 */
	@Override
	public void draw() {
		RectBuilder rect = this.game.rect()
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
			float sign = Math.signum(this.node4.y() - this.node1.y());
			HorizontalLine seg1Top = HorizontalLine.of(
				this.room1.maxX,
				this.node2.x() + sign * this.margin,
				this.node2.y() - this.margin
			);
			HorizontalLine seg1Bottom = HorizontalLine.of(
				this.room1.maxX,
				this.node2.x() - sign * this.margin,
				this.node2.y() + this.margin
			);

			VerticalLine seg2Left = VerticalLine.of(
				this.node2.x() - this.margin,
				this.node2.y() + sign * this.margin,
				this.node3.y() + sign * this.margin
			);
			VerticalLine seg2Right = VerticalLine.of(
				this.node2.x() + this.margin,
				this.node2.y() - sign * this.margin,
				this.node3.y() - sign * this.margin
			);

			HorizontalLine seg3Top = HorizontalLine.of(
				this.node3.x() + sign * this.margin,
				this.room2.minX,
				this.node3.y() - this.margin
			);
			HorizontalLine seg3Bottom = HorizontalLine.of(
				this.node3.x() - sign * this.margin,
				this.room2.minX,
				this.node3.y() + this.margin
			);

			horizontalWalls.add(seg1Top);
			horizontalWalls.add(seg1Bottom);

			verticalWalls.add(seg2Left);
			verticalWalls.add(seg2Right);

			horizontalWalls.add(seg3Top);
			horizontalWalls.add(seg3Bottom);
		} else {
			float sign = Math.signum(this.node4.x() - this.node1.x());

			VerticalLine seg1Left = VerticalLine.of(
				this.node2.x() - this.margin,
				this.room1.maxY,
				this.node2.y() + sign * this.margin
			);
			VerticalLine seg1Right = VerticalLine.of(
				this.node2.x() + this.margin,
				this.room1.maxY,
				this.node2.y() - sign * this.margin
			);

			HorizontalLine seg2Top = HorizontalLine.of(
				this.node2.x() + sign * this.margin,
				this.node3.x() + sign * this.margin,
				this.node2.y() - this.margin
			);

			HorizontalLine seg2Bottom = HorizontalLine.of(
				this.node2.x() - sign * this.margin,
				this.node3.x() - sign * this.margin,
				this.node2.y() + this.margin
			);

			VerticalLine seg3Left = VerticalLine.of(
				this.node3.x() - this.margin,
				this.node3.y() + sign * this.margin,
				this.room2.minY
			);
			VerticalLine seg3Right = VerticalLine.of(
				this.node3.x() + this.margin,
				this.node3.y() - sign * this.margin,
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

	@Override
	public Node startingNode() {
		return this.node1;
	}

	@Override
	public Node endingNode() {
		return this.node4;
	}
}
