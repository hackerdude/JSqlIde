package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hackerdude.apps.sqlide.xml.hostconfig.ClassPath;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * This panel allows the user to edit a class path.
 */
public class ConnectionClassPathPanel extends JPanel {

	public final Action ACTION_ADD = new AddAction();
	public final Action ACTION_REMOVE = new RemoveAction();

	private SqlideHostConfig databaseSpec;
	ClassPathListModel listModel = new ClassPathListModel();

	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlBottom = new JPanel();
	private JPanel pnlTop = new JPanel();
	private JButton btnRemove = new JButton(ACTION_REMOVE);
	private JButton btnAdd = new JButton(ACTION_ADD);
	private JScrollPane fileScroller = new JScrollPane();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JList lstFileList = new JList();
	private JLabel lblInstruction = new JLabel();
	private BorderLayout borderLayout3 = new BorderLayout();

	public ConnectionClassPathPanel() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		btnRemove.setText("Remove");
		btnAdd.setText("Add");
		pnlCenter.setLayout(borderLayout2);
		lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstruction.setText("Specify JAR Files for Classpath");
		pnlTop.setLayout(borderLayout3);
		this.add(pnlCenter, BorderLayout.CENTER);
		this.add(pnlBottom,  BorderLayout.SOUTH);
		this.add(pnlTop, BorderLayout.NORTH);
		pnlBottom.add(btnAdd, null);
		pnlBottom.add(btnRemove, null);
		pnlCenter.add(fileScroller,  BorderLayout.CENTER);
		fileScroller.getViewport().add(lstFileList, null);
		pnlTop.add(lblInstruction, BorderLayout.CENTER);
	}
	public void setDatabaseSpec(SqlideHostConfig databaseSpec) {
		this.databaseSpec = databaseSpec;
		readFromModel();
	}


	public SqlideHostConfig getDatabaseSpec() {
		return databaseSpec;
	}

	public void readFromModel() {

		String[] jarFiles = databaseSpec.getJdbc().getClassPath()==null?null:databaseSpec.getJdbc().getClassPath().getPathelement();
		listModel.clear();
		if ( (jarFiles != null) && jarFiles.length>0 ) {
			for ( int i=0; i<jarFiles.length; i++ ) listModel.addElement(jarFiles[i]);
		}
		lstFileList.setModel(listModel);
	}

	public void applyToModel() {
		ArrayList newJarFiles = listModel.getList();
		synchronized ( newJarFiles ) {
			String[] jarFiles = new String[newJarFiles.size()];
			jarFiles = (String[])newJarFiles.toArray(jarFiles);
			if ( databaseSpec.getJdbc().getClassPath() != null ) {
				databaseSpec.getJdbc().setClassPath(new ClassPath());
			}
			databaseSpec.getJdbc().getClassPath().setPathelement(jarFiles);
		}

	}

	private class ClassPathListModel extends AbstractListModel {

		ArrayList classpathList = new ArrayList();

		public int getSize() { return classpathList.size(); }

		public Object getElementAt(int row) {  return classpathList.get(row); }

		public void addElement(Object element) {
			classpathList.add(element);
			fireIntervalAdded(this,classpathList.size()-1, classpathList.size()-1);
		}

		public void clear() { classpathList.clear(); }

		public ArrayList getList() { return classpathList; }

		public void removeElement(int row) {
			classpathList.remove(row);
			fireIntervalAdded(this, row,row);
		}

	}

	class AddAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			String[] newJarFiles = selectFileNames(".jar", "Java Archive File", true);
			if ( (newJarFiles!=null) && ( newJarFiles.length > 0 ) ) {
				for ( int i=0; i<newJarFiles.length; i++ ) {
					listModel.addElement(newJarFiles[i]);
				}
			}
		}
	}

	class RemoveAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			listModel.removeElement(lstFileList.getSelectedIndex());
		}
	}

	public String[] selectFileNames( String extension, String description, boolean allowDirs ) {

		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		fc.setFileFilter( new myFileFilter( description, extension, allowDirs ) );
		int returnVal = fc.showDialog(this, "Add Jars");
		if (returnVal == JFileChooser.APPROVE_OPTION ) {
				File[] files = fc.getSelectedFiles();
				String[] result = new String[files.length];
				for ( int i=0; i<files.length; i++) {
					try {
						result[i] = files[i].getCanonicalPath();
					} catch( IOException io ) {
						result[i] = files[i].getAbsolutePath();
					}
				}
				return result;
		}
		return null;
	}

	private class myFileFilter extends javax.swing.filechooser.FileFilter {
		String desc;
		String ext;
		boolean allowdir;
		public myFileFilter( String description, String extension, boolean dirs ) {
			desc = description;
			ext = extension;
			allowdir = dirs;
		}

		public String getDescription() { return(desc); };

		public boolean accept( File f ) {
				boolean bAccept = false;
				bAccept = f.toString().endsWith(ext);
				if ( allowdir ) bAccept = bAccept | f.isDirectory();
				return( bAccept );
		}
	}

}
