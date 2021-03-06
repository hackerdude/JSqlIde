package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class AboutDialog extends JDialog implements KeyListener {

	public final Action LICENSE_ACTION = SqlIdeApplication.getInstance().HELP_LICENSE;

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
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setDefaultCapable(true);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				hide();
			}
		});
		label.setForeground(Color.red);

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
		if ( e.getKeyCode() == KeyEvent.VK_A && e.getModifiers() == KeyEvent.ALT_MASK ) {
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
