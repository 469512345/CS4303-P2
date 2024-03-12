package cs4303.p2.game.level.room;

import cs4303.p2.Main;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.AxisDirection;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.corridor.AbstractCorridor;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A leaf node in a room tree
 */
public final class LeafRoom extends AbstractRoom {

	/**
	 * Corridors leading into this room
	 */
	public final List<AbstractCorridor> corridors = new ArrayList<>();

	/**
	 * Create a room
	 *
	 * @param main      main instance
	 * @param parent    parent room, or null if root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 */
	public LeafRoom(
		Main main,
		ContainerRoom parent,
		LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		super(main, parent, levelInfo, minX, minY, maxX, maxY);
	}

	@Override
	public void draw() {
		this.main.rect()
			.noStroke()
			.fill(Color.WHITE)
			.copy(this)
			.draw();
	}

	@Override
	public LeafRoom findRoom(Axis axis, AxisDirection direction) {
		return this;
	}

	@Override
	public void appendRooms(Collection<LeafRoom> result) {
		result.add(this);
	}

	@Override
	public void appendWalls(
		Collection<HorizontalLine> horizontalWalls,
		Collection<VerticalLine> verticalWalls
	) {
		HorizontalLine bottomWall = this.bottomEdge();
		VerticalLine rightWall = this.rightEdge();
		HorizontalLine topWall = this.topEdge();
		VerticalLine leftWall = this.leftEdge();
		for (AbstractCorridor corridor : this.corridors) {
			for (Rectangle segment : corridor.segments) {
				if (bottomWall != null && segment.intersects(bottomWall)) {
					bottomWall = null;
					Rectangle intersection = segment.intersection(this);
					horizontalWalls.add(HorizontalLine.of(
						this.minX,
						intersection.minX(),
						this.maxY
					));
					horizontalWalls.add(HorizontalLine.of(
						intersection.maxX(),
						this.maxX,
						this.maxY
					));
				}
				if (rightWall != null && segment.intersects(rightWall)) {
					rightWall = null;
					Rectangle intersection = segment.intersection(this);
					verticalWalls.add(VerticalLine.of(this.maxX, this.minY, intersection.minY()));
					verticalWalls.add(VerticalLine.of(this.maxX, intersection.maxY(), this.maxY));
				}
				if (topWall != null && segment.intersects(topWall)) {
					topWall = null;
					Rectangle intersection = segment.intersection(this);
					horizontalWalls.add(HorizontalLine.of(
						this.minX,
						intersection.minX(),
						this.minY
					));
					horizontalWalls.add(HorizontalLine.of(
						intersection.maxX(),
						this.maxX,
						this.minY
					));
				}
				if (leftWall != null && segment.intersects(leftWall)) {
					leftWall = null;
					Rectangle intersection = segment.intersection(this);
					verticalWalls.add(VerticalLine.of(this.minX, this.minY, intersection.minY()));
					verticalWalls.add(VerticalLine.of(this.minX, intersection.maxY(), this.maxY));
				}

				//Only consider corridors where this is room1
				//This will prevent corridors being counted in duplicate
				if (corridor.room1 == this) {
					corridor.appendWalls(horizontalWalls, verticalWalls);
				}
			}
		}
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
