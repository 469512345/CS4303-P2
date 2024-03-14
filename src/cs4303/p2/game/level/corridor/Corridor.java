package cs4303.p2.game.level.corridor;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.level.Axis;
import cs4303.p2.game.level.room.LeafRoom;
import cs4303.p2.util.collisions.HorizontalLine;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.collisions.VerticalLine;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * A corridor between two rooms. This will aim to be straight if possible, but will otherwise consist of 3 segments.
 */
public sealed abstract class Corridor permits CompositeCorridor, StraightCorridor {

	/**
	 * Main instance
	 */
	protected final GameScreen game;
	/**
	 * First room
	 */
	public final LeafRoom room1;
	/**
	 * Second room
	 */
	public final LeafRoom room2;
	/**
	 * Axis perpendicular to the corridor - this is the axis of split of the larger composite room
	 */
	protected final Axis axis;
	/**
	 * Width of the corridor
	 */
	protected final float width;
	/**
	 * Half the width - margin around the actual points of the corridor
	 */
	protected final float margin;
	/**
	 * Nodes of the corridor. If the corridor is straight then this will only be the start and end point, but if the
	 * corridor has bends there will be point at each 90-degree bend.
	 */
	public final ArrayList<PVector> points;
	/**
	 * Rectangle segments that make up this corridor. These are calculated from the points upon construction to avoid
	 * recomputing each frame
	 */
	public final ArrayList<Rectangle> segments;

	/**
	 * Create a new corridor
	 *
	 * @param game  main instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis of the corridor
	 * @param width half-width of the corridor
	 */
	public Corridor(GameScreen game, LeafRoom room1, LeafRoom room2, Axis axis, float width) {
		this.game = game;
		this.room1 = room1;
		this.room2 = room2;
		this.axis = axis;
		this.width = width;
		this.margin = width / 2f;
		this.points = new ArrayList<>();
		this.segments = new ArrayList<>();

		this.room1.corridors.add(this);
		this.room2.corridors.add(this);
	}

	/**
	 * Draw to screen
	 */
	public abstract void draw();

	/**
	 * Calculate the walls of this corridor and append them to the list of walls
	 *
	 * @param horizontalWalls collection of horizontal walls to append to
	 * @param verticalWalls   collection of vertical walls to append to
	 */
	public abstract void appendWalls(
		Collection<HorizontalLine> horizontalWalls,
		Collection<VerticalLine> verticalWalls
	);

	/**
	 * Get a random value based on a gaussian distribution
	 *
	 * @param random random instance
	 * @param min    min value
	 * @param max    max value
	 *
	 * @return random value in range
	 */
	protected static float random(Random random, float min, float max) {
		float mean = (min + max) / 2f;
		float stdDev = (max - min) / 4f;

		float result;
		do {
			result = (float) random.nextGaussian(mean, stdDev);
		} while (result > max || result < min);
		return result;
	}

	/**
	 * Convert a line, with a square margin in all directions, into a rectangle segment
	 *
	 * @param p1     point 1
	 * @param p2     point 2
	 * @param margin margin around the line in all directions (square-cap, not rounded)
	 *
	 * @return rectangle segment
	 */
	protected static Rectangle lineToRect(PVector p1, PVector p2, float margin) {
		float minX = Float.min(p1.x, p2.x);
		float minY = Float.min(p1.y, p2.y);

		float maxX = Float.max(p1.x, p2.x);
		float maxY = Float.max(p1.y, p2.y);

		float width = maxX - minX + 2 * margin;
		float height = maxY - minY + 2 * margin;

		return Rectangle.of(minX - margin, minY - margin, width, height);
	}

	/**
	 * Create a corridor. This method will look at the two rooms and determine whether the corridor should be straight
	 * or composite.
	 *
	 * @param game  game instance
	 * @param room1 first room
	 * @param room2 second room
	 * @param axis  axis perpendicular to corridor
	 * @param width width of corridor
	 *
	 * @return corridor instance
	 */
	public static Corridor createCorridor(
		GameScreen game,
		LeafRoom room1,
		LeafRoom room2,
		Axis axis,
		float width
	) {
		float margin = width / 2f;

		Random random = game.main.random;

		if (axis == Axis.VERTICAL) {
			Float corridorY = null;

			boolean room1Above = room1.minY < room2.minY;
			boolean room1Below = room1.maxY > room2.maxY;

			if (room1Above) {
				if (room1Below) {
					corridorY = random(random, room2.minY + margin, room2.maxY - margin);
				} else if (room1.maxY - room2.minY > width) { // Overlap with enough room for a corridor
					corridorY = random(random, room2.minY + margin, room1.maxY - margin);
				}
			} else {
				if (room1Below) {
					if (room2.maxY - room1.minY > width) {
						corridorY = random(random, room1.minY + margin, room2.maxY - margin);
					}
				} else {
					corridorY = random(random, room1.minY + margin, room1.maxY - margin);
				}
			}

			if (corridorY != null) {
				PVector point1 = new PVector(room1.maxX, corridorY);
				PVector point2 = new PVector(room2.minX, corridorY);
				return new StraightCorridor(game, room1, room2, axis, width, point1, point2);
			} else {
				float centreX = (room1.maxX + room2.minX) / 2;
				float room1CorridorY = random(random, room1.minY + margin, room1.maxY - margin);
				float room2CorridorY = random(random, room2.minY + margin, room2.maxY - margin);

				PVector point1 = new PVector(room1.maxX, room1CorridorY);
				PVector point2 = new PVector(centreX, room1CorridorY);
				PVector point3 = new PVector(centreX, room2CorridorY);
				PVector point4 = new PVector(room2.minX, room2CorridorY);

				return new CompositeCorridor(game, room1, room2, axis, width, point1, point2, point3, point4);
			}
		} else {
			Float corridorX = null;

			boolean room1Left = room1.minX < room2.minX;
			boolean room1Right = room1.maxX > room2.maxX;

			if (room1Left) {
				if (room1Right) {
					corridorX = random(random, room2.minX + margin, room2.maxX - margin);
				} else if (room1.maxX - room2.minX > width) { // Overlap with enough room for a corridor
					corridorX = random(random, room2.minX + margin, room1.maxX - margin);
				}
			} else {
				if (room1Right) {
					if (room2.maxX - room1.minX > width) {
						corridorX = random(random, room1.minX + margin, room2.maxX - margin);
					}
				} else {
					corridorX = random(random, room1.minX + margin, room1.maxX - margin);
				}
			}

			if (corridorX != null) {
				PVector point1 = new PVector(corridorX, room1.maxY);
				PVector point2 = new PVector(corridorX, room2.minY);

				return new StraightCorridor(game, room1, room2, axis, width, point1, point2);
			} else {
				float centreY = (room1.maxY + room2.minY) / 2;
				float room1CorridorX = random(random, room1.minX + margin, room1.maxX - margin);
				float room2CorridorX = random(random, room2.minX + margin, room2.maxX - margin);

				PVector point1 = new PVector(room1CorridorX, room1.maxY);
				PVector point2 = new PVector(room1CorridorX, centreY);
				PVector point3 = new PVector(room2CorridorX, centreY);
				PVector point4 = new PVector(room2CorridorX, room2.minY);

				return new CompositeCorridor(game, room1, room2, axis, width, point1, point2, point3, point4);
			}

		}
	}
}
