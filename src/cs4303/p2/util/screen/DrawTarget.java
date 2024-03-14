package cs4303.p2.util.screen;

import cs4303.p2.util.builder.EllipseBuilder;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.TextBuilder;

/**
 * Something which supports builders to draw onto it
 */
public interface DrawTarget {

	/**
	 * Get the rectangle builder
	 *
	 * @return rectangle builder instance
	 */
	RectBuilder rect();

	/**
	 * Get the text builder
	 *
	 * @param text text to draw
	 *
	 * @return text builder instance
	 */
	TextBuilder text(String text);

	/**
	 * Get the line builder
	 *
	 * @return line builder instance
	 */
	LineBuilder line();

	/**
	 * Get the ellipse builder
	 *
	 * @return ellipse builder instance
	 */
	EllipseBuilder ellipse();

}
