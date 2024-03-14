package cs4303.p2.game.level;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.entity.family.Family;
import cs4303.p2.game.entity.robot.Robot;
import processing.core.PVector;

/**
 * Simple record containing parameters for level generation
 *
 * @param width                   width of level
 * @param height                  height of level
 * @param minRoomWidth            minimum width of a room
 * @param minRoomHeight           minimum height of a room
 * @param maxRoomWidth            maximum width of a room
 * @param maxRoomHeight           maximum height of a room
 * @param minMargin               minimum margin around a room
 * @param maxMargin               maximum margin around a room
 * @param splitChance             chance of a room to split if able to (A room will always split if greater than
 *                                {@link #maxRoomWidth} or {@link #maxRoomHeight}.)
 * @param corridorWidth           width of corridors
 * @param minObstacleRadius       number of obstacles in each room
 * @param maxObstacleRadius       number of obstacles in each corridor
 * @param minObstaclesPerRoom     minimum number of obstacles in each room
 * @param maxObstaclesPerRoom     maximum number of obstacles in each room
 * @param minObstaclesPerCorridor minimum number of obstacles in each corridor segment
 * @param maxObstaclesPerCorridor maximum number of obstacles in each corridor segment
 * @param minHumansPerRoom        minimum number of humans per room
 * @param maxHumansPerRoom        maximum number of humans per room
 * @param humanConstructors       constructors for humans available in this level
 * @param minRobotsPerRoom        minimum number of robots per room
 * @param maxRobotsPerRoom        maximum number of robots per room
 * @param robotConstructors       constructors for robots available in this level
 * @param roomRobotChance         chance that a room generates with robots, otherwise it will generate with humans
 */
public record LevelInfo(
	float width,
	float height,
	float minRoomWidth,
	float minRoomHeight,
	float maxRoomWidth,
	float maxRoomHeight,
	float minMargin,
	float maxMargin,
	float splitChance,
	float corridorWidth,
	float minObstacleRadius,
	float maxObstacleRadius,
	int minObstaclesPerRoom,
	int maxObstaclesPerRoom,
	int minObstaclesPerCorridor,
	int maxObstaclesPerCorridor,
	int minHumansPerRoom,
	int maxHumansPerRoom,
	HumanConstructor[] humanConstructors,
	int minRobotsPerRoom,
	int maxRobotsPerRoom,
	RobotConstructor[] robotConstructors,
	float roomRobotChance
) {

	/**
	 * Functional interface for representing a constructor for an object in the world
	 *
	 * @param <T> type of object
	 */
	public interface ObjectConstructor<T> {

		/**
		 * Create the entity
		 *
		 * @param game     game instance
		 * @param position position
		 *
		 * @return newly constructed object instance
		 */
		T construct(GameScreen game, PVector position);

	}

	/**
	 * Constructor for humans.
	 * Making this its own type makes creating arrays much easier; it is not easy to create an array of a generic type
	 */
	public interface HumanConstructor extends ObjectConstructor<Family> {

	}

	/**
	 * Constructor for robots.
	 * Making this its own type makes creating arrays much easier; it is not easy to create an array of a generic type
	 */
	public interface RobotConstructor extends ObjectConstructor<Robot> {
	}

}
