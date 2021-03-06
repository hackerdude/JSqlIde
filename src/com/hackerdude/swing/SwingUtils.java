package com.hackerdude.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * A collection of static utility methods for swing.
 * @author David Martinez
 * @version 1.0
 */
public class SwingUtils {

    private SwingUtils() {
    }

	/**
	 * This method returns the point at which the incoming dialog will be
	 * perfectly centered on screen.
	 * @param windowToCenter The window to center. Make sure thesize has been set (pack or validate first).
	 * @return The point to set on the dialog using setLocation().
	 */
	public static Point getCenteredWindowPoint(java.awt.Window windowToCenter) {
		//Center the Dialog
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = windowToCenter.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		Point centerCoord = new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		return centerCoord;
	}


}