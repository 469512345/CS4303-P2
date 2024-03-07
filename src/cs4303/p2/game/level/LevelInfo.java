package cs4303.p2.game.level;

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
	float corridorWidth
) {
}
