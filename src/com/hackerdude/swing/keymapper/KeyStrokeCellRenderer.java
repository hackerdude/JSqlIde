package com.hackerdude.swing.keymapper;
/*
=====================================================================

  KeyStrokeCellRenderer.java

  Created by Claude Duguay
  Copyright (c) 2000

=====================================================================
*/

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;

public class KeyStrokeCellRenderer
  extends DefaultTableCellRenderer
{
  public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected,
    boolean hasFocus, int row, int col)
  {
    String text = value.toString();
    if (value instanceof KeyStroke)
    {
      KeyStroke keyStroke = (KeyStroke)value;
      text = KeyStrokeField.formatKeyStroke(keyStroke);
    }
    return super.getTableCellRendererComponent(
      table, text, isSelected, false, row, col);
  }
}

