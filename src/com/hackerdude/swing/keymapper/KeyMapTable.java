package com.hackerdude.swing.keymapper;
/*
=====================================================================

  KeyMapTable.java

  Created by Claude Duguay
  Copyright (c) 2000

=====================================================================
*/

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class KeyMapTable extends JTable
{
  public KeyMapTable(TableModel model)
  {
    super(model);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setDefaultEditor(Object.class, null);
    setDefaultRenderer(Object.class, new KeyStrokeCellRenderer());
  }
}

