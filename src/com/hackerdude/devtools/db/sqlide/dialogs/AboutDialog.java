package com.hackerdude.devtools.db.sqlide.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.hackerdude.devtools.db.sqlide.*;
/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class AboutDialog extends JDialog implements KeyListener {

	public final Action LICENSE_ACTION = sqlide.getInstance().HELP_LICENSE;

	LogoPanel logoPanel = new LogoPanel();

	JLabel label = new JLabel("JSQLIDE V "+ProgramConfig.VERSION_NUMBER);
	JPanel bottomPanel = new JPanel();
	JButton btn = new JButton("Ok", ProgramIcons.getInstance().getAppIcon());
	JButton btnLicense = new JButton(LICENSE_ACTION);

	public AboutDialog(JFrame owner) {
		super(owner);
		jbInit();
	}

    public AboutDialog() {
		super();
		jbInit();
	}

	private void jbInit() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(logoPanel, BorderLayout.CENTER);
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.add(btn);
		bottomPanel.add(btnLicense);
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		btn.setDefaultCapable(true);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				hide();
			}
		});

		setTitle("About SQLIDE...");
		addKeyListener(this);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) { logoPanel.stop(); }
            public void windowDeiconified(WindowEvent e) { logoPanel.start();}
            public void windowIconified(WindowEvent e) { logoPanel.stop();}
        };
        addWindowListener(l);

    }

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if ( e.getKeyCode() == e.VK_A && e.getModifiers() == e.ALT_MASK ) {
			if ( logoPanel.isRunning() ) logoPanel.stop(); else logoPanel.start();
		}
	}

	public void keyTyped(KeyEvent e) {}


	public static void main(String []args) {
		AboutDialog dlg = new AboutDialog();
		dlg.pack();
		dlg.setModal(true);
		dlg.show();
		System.exit(0);

	}

}
