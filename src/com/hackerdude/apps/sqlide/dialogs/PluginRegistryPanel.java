package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;

import com.hackerdude.apps.sqlide.RunningPlugins;
import com.hackerdude.apps.sqlide.pluginapi.IDEPluginIF;
import com.hackerdude.apps.sqlide.plugins.definitions.PluginDefinition;
import com.hackerdude.apps.sqlide.plugins.definitions.PluginRegistry;

public class PluginRegistryPanel extends JPanel  {

	private ArrayList listSelectionListeners;
	private PluginDefinitionListModel selectedModel;

	public static final int LIST_TYPE_ALL_PLUGINS       = 0;
	public static final int LIST_TYPE_NODE_CONTEXT      = 1;
	public static final int LIST_TYPE_VISUAL_PLUGINS    = 2;
	public static final int LIST_TYPE_BROWSER_EXTENSION = 3;
	public static final int LIST_TYPE_NON_VISUAL        = 4;

	private ListCellRenderer pluginsCellRenderer = new PluginCellRenderer();

	private PluginDefinitionListModel allPluginInstancesListModel  = new PluginDefinitionListModel(LIST_TYPE_ALL_PLUGINS);
	private PluginDefinitionListModel nodeContextListModel         = new PluginDefinitionListModel(LIST_TYPE_NODE_CONTEXT);
	private PluginDefinitionListModel visualPluginsListModel       = new PluginDefinitionListModel(LIST_TYPE_VISUAL_PLUGINS);
	private PluginDefinitionListModel browserExtensionListModel    = new PluginDefinitionListModel(LIST_TYPE_BROWSER_EXTENSION);
	private PluginDefinitionListModel nonVisualListModel           = new PluginDefinitionListModel(LIST_TYPE_NON_VISUAL);

	public final ListModel[] PLUGIN_MODELS =  { allPluginInstancesListModel, nodeContextListModel, visualPluginsListModel, browserExtensionListModel, nonVisualListModel };

    BorderLayout borderLayout1 = new BorderLayout();
    private JPanel pnlMainPanel = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JScrollPane spPluginsRunning = new JScrollPane();
    private JPanel pnlHeader = new JPanel();
    private JLabel lblHeader = new JLabel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JList lstRunningPlugins = new JList();
    private JPanel jPanel1 = new JPanel();
    private JLabel lblPluginFilter = new JLabel();
    private JComboBox jComboBox1 = new JComboBox(PLUGIN_MODELS);

    public PluginRegistryPanel() {
        try {
            jbInit();
			lstRunningPlugins.setCellRenderer(pluginsCellRenderer);
			lstRunningPlugins.setModel(allPluginInstancesListModel);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        pnlMainPanel.setLayout(borderLayout2);
        lblHeader.setText("Plugins Installed");
        pnlHeader.setLayout(borderLayout3);
        lblPluginFilter.setText("Plugins: ");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jComboBox1_actionPerformed(e);
            }
        });
        this.add(pnlMainPanel, BorderLayout.CENTER);
        pnlMainPanel.add(spPluginsRunning, BorderLayout.CENTER);
        spPluginsRunning.getViewport().add(lstRunningPlugins, null);
        this.add(pnlHeader, BorderLayout.NORTH);
        pnlHeader.add(lblHeader, BorderLayout.CENTER);
        pnlMainPanel.add(jPanel1,  BorderLayout.SOUTH);
        jPanel1.add(lblPluginFilter, null);
        jPanel1.add(jComboBox1, null);
    }

	public void setRunningPlugins(RunningPlugins runningPlugins) {
//		for ( int i=0; i< PLUGIN_MODELS.length; i++ ) {
//			ListModel plugin = PLUGIN_MODELS[i];
//			if ( plugin instanceof PluginsListModel ) {
//			  ((PluginsListModel)plugin).setRunningPlugins(runningPlugins);
//			}
//		}
	}

	class PluginCellRenderer extends JLabel implements ListCellRenderer {
		public PluginCellRenderer() {
			setOpaque(true);
		}
		public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
		{
			IDEPluginIF plugin = null;

			if ( value instanceof PluginDefinition ) {
				plugin = ((PluginDefinition)value).pluginInstance;
			} else {
				plugin = (IDEPluginIF)value;
			}
			setText(plugin.getPluginName());
			setIcon(plugin.getPluginIcon());
			setBackground(isSelected ? Color.red : Color.white);
			setForeground(isSelected ? Color.white : Color.black);
			return this;
		}
	}

    synchronized void jComboBox1_actionPerformed(ActionEvent e) {
		selectedModel = (PluginDefinitionListModel)jComboBox1.getSelectedItem();
		lstRunningPlugins.setModel(selectedModel);
    }

	class PluginDefinitionListModel extends AbstractListModel {

		PluginDefinition[] pluginDefs;
		int listType;

		public PluginDefinitionListModel(int listType) {
			this.listType = listType;
			filterByType();
		}

		public int getSize() {

			return pluginDefs==null?0:pluginDefs.length;
		}

		public Object getElementAt(int row) {
			return pluginDefs[row];
		}

		public void filterByType() {
			switch ( listType ) {
				case LIST_TYPE_ALL_PLUGINS:
					pluginDefs = PluginRegistry.getInstance().getAllPluginDefinitions();
					break;
				case LIST_TYPE_NODE_CONTEXT:
					pluginDefs = PluginRegistry.getInstance().getAllNodeContextPluginDefinitions();
					break;
				case LIST_TYPE_VISUAL_PLUGINS:
					pluginDefs = PluginRegistry.getInstance().getAllVisualPluginDefinitions();
					break;
				case LIST_TYPE_BROWSER_EXTENSION:
					pluginDefs = PluginRegistry.getInstance().getAllBrowserExtensionPluginDefinitions();
					break;
				case LIST_TYPE_NON_VISUAL:
					pluginDefs = PluginRegistry.getInstance().getAllNonVisualPluginDefinitions();
					break;
			}
		}

		public String toString() {
			switch( listType) {
					case LIST_TYPE_ALL_PLUGINS: return "All";
					case LIST_TYPE_NODE_CONTEXT: return "Node Context";
					case LIST_TYPE_VISUAL_PLUGINS: return "Visual";
					case LIST_TYPE_BROWSER_EXTENSION: return "Browser Extension";
					case LIST_TYPE_NON_VISUAL: return "Non Visual";
			}
			return "unknown";

		}

	}

	public void addListSelectionListener(ListSelectionListener listener) {
		lstRunningPlugins.getSelectionModel().addListSelectionListener(listener);
	}

	public void removeListSelectionListener(ListSelectionListener listener) {
		lstRunningPlugins.getSelectionModel().removeListSelectionListener(listener);
	}

	public synchronized Object getElementAt(int index) {
		if ( lstRunningPlugins.getModel().getSize() > index ) return lstRunningPlugins.getModel().getElementAt(index);
		else return null;
	}

}