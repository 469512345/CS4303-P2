package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.Rectangle;

import java.util.Collection;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Abstract base class for a Room in the Partition Tree
 */
public sealed abstract class AbstractRoom implements Rectangle permits LeafRoom, ContainerRoom {

	/**
	 * Main instance
	 */
	@NotNull
	protected final Main main;
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
	 * @param main      main instance
	 * @param parent    parent room, or null if root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 */
	protected AbstractRoom(
		@NotNull Main main,
		@Nullable ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		this.main = main;
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
	 * Append any horizontalWalls contained within this region to a collection
	 *
	 * @param result collection to append to
	 */
	public abstract void appendWalls(Collection<HorizontalWall> horizontalWalls, Collection<VerticalWall> verticalWalls);

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
	 * @param main      main instance
	 * @param parent    parent room, or null if creating the root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 *
	 * @return created room
	 */
	public static AbstractRoom createRoom(
		@NotNull Main main,
		@Nullable ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		Random random = main.random;

		float width = maxX - minX;
		float height = maxY - minY;

		boolean verticalSplitAllowed = width > 2 * levelInfo.minRoomWidth();
		boolean horizontalSplitAllowed = height > 2 * levelInfo.minRoomHeight();

		boolean mustVerticalSplit = width > levelInfo.maxRoomWidth();
		boolean mustHorizontalSplit = height > levelInfo.maxRoomHeight();

		boolean passRandomChance = random.nextFloat(0, 1) < levelInfo.splitChance();

		if (!(mustVerticalSplit || mustHorizontalSplit) && (!verticalSplitAllowed && !horizontalSplitAllowed || !passRandomChance)) {
			float lower = levelInfo.minMargin();
			float upper = levelInfo.maxMargin();

			float leftMargin = random.nextFloat(lower, upper);
			float rightMargin = random.nextFloat(lower, upper);
			float topMargin = random.nextFloat(lower, upper);
			float bottomMargin = random.nextFloat(lower, upper);
			return new LeafRoom(
				main,
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
			axis = main.random.nextBoolean() ? Axis.VERTICAL : Axis.HORIZONTAL;
		}
		return new ContainerRoom(main, parent, levelInfo, minX, minY, maxX, maxY, axis);
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
	public int compareTo(AbstractRoom other, Axis axis, AxisDirection axisDirection) {
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
	 * @param main      main instance
	 * @param levelInfo parameters describing level generation
	 *
	 * @return created room instance
	 */
	public static AbstractRoom createRoot(Main main, LevelInfo levelInfo) {
		return createRoom(main, null, levelInfo, 0, 0, levelInfo.width(), levelInfo.height());
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
