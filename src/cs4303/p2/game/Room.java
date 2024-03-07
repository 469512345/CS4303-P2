package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.util.Either;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.Rectangle;

import java.awt.Color;
import java.util.Random;

public class Room implements Rectangle {

	private final Main main;
	public final float minX;
	public final float minY;
	public final float maxX;
	public final float maxY;
	@Nullable
	public final Room parent;
	@NotNull
	private final Either<Split, LeafInfo> data;

	public Room(
		@NotNull Main main,
		@Nullable Room parent,
		@NotNull LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		this.main = main;
		this.parent = parent;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

		Random random = main.random;

		boolean verticalSplitAllowed = this.width() > 2 * levelInfo.minRoomWidth();
		boolean horizontalSplitAllowed = this.height() > 2 * levelInfo.minRoomHeight();

		boolean mustVerticalSplit = this.width() > 2 * levelInfo.maxRoomWidth();
		boolean mustHorizontalSplit = this.height() > 2 * levelInfo.maxRoomHeight();

		boolean passRandomSplit = random.nextFloat(0, 1) < levelInfo.splitChance();

		SplitAxis axis;

		if (mustVerticalSplit && !mustHorizontalSplit) {
			axis = SplitAxis.VERTICAL;
		} else if (mustHorizontalSplit && !mustVerticalSplit) {
			axis = SplitAxis.HORIZONTAL;
		} else if (!(mustVerticalSplit || mustHorizontalSplit) && (!verticalSplitAllowed && !horizontalSplitAllowed || !passRandomSplit)) {
			float lower = levelInfo.minMargin();
			float upper = levelInfo.maxMargin();
			LeafInfo leafInto = new LeafInfo(
				this,
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper),
				random.nextFloat(lower, upper)
			);
			this.data = Either.ofB(leafInto);
			return;
		} else if (!verticalSplitAllowed) {
			axis = SplitAxis.HORIZONTAL;
		} else if (!horizontalSplitAllowed) {
			axis = SplitAxis.VERTICAL;
		} else {
			axis = main.random.nextBoolean() ? SplitAxis.VERTICAL : SplitAxis.HORIZONTAL;
		}

		if (axis == SplitAxis.HORIZONTAL) {
			float splitLine = this.split(this.minY, this.maxY, levelInfo.minRoomHeight());

			Room child1 = new Room(this.main, this, levelInfo, this.minX, this.minY, this.maxX, splitLine);
			Room child2 = new Room(this.main, this, levelInfo, this.minX, splitLine, this.maxX, this.maxY);
			this.data = Either.ofA(new Split(axis, child1, child2));
		} else {
			float splitLine = this.split(this.minX, this.maxX, levelInfo.minRoomWidth());

			Room child1 = new Room(this.main, this, levelInfo, this.minX, this.minY, splitLine, this.maxY);
			Room child2 = new Room(this.main, this, levelInfo, splitLine, this.minY, this.maxX, this.maxY);
			this.data = Either.ofA(new Split(axis, child1, child2));
		}
	}

	public Room(Main main, LevelInfo levelInfo) {
		this(
			main,
			null,
			levelInfo,
			0,
			0,
			levelInfo.width(),
			levelInfo.height()
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
		float stdDev = difference / 2f;
		double splitLine;
		do {
			splitLine = main.random.nextGaussian(midPoint, stdDev);
		} while (splitLine > max - smallest || splitLine < min + smallest);
		return (float) splitLine;
	}

	public float area() {
		return this.width() * this.height();
	}

	@Override
	public float minX() {
		return this.minX;
	}

	@Override
	public float minY() {
		return this.minY;
	}

	public float width() {
		return this.maxX - this.minX;
	}

	public float height() {
		return this.maxY - this.minY;
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
			main.rect().
				noStroke()
				.fill(Color.WHITE)
				.copy(info)
				.draw();
			main.ellipse()
				.at(info.centreX(), info.centreY())
				.radius(15)
				.fill(Color.BLUE)
				.draw();
		} else {
			Split split = this.data.a();
			Room child1 = split.child1;
			Room child2 = split.child2;
			child1.draw();
			child2.draw();

			float halfStrokeWeight = (float) Math.ceil(main.CORRIDOR_STROKE_WEIGHT / 2f);

			if (split.axis == SplitAxis.VERTICAL) {
				float centreX = (child1.roomMaxX() + child2.roomMinX()) / 2;
				float child1CentreY = child1.roomCentreY();
				float child2CentreY = child2.roomCentreY();

				main.line()
					.from(child1.roomMaxX(), child1CentreY)
					.to(centreX + halfStrokeWeight, child1CentreY)
					.stroke(Color.RED)
					.strokeWeight(main.CORRIDOR_STROKE_WEIGHT)
					.draw();

				main.line()
					.from(centreX, child1CentreY)
					.to(centreX, child2CentreY)
					.draw();

				main.line()
					.from(child2.roomMinX(), child2CentreY)
					.to(centreX - halfStrokeWeight, child2CentreY)
					.draw();

			} else {
				float centreY = (child1.roomMaxY() + child2.roomMinY()) / 2;
				float child1CentreX = child1.roomCentreX();
				float child2CentreX = child2.roomCentreX();

				main.line()
					.from(child1CentreX, child1.roomMaxY())
					.to(child1CentreX, centreY + halfStrokeWeight)
					.stroke(Color.RED)
					.strokeWeight(main.CORRIDOR_STROKE_WEIGHT)
					.draw();

				main.line()
					.from(child1CentreX, centreY)
					.to(child2CentreX, centreY)
					.draw();

				main.line()
					.from(child2CentreX, child2.roomMinY())
					.to(child2CentreX, centreY - halfStrokeWeight)
					.draw();
			}
		}
	}

	private record Split(
		SplitAxis axis,
		Room child1,
		Room child2
	) {

	}

	private record LeafInfo(
		Room room,
		float leftMargin,
		float rightMargin,
		float topMargin,
		float bottomMargin
	) implements Rectangle {


		@Override
		public float minX() {
			return this.room.minX + this.leftMargin;
		}

		@Override
		public float minY() {
			return this.room.minY + this.topMargin;
		}

		@Override
		public float width() {
			return room.width() - this.rightMargin - this.leftMargin;
		}

		@Override
		public float height() {
			return room.height() - this.bottomMargin - this.topMargin;
		}
	}

	public float roomMinX() {
		if (this.data.hasB()) {
			return this.data.b()
				.minX();
		} else {
			return this.minX();
		}
	}

	public float roomMaxX() {
		if (this.data.hasB()) {
			return this.data.b()
				.maxX();
		} else {
			return this.maxX();
		}
	}

	public float roomMinY() {
		if (this.data.hasB()) {
			return this.data.b()
				.minY();
		} else {
			return this.minY();
		}
	}

	public float roomMaxY() {
		if (this.data.hasB()) {
			return this.data.b()
				.maxY();
		} else {
			return this.maxY();
		}
	}

	public float roomCentreX() {
		if (this.data.hasB()) {
			return this.data.b()
				.centreX();
		} else {
			return this.centreX();
		}
	}

	public float roomCentreY() {
		if (this.data.hasB()) {
			return this.data.b()
				.centreY();
		} else {
			return this.centreY();
		}
	}
}
