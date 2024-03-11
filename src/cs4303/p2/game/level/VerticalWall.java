package cs4303.p2.game.level;

import cs4303.p2.util.collisions.VerticalLine;

/**
 * A vertical wall, parallel to the y-axis.
 *
 * @param x    x coordinate
 * @param minY minimum y coordinate
 * @param maxY maximum y coordinate
 */
public record VerticalWall(
	float x,
	float minY,
	float maxY
) implements VerticalLine {

}
