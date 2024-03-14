package cs4303.p2.game.level;

/**
 * Simple record containing parameters for level generation
 *
 * @param width                width of level
 * @param height               height of level
 * @param minRoomWidth         minimum width of a room
 * @param minRoomHeight        minimum height of a room
 * @param maxRoomWidth         maximum width of a room
 * @param maxRoomHeight        maximum height of a room
 * @param minMargin            minimum margin around a room
 * @param maxMargin            maximum margin around a room
 * @param splitChance          chance of a room to split if able to (A room will always split if greater than
 *                             {@link #maxRoomWidth} or {@link #maxRoomHeight}.)
 * @param corridorWidth        width of corridors
 * @param obstaclesPerRoom     number of obstacles in each room
 * @param obstaclesPerCorridor number of obstacles in each corridor
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
	int maxObstaclesPerCorridor
) {
}
