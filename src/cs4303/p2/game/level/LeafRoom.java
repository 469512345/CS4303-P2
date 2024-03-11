package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.collisions.Rectangle;

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
	public final List<Corridor> corridors = new ArrayList<>();

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
	public void appendWalls(Collection<HorizontalWall> horizontalWalls, Collection<VerticalWall> verticalWalls) {
		appendWalls(this, horizontalWalls, verticalWalls);
		for (Corridor corridor : this.corridors) {
			//Only consider corridors where this is room1
			//This will prevent corridors being counted in duplicate
			if (corridor.room1 != this) {
				continue;
			}
			for (Rectangle segment : corridor.segments) {
				appendWalls(segment, horizontalWalls, verticalWalls);
			}
		}
	}

	/**
	 * Determine the horizontalWalls of a rectangle and add them to a collection
	 *
	 * @param rectangle       rectangle to determine
	 * @param horizontalWalls collection to append to
	 */
	private static void appendWalls(
		Rectangle rectangle,
		Collection<HorizontalWall> horizontalWalls,
		Collection<VerticalWall> verticalWalls
	) {
		float minX = rectangle.minX();
		float minY = rectangle.minY();
		float maxX = rectangle.maxX();
		float maxY = rectangle.maxY();

		horizontalWalls.add(new HorizontalWall(minX, maxX, minY));
		horizontalWalls.add(new HorizontalWall(minX, maxX, maxY));
		verticalWalls.add(new VerticalWall(minX, minY, maxY));
		verticalWalls.add(new VerticalWall(maxX, minY, maxY));
	}
}
