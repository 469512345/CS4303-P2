package cs4303.p2.game.level;

import cs4303.p2.util.collisions.HorizontalLine;

/**
 * A horizontal wall, parallel to the y-axis.
 *
 * @param minX minimum x coordinate
 * @param maxX maximum x coordinate
 * @param y    y coordinate
 */
public record HorizontalWall(
	float minX,
	float maxX,
	float y
) implements HorizontalLine {

}
