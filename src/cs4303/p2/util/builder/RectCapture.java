package cs4303.p2.util.builder;

import cs4303.p2.util.collisions.Rectangle;

/**
 * Immutable capture of a rectangle
 *
 * @param minX   Min x coord
 * @param minY   Min y coord
 * @param width  Width of rectangle
 * @param height Height of rectangle
 */
public record RectCapture(float minX, float minY, float width, float height) implements Rectangle {


}
