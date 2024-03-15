package cs4303.p2.game.level.room;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.AxisDirection;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.level.Node;
import cs4303.p2.game.level.corridor.Corridor;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;

import java.util.Collection;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Abstract base class for a Room in the Partition Tree
 */
public sealed abstract class Room implements Rectangle permits LeafRoom, ContainerRoom {

	/**
	 * Main instance
	 */
	@NotNull
	protected final GameScreen game;
	/**
	 * Parent of this room, or null if this room is the root node
	 */
	@Nullable
	public final ContainerRoom parent;
	/**
	 * Parameters for level generation
	 */
	@NotNull
	public final LevelInfo levelInfo;
	/**
	 * Minimum x coordinate of room's bounds
	 */
	public final float minX;
	/**
	 * Minimum y coordinate of room's bounds
	 */
	public final float minY;
	/**
	 * Maximum x coordinate of room's bounds
	 */
	public final float maxX;
	/**
	 * Maximum y coordinate of room's bounds
	 */
	public final float maxY;

	/**
	 * Create a room
	 *
	 * @param game      main instance
	 * @param parent    parent room, or null if root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 */
	protected Room(
		@NotNull GameScreen game,
		@Nullable ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		this.game = game;
		this.parent = parent;
		this.levelInfo = levelInfo;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	/**
	 * Draw the room
	 */
	public abstract void draw();

	/**
	 * Find the room with the most extreme coordinate in a direction along an axis
	 *
	 * @param axis      axis of coordinates
	 * @param direction which side of the room should be compared
	 *
	 * @return room with the most extreme coordinate in the direction of the axis
	 */
	public abstract LeafRoom findRoom(Axis axis, AxisDirection direction);

	/**
	 * Append any leaf nodes contained within this region to a collection
	 *
	 * @param result collection to append to
	 */
	public abstract void appendRooms(Collection<LeafRoom> result);

	/**
	 * Append any corridors contained within this region to a collection
	 *
	 * @param corridors collection to append to
	 */
	public abstract void appendCorridors(Collection<Corridor> corridors);

	/**
	 * Append any nodes contained within this region to a collection
	 *
	 * @param nodes collection to append to
	 */
	public abstract void appendNodes(Collection<Node> nodes);

	/**
	 * Connect the nodes for any corridors into rooms in this region to eachother
	 */
	public abstract void connectNodes();

	/**
	 * Append any walls contained within this region to collections for horizontal and vertical
	 *
	 * @param horizontalWalls collection to append horizontal walls to
	 * @param verticalWalls   collection to append vertical walls to
	 */
	public abstract void appendWalls(
		Collection<HorizontalLine> horizontalWalls,
		Collection<VerticalLine> verticalWalls
	);

	@Override
	public float minX() {
		return this.minX;
	}

	@Override
	public float minY() {
		return this.minY;
	}

	@Override
	public float width() {
		return this.maxX - this.minX;
	}

	@Override
	public float height() {
		return this.maxY - this.minY;
	}

	/**
	 * Create a room
	 *
	 * @param game      game instance
	 * @param parent    parent room, or null if creating the root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 *
	 * @return created room
	 */
	public static Room createRoom(
		@NotNull GameScreen game,
		@Nullable ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		Random random = game.main.random;

		float width = maxX - minX;
		float height = maxY - minY;

		boolean verticalSplitAllowed = width > 2 * levelInfo.minRoomWidth();
		boolean horizontalSplitAllowed = height > 2 * levelInfo.minRoomHeight();

		boolean mustVerticalSplit = width > levelInfo.maxRoomWidth();
		boolean mustHorizontalSplit = height > levelInfo.maxRoomHeight();

		boolean passRandomChance = random.nextFloat(0, 1) < levelInfo.splitChance();

		if (!(mustVerticalSplit || mustHorizontalSplit) && ((!verticalSplitAllowed && !horizontalSplitAllowed) || !passRandomChance)) {
			float lower = levelInfo.minMargin();
			float upper = levelInfo.maxMargin();

			float leftMargin = random.nextFloat(lower, upper);
			float rightMargin = random.nextFloat(lower, upper);
			float topMargin = random.nextFloat(lower, upper);
			float bottomMargin = random.nextFloat(lower, upper);
			return new LeafRoom(
				game,
				parent,
				levelInfo,
				minX + leftMargin,
				minY + topMargin,
				maxX - bottomMargin,
				maxY - rightMargin
			);
		}

		Axis axis;

		if (mustVerticalSplit && !mustHorizontalSplit) {
			axis = Axis.VERTICAL;
		} else if (mustHorizontalSplit && !mustVerticalSplit) {
			axis = Axis.HORIZONTAL;
		} else if (!verticalSplitAllowed) {
			axis = Axis.HORIZONTAL;
		} else if (!horizontalSplitAllowed) {
			axis = Axis.VERTICAL;
		} else {
			axis = random.nextBoolean() ? Axis.VERTICAL : Axis.HORIZONTAL;
		}
		return new ContainerRoom(game, parent, levelInfo, minX, minY, maxX, maxY, axis);
	}

	/**
	 * Compare this room's coordinate to another room, along a given axis and direction along the axis
	 *
	 * @param other         room to compare against this room
	 * @param axis          axis for comparison
	 * @param axisDirection which side of the room to compare
	 *
	 * @return int < 0 for lower, int > 1 for greater, 0 for equal
	 */
	public int compareTo(Room other, Axis axis, AxisDirection axisDirection) {
		if (axis == Axis.HORIZONTAL) {
			if (axisDirection == AxisDirection.MIN) {
				return Float.compare(this.minX, other.minX);
			} else {
				return Float.compare(this.maxX, other.maxX);
			}
		} else {
			if (axisDirection == AxisDirection.MIN) {
				return Float.compare(this.minY, other.minY);
			} else {
				return Float.compare(this.maxY, other.maxY);
			}
		}
	}

	/**
	 * Create the root room
	 *
	 * @param game      game instance
	 * @param levelInfo parameters describing level generation
	 *
	 * @return created room instance
	 */
	public static Room createRoot(GameScreen game, LevelInfo levelInfo) {
		//Sometimes level generation will fail due to bad random numbers.
		//It's a dirty fix, but it works...
		while (true) {
			try {
				Room room = createRoom(game, null, levelInfo, 0, 0, levelInfo.width(), levelInfo.height());
				room.connectNodes();
				return room;
			} catch (IllegalArgumentException ignored) {
			}
		}
	}

	@Override
	public String toString() {
		return new StringJoiner(
			", ",
			this.getClass()
				.getSimpleName() + "[",
			"]"
		)
			.add("minX=" + this.minX)
			.add("minY=" + this.minY)
			.add("maxX=" + this.maxX)
			.add("maxY=" + this.maxY)
			.toString();
	}
}
