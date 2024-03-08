package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.RectCapture;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Corridor {

	private final Main main;
	private final LeafRoom room1;
	private final LeafRoom room2;
	private final Axis axis;
	private final float width;
	private final ArrayList<PVector> points;
	private final ArrayList<Rectangle> segments;

	public Corridor(Main main, LeafRoom room1, LeafRoom room2, Axis axis, float width) {
		this.main = main;
		this.room1 = room1;
		this.room2 = room2;
		this.axis = axis;
		this.width = width;
		this.points = new ArrayList<>();
		this.segments = new ArrayList<>();

		float halfWidth = this.width / 2f;

		Random random = main.random;

		if (this.axis == Axis.VERTICAL) {
			Float corridorY = null;

			boolean room1Above = room1.minY < room2.minY;
			boolean room1Below = room1.maxY > room2.maxY;

			if (room1Above) {
				if (room1Below) {
					corridorY = random(random, room2.minY + halfWidth, room2.maxY - halfWidth);
				} else if (room1.maxY - room2.minY > width) { // Overlap with enough room for a corridor
					corridorY = random(random, room2.minY + halfWidth, room1.maxY - halfWidth);
				}
			} else {
				if (room1Below) {
					if (room2.maxY - room1.minY > width) {
						corridorY = random(random, room1.minY + halfWidth, room2.maxY - halfWidth);
					}
				} else {
					corridorY = random(random, room1.minY + halfWidth, room1.maxY - halfWidth);
				}
			}

			if (corridorY != null) {
				this.points.add(new PVector(room1.maxX, corridorY));
				this.points.add(new PVector(room2.minX, corridorY));
			} else {
				float centreX = (room1.maxX + room2.minX) / 2;
				float room1CorridorY = random(random, room1.minY + halfWidth, room1.maxY - halfWidth);
				float room2CorridorY = random(random, room2.minY + halfWidth, room2.maxY - halfWidth);

				this.points.add(new PVector(room1.maxX, room1CorridorY));
				this.points.add(new PVector(centreX, room1CorridorY));
				this.points.add(new PVector(centreX, room2CorridorY));
				this.points.add(new PVector(room2.minX, room2CorridorY));
			}
		} else {
			Float corridorX = null;

			boolean room1Left = room1.minX < room2.minX;
			boolean room1Right = room1.maxX > room2.maxX;

			if (room1Left) {
				if (room1Right) {
					corridorX = random(random, room2.minX + halfWidth, room2.maxX - halfWidth);
				} else if (room1.maxX - room2.minX > width) { // Overlap with enough room for a corridor
					corridorX = random(random, room2.minX + halfWidth, room1.maxX - halfWidth);
				}
			} else {
				if (room1Right) {
					if (room2.maxX - room1.minX > width) {
						corridorX = random(random, room1.minX + halfWidth, room2.maxX - halfWidth);
					}
				} else {
					corridorX = random(random, room1.minX + halfWidth, room1.maxX - halfWidth);
				}
			}

			if (corridorX != null) {
				this.points.add(new PVector(corridorX, room1.maxY));
				this.points.add(new PVector(corridorX, room2.minY));
			} else {
				float centreY = (room1.maxY + room2.minY) / 2;
				float room1CorridorX = random(random, room1.minX + halfWidth, room1.maxX - halfWidth);
				float room2CorridorX = random(random, room2.minX + halfWidth, room2.maxX - halfWidth);

				this.points.add(new PVector(room1CorridorX, room1.maxY));
				this.points.add(new PVector(room1CorridorX, centreY));
				this.points.add(new PVector(room2CorridorX, centreY));
				this.points.add(new PVector(room2CorridorX, room2.minY));
			}
		}
		for (int i = 1; i < this.points.size(); i++) {
			PVector point1 = this.points.get(i - 1);
			PVector point2 = this.points.get(i);

			this.segments.add(lineToRect(point1, point2, halfWidth));
		}
	}

	void draw() {
		RectBuilder rect = main.rect()
			.fill(Color.WHITE);

		for (Rectangle segment : this.segments) {
			rect.copy(segment)
				.draw();
		}
	}

	private static float random(Random random, float min, float max) {
		float mean = (min + max) / 2f;
		float stdDev = (max - min) / 4f;

		float result;
		do {
			result = (float) random.nextGaussian(mean, stdDev);
		} while (result > max || result < min);
		return result;
	}

	private static Rectangle lineToRect(PVector p1, PVector p2, float margin) {
		float minX = Float.min(p1.x, p2.x);
		float minY = Float.min(p1.y, p2.y);

		float maxX = Float.max(p1.x, p2.x);
		float maxY = Float.max(p1.y, p2.y);

		float width = maxX - minX + 2 * margin;
		float height = maxY - minY + 2 * margin;

		return new RectCapture(minX - margin, minY - margin, width, height);
	}
}
