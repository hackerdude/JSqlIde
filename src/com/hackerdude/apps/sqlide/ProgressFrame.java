/*
 *  ProgressFrame.java - Progress dialog for the
 *  startup of sqlIDE.
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @author David Martinez <david@hackerdude.com>
 * @revision $Revision$
 * Id      : $Id$
*/
package com.hackerdude.apps.sqlide;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;

/**
 * This class is used for long processes (like the application startup). I
 * might make this into a splash screen w/long process :-)
 *
 * @version $Id$
 */
public class ProgressFrame extends JFrame {

	JPanel progressPanel;
	JLabel progressLabel = null;
	JProgressBar progressBar = null;

	public static ProgressFrame createProgressFrame(String title, int begin, int end ) {
	  URL url = ProgressFrame.class.getResource("/com/hackerdude/images/SQLIDE-Splash.jpg");
	  ImageIcon icon = new ImageIcon(url);
	  return new ProgressFrame(title, begin, end, icon);
	}

	public static ProgressFrame createProgressFrame(String title, int begin, int end, ImageIcon icon ) {
	  return new ProgressFrame(title, begin, end, icon);
	}

	/**
	 * ProgressFrame Constructor. Just give it a title, a beginning
	 * and an ending.
	 */
	private ProgressFrame( String title, int beginning, int ending, ImageIcon icon ) {
	  super(title);
	  progressPanel = new JPanel();
	  progressPanel.setLayout( new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
	  JButton jb = new JButton(icon ); // new ImageIcon(url) );
	  jb.setAlignmentX(CENTER_ALIGNMENT);
	  progressPanel.add( jb );

	  progressLabel = new JLabel("Loading, please wait...");
	  progressLabel.setAlignmentX(CENTER_ALIGNMENT);
	  Dimension d = new Dimension(400, 20);
	  progressLabel.setMaximumSize(d);
	  progressLabel.setPreferredSize(d);
	  progressPanel.add(progressLabel);
	  progressPanel.add(Box.createRigidArea(new Dimension(1,20)));

	  progressBar = new JProgressBar(beginning, ending);
	  progressBar.setStringPainted(true);
	  progressLabel.setLabelFor(progressBar);
	  progressBar.setAlignmentX(CENTER_ALIGNMENT);
	  progressBar.getAccessibleContext().setAccessibleName("sqlIDE Load Progress");
	  progressPanel.add(progressBar);
	  getContentPane().add(progressPanel, BorderLayout.CENTER);
	  pack();
	  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	  setLocation(screenSize.width/2 - this.getWidth()/2, screenSize.height/2 - this.getHeight()/2);
	  setVisible(true);
	}

	/**
	 * Change the value with this. You normally would be incrementing this
	 * value, but I guess you could decrement it (what do I care? :-)
	 */
	public void setValue( int newValue ) { progressBar.setValue( newValue ); }

	/**
	 * You should say what it is that is taking so much time by
	 * calling this guy every once in a while.
	 */
	public void changeMessage( String newMessage ) { progressLabel.setText(newMessage); }

}

