package cs4303.p2.game.level.corridor;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.Node;
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
public final class StraightCorridor extends Corridor implements Rectangle {

	/**
	 * First point - min x or min y - connecting to bottom or right of room 1
	 */
	private final Node node1;
	/**
	 * Second point - max x or max y - connecting to top or left of room 2
	 */
	private final Node node2;

	/**
	 * Create a new corridor
	 *
	 * @param game  game instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis of the corridor
	 * @param width width of the corridor
	 * @param node1 node in room 1
	 * @param node2 node in room 2
	 */
	public StraightCorridor(
		GameScreen game, LeafRoom room1, LeafRoom room2, Axis axis, float width, PVector node1, PVector node2
	) {
		super(game, room1, room2, axis, width);
		this.node1 = new Node(node1);
		this.node2 = new Node(node2);

		this.node1.connectTo(this.node2);

		this.nodes.add(this.node1);
		this.nodes.add(this.node2);

		this.segments.add(this);
	}

	/**
	 * Draw to screen
	 */
	@Override
	public void draw() {
		this.game.rect()
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
	public Node startingNode() {
		return this.node1;
	}

	@Override
	public Node endingNode() {
		return this.node2;
	}

	@Override
	public float minX() {
		return this.node1.x() - this.margin;
	}

	@Override
	public float minY() {
		return this.node1.y() - this.margin;
	}

	@Override
	public float width() {
		return this.node2.x() - this.node1.x() + this.width;
	}

	@Override
	public float height() {
		return this.node2.y() - this.node1.y() + this.width;
	}
}
