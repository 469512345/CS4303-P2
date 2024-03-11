package cs4303.p2.util.keybind;

import processing.event.Event;
import processing.event.MouseEvent;

/**
 * A mouse keybinding
 */
public final class MouseKeybind implements Keybind {

	/**
	 * Button on mouse, according to {@link MouseEvent#getButton()}.
	 */
	private final int button;

	/**
	 * Modifiers on event, according to {@link MouseEvent#getModifiers()}.
	 */
	private final int modifiers;

	/**
	 * @param button    button of the event
	 * @param modifiers modifiers of the event
	 */
	public MouseKeybind(int button, int modifiers) {
		this.button = button;
		this.modifiers = modifiers;
	}

	/**
	 * Create a mouse keybinding to mirror an event
	 *
	 * @param event event to mirror
	 */
	public MouseKeybind(MouseEvent event) {
		this(event.getButton(), event.getModifiers());
	}

	@Override
	public boolean test(Event event) {
		if (event instanceof MouseEvent mouseEvent) {
			return mouseEvent.getButton() == this.button && mouseEvent.getModifiers() == this.modifiers;
		}
		return false;
	}

}
