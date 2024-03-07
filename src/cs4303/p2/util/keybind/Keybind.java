package cs4303.p2.util.keybind;

import processing.event.Event;

/**
 * Abstract keybinding which may be for the keyboard or the mouse
 */
public interface Keybind {

	/**
	 * Test an event to see if this keybind accepts that event
	 *
	 * @param event event to test
	 *
	 * @return true if the event is the same button /key with the same modifiers as this binding, otherwise false
	 */
	boolean test(Event event);

}