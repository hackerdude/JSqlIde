package com.hackerdude.apps.sqlide.dialogs;

import java.awt.*;
import javax.swing.*;
import com.hackerdude.apps.sqlide.plugins.definitions.PluginDefinition;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.*;

public class PluginDefinitionPanel extends JPanel {

	private final PluginDefinition NULL_DEFINITION = getNullPluginDefinition();

    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel lblPluginDefinitionPanel = new JLabel();
    private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel pnlPluginName = new JPanel();
    private JLabel lblPluginName = new JLabel();
    private JPanel jPanel2 = new JPanel();
    private JLabel lblPluginClass = new JLabel();
	public PluginDefinition definition =  NULL_DEFINITION;

    public PluginDefinitionPanel() {
        try {
            jbInit();
			updateControls();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    void jbInit() throws Exception {
        lblPluginDefinitionPanel.setText("Plugin Definition Panel");
        this.setLayout(borderLayout1);
        jPanel1.setLayout(gridBagLayout1);
        lblPluginName.setText("Plugin Name: ");
        lblPluginClass.setText("Plugin Class:");
        this.add(lblPluginDefinitionPanel, BorderLayout.NORTH);
        this.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(pnlPluginName,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlPluginName.add(lblPluginName, null);
        jPanel1.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(lblPluginClass, null);
    }

	public void setPluginDefinition(PluginDefinition definition) {
		this.definition = definition;
		if ( definition == null ) this.definition = NULL_DEFINITION;
		updateControls();
	}

	public void updateControls() {
		lblPluginName.setText("Plugin Name: "+definition.pluginInstance.getPluginName());
		lblPluginName.setIcon(definition.pluginInstance.getPluginIcon());
		lblPluginClass.setText("Plugin Class: "+definition.pluginInstance.getClass().getName());
	}

	private PluginDefinition getNullPluginDefinition() {
		PluginDefinition result = new PluginDefinition();
		result.pluginInstance = new IDEPluginIF() {
			public String getPluginName() { return "No Plugin Selected"; }
			public String getPluginShortName() { return "N/A"; }
			public Icon getPluginIcon() {
				return ProgramIcons.getInstance().getServerIcon();
			}
			public String getPluginVersion() { return "N/A"; }
			public void freePlugin() {}
			public void initPlugin() {}
		};
		return result;
	}
}