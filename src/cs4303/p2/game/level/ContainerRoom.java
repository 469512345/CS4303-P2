package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public final class ContainerRoom extends AbstractRoom {

	@NotNull
	public final Axis splitAxis;
	@NotNull
	public final AbstractRoom child1;
	@NotNull
	public final AbstractRoom child2;
	@NotNull
	public final Corridor corridor;

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
		LeafRoom child1Leaf = this.child1.findRoom(this.splitAxis.other(), AxisDirection.MAX);
		LeafRoom child2Leaf = this.child2.findRoom(this.splitAxis.other(), AxisDirection.MIN);

		this.corridor = new Corridor(main, child1Leaf, child2Leaf, this.splitAxis, levelInfo.corridorWidth());
	}

	@Override
	public void draw() {
		child1.draw();
		child2.draw();
		corridor.draw();
	}

	@Override
	public LeafRoom findRoom(Axis axis, AxisDirection direction) {
		System.out.println("FindRoom(axis = " + axis + ", direction = " + direction + ")");
		ArrayList<LeafRoom> children = new ArrayList<>();
		this.appendRooms(children);
		System.out.println(children.stream()
			.map(LeafRoom::toString)
			.collect(Collectors.joining("\n")));
		LeafRoom max = null;
		for (LeafRoom child : children) {
			if (max == null) {
				max = child;
				System.out.println("max = " + child);
			} else if (direction == AxisDirection.MAX && child.compareTo(max, axis, direction) > 0) {
				max = child;
				System.out.println("max = " + child);
			} else if (direction == AxisDirection.MIN && child.compareTo(max, axis, direction) < 0) {
				max = child;
				System.out.println("max = " + child);
			}
		}
		System.out.println("result = " + max);
		return max;
	}

	@Override
	public void appendRooms(Collection<LeafRoom> result) {
		this.child1.appendRooms(result);
		this.child2.appendRooms(result);
	}

	/**
	 * Generate a random value between and min and max value.
	 * <p>
	 * This uses a guassian distribution (see {@link Random#nextGaussian(double, double)}, where the mean is the
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
