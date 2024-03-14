package cs4303.p2.util.screen;

import cs4303.p2.util.builder.EllipseBuilder;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.TextBuilder;

/**
 * A draw target, which defers the getting of its builders to another class
 */
public interface DeferredDrawTarget extends DrawTarget {

	DrawTarget deferRenderingTo();

	@Override
	default RectBuilder rect() {
		return this.deferRenderingTo()
			.rect();
	}

	@Override
	default TextBuilder text(String text) {
		return this.deferRenderingTo()
			.text(text);
	}

	@Override
	default LineBuilder line() {
		return this.deferRenderingTo()
			.line();
	}

	@Override
	default EllipseBuilder ellipse() {
		return this.deferRenderingTo()
			.ellipse();
	}
}
