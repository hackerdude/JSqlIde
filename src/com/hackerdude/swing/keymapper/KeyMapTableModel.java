package com.hackerdude.swing.keymapper;
/*
=====================================================================

  KeyMapTableModel.java

  Created by Claude Duguay
  Copyright (c) 2000

=====================================================================
*/

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class KeyMapTableModel extends DefaultTableModel
{
  public KeyMapTableModel()
  {
    addColumn("Action");
    addColumn("Key Assignment");
  }

  public void setModelInputMap(InputMap inputMap)
  {
    KeyStroke[] keys = inputMap.allKeys();
    SortedMap sortedMap = new TreeMap();
    for (int i = 0; i < keys.length; i++) {
		Object actionKey = inputMap.get(keys[i]);
		sortedMap.put(actionKey.toString(), keys[i]);
    }
    int count = getRowCount();
    for (int i = count - 1; i >= 0; i--)
    {
      removeRow(i);
    }
    Iterator iterator = sortedMap.entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      Object[] col = {entry.getKey(), entry.getValue()};
      addRow(col);
    }
  }

  public InputMap getModelInputMap()
  {
    InputMap inputMap = new InputMap();
    for (int i = 0; i < getRowCount(); i++)
    {
      Object stroke = getValueAt(i, 1);
      if (stroke instanceof KeyStroke)
      {
        inputMap.put((KeyStroke)stroke,
          (String)getValueAt(i, 0));
      }
    }
    return inputMap;
  }




}

