package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Room which is not a leaf node on the tree. This room will contain two child rooms
 */
public final class ContainerRoom extends AbstractRoom {

	/**
	 * The axis this room is split on
	 */
	@NotNull
	public final Axis splitAxis;
	/**
	 * First child room
	 */
	@NotNull
	public final AbstractRoom child1;
	/**
	 * Second child room
	 */
	@NotNull
	public final AbstractRoom child2;
	/**
	 * Corridor connecting the child rooms
	 */
	@NotNull
	public final Corridor corridor;

	/**
	 * Create a container room
	 *
	 * @param main      main instance
	 * @param parent    parent room, or null if root node
	 * @param levelInfo parameters describing level generation
	 * @param minX      minimum x coordinate of room's bounds
	 * @param minY      minimum y coordinate of room's bounds
	 * @param maxX      maximum x coordinate of room's bounds
	 * @param maxY      maximum y coordinate of room's bounds
	 * @param splitAxis axis in which the room should split
	 */
	public ContainerRoom(
		@NotNull Main main,
		@Nullable ContainerRoom parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY,
		@NotNull Axis splitAxis
	) {
		super(main, parent, levelInfo, minX, minY, maxX, maxY);
		this.splitAxis = splitAxis;

		if (this.splitAxis == Axis.HORIZONTAL) {
			float splitLine = split(main.random, minY, maxY, levelInfo.minRoomHeight());

			this.child1 = AbstractRoom.createRoom(
				this.main,
				this,
				levelInfo,
				this.minX,
				this.minY,
				this.maxX,
				splitLine
			);
			this.child2 = AbstractRoom.createRoom(
				this.main,
				this,
				levelInfo,
				this.minX,
				splitLine,
				this.maxX,
				this.maxY
			);
		} else {
			float splitLine = split(main.random, minX, maxX, levelInfo.minRoomWidth());

			this.child1 = AbstractRoom.createRoom(
				this.main,
				this,
				levelInfo,
				this.minX,
				this.minY,
				splitLine,
				this.maxY
			);
			this.child2 = AbstractRoom.createRoom(
				this.main,
				this,
				levelInfo,
				splitLine,
				this.minY,
				this.maxX,
				this.maxY
			);
		}
		//Find the leaf nodes to connect in the two child rooms
		LeafRoom child1Leaf = this.child1.findRoom(this.splitAxis.other(), AxisDirection.MAX);
		LeafRoom child2Leaf = this.child2.findRoom(this.splitAxis.other(), AxisDirection.MIN);

		this.corridor = new Corridor(main, child1Leaf, child2Leaf, this.splitAxis, levelInfo.corridorWidth());
	}

	@Override
	public void draw() {
		this.child1.draw();
		this.child2.draw();
		this.corridor.draw();
	}

	@Override
	public LeafRoom findRoom(Axis axis, AxisDirection direction) {
		ArrayList<LeafRoom> children = new ArrayList<>();
		this.appendRooms(children);
		LeafRoom max = null;
		for (LeafRoom child : children) {
			if (max == null) {
				max = child;
			} else if (direction == AxisDirection.MAX && child.compareTo(max, axis, direction) > 0) {
				max = child;
			} else if (direction == AxisDirection.MIN && child.compareTo(max, axis, direction) < 0) {
				max = child;
			}
		}
		return max;
	}

	@Override
	public void appendRooms(Collection<LeafRoom> result) {
		this.child1.appendRooms(result);
		this.child2.appendRooms(result);
	}

	@Override
	public void appendWalls(Collection<HorizontalWall> horizontalWalls, Collection<VerticalWall> verticalWalls) {
		this.child1.appendWalls(horizontalWalls, verticalWalls);
		this.child2.appendWalls(horizontalWalls, verticalWalls);
	}

	/**
	 * Generate a random value between and min and max value.
	 * <p>
	 * This uses a gaussian distribution (see {@link Random#nextGaussian(double, double)}), where the mean is the
	 * midpoint of the numbers and the standard deviation is 1/8th of the range. This means the numbers will be heavily
	 * weighted towards the mean, but with possibility of more extreme values.
	 *
	 * @param min minimum value
	 * @param max maximum value
	 *
	 * @return random number in range
	 */
	private static float split(Random random, float min, float max, float smallest) {
		float midPoint = (min + max) / 2f;
		float difference = (max - min) - 2 * smallest;
		float stdDev = difference / 2f;
		double splitLine;
		do {
			splitLine = random.nextGaussian(midPoint, stdDev);
		} while (splitLine > max - smallest || splitLine < min + smallest);
		return (float) splitLine;
	}


}
