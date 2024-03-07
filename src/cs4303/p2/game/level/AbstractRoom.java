package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.Rectangle;

import java.util.Collection;
import java.util.Random;
import java.util.StringJoiner;

public sealed abstract class AbstractRoom implements Rectangle permits LeafRoom, ContainerRoom {

	@NotNull
	protected final Main main;
	@Nullable
	public final ContainerRoom parent;
	@NotNull
	public final LevelInfo levelInfo;
	public final float minX;
	public final float minY;
	public final float maxX;
	public final float maxY;

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

	public abstract void draw();

	public abstract LeafRoom findRoom(Axis axis, AxisDirection direction);

	public abstract void appendRooms(Collection<LeafRoom> result);

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

		boolean mustVerticalSplit = width >  levelInfo.maxRoomWidth();
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

	public static AbstractRoom createRoot(Main main, LevelInfo levelInfo) {
		return createRoom(main, null, levelInfo, 0, 0, levelInfo.width(), levelInfo.height());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ",
			this.getClass()
				.getSimpleName() + "[",
			"]"
		)
			.add("minX=" + minX)
			.add("minY=" + minY)
			.add("maxX=" + maxX)
			.add("maxY=" + maxY)
			.toString();
	}
}
