package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.Properties;
import cs4303.p2.util.Either;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;

import java.util.Random;

public class Room {

	private final Main main;
	public final float xMin;
	public final float yMin;
	public final float xMax;
	public final float yMax;
	@Nullable
	public final Room parent;
	@NotNull
	private final Either<Split, LeafInfo> data;

	public Room(
		@NotNull Main main,
		@Nullable Room parent,
		float xMin,
		float yMin,
		float xMax,
		float yMax
	) {
		this.main = main;
		this.parent = parent;
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;

		Random random = main.random;
		Properties properties = main.properties;

		boolean verticalSplitAllowed = this.width() > 2 * properties.roomMinWidth();
		boolean horizontalSplitAllowed = this.height() > 2 * properties.roomMinHeight();

		if (!verticalSplitAllowed && !horizontalSplitAllowed) {
			float lower = properties.roomMarginMin;
			float upper = properties.roomMarginMax;
			LeafInfo info = new LeafInfo(
				random.nextInt(),
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper)
			);
			System.out.println(info);
			this.data = Either.ofB(info);
			return;
		}

		SplitAxis axis;
		if (!verticalSplitAllowed) {
			axis = SplitAxis.HORIZONTAL;
		} else if (!horizontalSplitAllowed) {
			axis = SplitAxis.VERTICAL;
		} else {
			axis = main.random.nextBoolean() ? SplitAxis.VERTICAL : SplitAxis.HORIZONTAL;
		}

		if (axis == SplitAxis.HORIZONTAL) {
			float splitLine = this.split(this.yMin, this.yMax, properties.roomMinHeight());

			Room child1 = new Room(this.main, this, this.xMin, this.yMin, this.xMax, splitLine);
			Room child2 = new Room(this.main, this, this.xMin, splitLine, this.xMax, this.yMax);
			this.data = Either.ofA(new Split(axis, child1, child2));
		} else {
			float splitLine = this.split(this.xMin, this.xMax, properties.roomMinWidth());

			Room child1 = new Room(this.main, this, this.xMin, this.yMin, splitLine, this.yMax);
			Room child2 = new Room(this.main, this, splitLine, this.yMin, this.xMax, this.yMax);
			this.data = Either.ofA(new Split(axis, child1, child2));
		}
	}

	public Room(Main main) {
		this(
			main,
			null,
			0,
			0,
			main.width,
			main.height
		);
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
	private float split(float min, float max, float smallest) {
		float midPoint = (min + max) / 2f;
		float difference = (max - min) - 2 * smallest;
		float stdDev = difference / 4f;
		double splitLine;
		do {
			splitLine = main.random.nextGaussian(midPoint, stdDev);
		} while (splitLine > max - smallest || splitLine < min + smallest);
		return (float) splitLine;
	}

	public float area() {
		return this.width() * this.height();
	}

	public float width() {
		return this.xMax - this.xMin;
	}

	public float height() {
		return this.yMax - this.yMin;
	}

	/**
	 * Boolean blindness go brr
	 */
	public enum SplitAxis {
		HORIZONTAL,
		VERTICAL;

		public SplitAxis other() {
			return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
		}
	}

	void draw() {
		if (this.data.hasB()) {
			LeafInfo info = this.data.b();
			main.noStroke();
			main.fill(info.color);
			main.rect(
				this.xMin + info.leftMargin,
				this.yMin + info.topMargin,
				this.width() - info.rightMargin - info.leftMargin,
				this.height() - info.bottomMargin - info.topMargin
			);
		} else {
			Split split = this.data.a();
			split.child1()
				.draw();
			split.child2()
				.draw();
		}
	}

	private record Split(SplitAxis axis, Room child1, Room child2) {

	}

	private record LeafInfo(int color, float leftMargin, float rightMargin, float topMargin,
							float bottomMargin) {

	}

}
