package com.hackerdude.swing.keymapper;
/*
=====================================================================

  JKeyMapTest.java

  Created by Claude Duguay
  Copyright (c) 2000

=====================================================================
*/

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;

public class JKeyMapTest extends JPanel
{
  public JKeyMapTest()
  {
    JComponent component = new JTree();
    JKeyMap keyMap = new JKeyMap();
    keyMap.setModelInputMap(component.getInputMap());
    add(keyMap);
  }

  public static void main(String[] args)
  {
    PLAF.setNativeLookAndFeel(true);

    JFrame frame = new JFrame("JKeyMap Test");
    frame.getContentPane().add(new JKeyMapTest());
    frame.pack();
    frame.show();
  }
}

