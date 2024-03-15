package cs4303.p2.game.level.room;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.AxisDirection;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.Node;
import cs4303.p2.game.level.corridor.Corridor;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A leaf node in a room tree
 */
public final class LeafRoom extends Room {

	/**
	 * Corridors leading into this room
	 */
	public final List<Corridor> corridors = new ArrayList<>();

	/**
	 * Create a room
	 *
	 * @param game      game instance
	 * @param parent    parent room, or null if root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 */
	public LeafRoom(
		@NotNull GameScreen game,
		ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		super(game, parent, levelInfo, minX, minY, maxX, maxY);
	}

	@Override
	public void draw() {
		this.game.rect()
			.noStroke()
			.fill(this.game.main.ROOM_COLOR)
			.copy(this)
			.draw();
	}

	@NotNull
	@Override
	public LeafRoom findRoom(Axis axis, AxisDirection direction) {
		return this;
	}

	@Override
	public void appendRooms(@NotNull Collection<LeafRoom> result) {
		result.add(this);
	}

	@Override
	public void appendCorridors(Collection<Corridor> corridors) {
		//Nothing to do - all handled by parent
	}

	@Override
	public void appendNodes(@NotNull Collection<Node> nodes) {
		for (Corridor corridor : this.corridors) {
			if (this == corridor.room1) {
				nodes.addAll(corridor.nodes);
			}
		}
	}

	@Override
	public void connectNodes() {
		ArrayList<Node> nodes = new ArrayList<>(this.corridors.size());

		for (Corridor corridor : this.corridors) {
			Node node;
			if (this == corridor.room1) {
				node = corridor.startingNode();
			} else {
				node = corridor.endingNode();
			}
			nodes.add(node);
		}

		for (int i = 0; i < nodes.size(); i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				nodes.get(i)
					.connectTo(nodes.get(j));
			}
		}
	}

	@Override
	public void appendWalls(
		@NotNull Collection<HorizontalLine> horizontalWalls,
		@NotNull Collection<VerticalLine> verticalWalls
	) {
		HorizontalLine bottomWall = this.bottomEdge();
		VerticalLine rightWall = this.rightEdge();
		HorizontalLine topWall = this.topEdge();
		VerticalLine leftWall = this.leftEdge();
		for (Corridor corridor : this.corridors) {
			for (Rectangle segment : corridor.segments) {
				Rectangle intersection = segment.intersection(this);
				if(intersection == null) {
					continue;
				}
				if (bottomWall != null && segment.intersects(bottomWall)) {
					bottomWall = null;
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
					verticalWalls.add(VerticalLine.of(this.maxX, this.minY, intersection.minY()));
					verticalWalls.add(VerticalLine.of(this.maxX, intersection.maxY(), this.maxY));
				}
				if (topWall != null && segment.intersects(topWall)) {
					topWall = null;
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
