package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.hackerdude.apps.sqlide.RunningPlugins;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.plugins.definitions.PluginDefinition;
import com.hackerdude.swing.SwingUtils;

public class DlgPluginManager extends JDialog {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JSplitPane spPluginSplit = new JSplitPane();
    private PluginRegistryPanel runningPluginsPanel = new PluginRegistryPanel();
    private PluginDefinitionPanel pnlPluginDefinition = new PluginDefinitionPanel();

	private final ListSelectionListener LIST_SELECTION_LISTENER = new PluginRegistrySelectionListener();

	public DlgPluginManager(JFrame owner) {
		super(owner);
		try {
			jbInit();
			runningPluginsPanel.addListSelectionListener(LIST_SELECTION_LISTENER);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

    public DlgPluginManager() {
        try {
            jbInit();
			runningPluginsPanel.addListSelectionListener(LIST_SELECTION_LISTENER);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        spPluginSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.getContentPane().add(spPluginSplit, BorderLayout.NORTH);
        spPluginSplit.add(runningPluginsPanel, JSplitPane.TOP);
        spPluginSplit.add(pnlPluginDefinition, JSplitPane.BOTTOM);
    }


	public static void main(String[] args) {
		RunningPlugins plugins =  SqlIdeApplication.getInstance().getRunningPlugins();
		DlgPluginManager mgr = new DlgPluginManager();
		mgr.setTitle("Plugin Manager");
		mgr.runningPluginsPanel.setRunningPlugins(plugins);
		mgr.pack();
		mgr.show();
	}

	public static void showPluginManager(JFrame owner, String title) {
		RunningPlugins plugins =  SqlIdeApplication.getInstance().getRunningPlugins();
		DlgPluginManager mgr = new DlgPluginManager(owner);
		mgr.setTitle(title);
		mgr.setModal(true);
		mgr.runningPluginsPanel.setRunningPlugins(plugins);
		mgr.pack();
		mgr.setLocation(SwingUtils.getCenteredWindowPoint(mgr));
		mgr.show();
	}


	class PluginRegistrySelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent evt) {
			ListSelectionModel model = (ListSelectionModel)evt.getSource();
			int index = model.getLeadSelectionIndex();
			PluginDefinition definition = index == -1?null:(PluginDefinition)runningPluginsPanel.getElementAt(index);
			pnlPluginDefinition.setPluginDefinition(definition);

		}
	}
}

