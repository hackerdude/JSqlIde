/*
 *   Wizard.java - An basic Wizard UI for rapid wizard
 *   management.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Revision: $Revision$
 * Id      : $Id$
 *
*/
package com.hackerdude.lib.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is a basic wizard. It's basically a frame with the
 * outside pieces of a wizard. You create WizardPage objects
 * (basically panels with wizard communication features) and
 * add them to the wizard. Then the wizard will handle all the
 * next/previous buttons, etc.
 * <H3>How to Use this Wizard</H3>
 * <P>First you will need to create some Wizard Pages. See the
 * WizardPage document in this package on how to do this. After
 * this you can simply take the constru
 */
public class Wizard extends JDialog {

	public final Action ACTION_CANCEL = new CancelAction();
	public final Action ACTION_PREVIOUS = new PreviousPageAction();
	public final Action ACTION_NEXT     = new NextPageAction();
	public final Action ACTION_DONE     = new DoneAction();

   // Wizard Elements
   WizardPage[] pages;
   WizardPage currentPage;
   int currentPageNo;

   // UI Elements
   BorderLayout borderLayout1 = new BorderLayout();
   JPanel jPanel1 = new JPanel();
   BorderLayout borderLayout2 = new BorderLayout();
   JButton btnWizardIcon = new JButton();
   JTextArea taPageText = new JTextArea();
   JLabel lblWizardTitle = new JLabel();
   JPanel pnlBottomBar = new JPanel();
   BorderLayout borderLayout3 = new BorderLayout();
   JPanel pnlCurrentPageContainer = new JPanel();
   JPanel jPanel4 = new JPanel();
   JButton btnNext = new JButton(ACTION_NEXT);
   JButton btnPrev = new JButton(ACTION_PREVIOUS);
   JButton btnDone = new JButton(ACTION_DONE);
   BorderLayout borderLayout4 = new BorderLayout();
   JButton btnCancel = new JButton(ACTION_CANCEL);
   JPanel jPanel2 = new JPanel();

	public static int OK = 0;
	public static int CANCEL = 1;

	public int result = CANCEL;

   public Wizard() {
      try {
         jbInit();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Creates a new Wizard with the specified title and introductory text.
    */
   public Wizard(JFrame owner, String title, String introText, boolean modal) {
      this(owner, title, title, introText, modal);
   }

   /**
    * Create a new Wizard, with the specified window title, large font
    * title, and an introductory text.
    */
   public Wizard(JFrame owner, String windowTitle, String title, String introText, boolean modal) {
	super(owner);
	try {
		jbInit();
	}
	catch (Exception ex) {
		ex.printStackTrace();
	}
	this.setModal(modal);
	taPageText.setText(introText);
	lblWizardTitle.setText(title);
	setTitle(windowTitle);
	this.updateControlState();
   }

   public void setIntroText(String introText) {
	   taPageText.setText(introText);
   }

   public void setWizardTitle(String wizardTitle) {
	   lblWizardTitle.setText(wizardTitle);
   }


   /**
    * This method loads the pages into the wizard. Feed it
    * the array of pages you want to put in a wizard in the
    * proper order. It will start by setting the current
    * page to zero.
    * @param An array of pages.
    */
   public void setPages( WizardPage[] pages ) {
      this.pages = pages;
      if ( this.pages!= null ) {
         this.currentPageNo = 0;
         setCurrentPage(0);
      }
      updateControlState();
   }

   private void jbInit() throws Exception {
      this.getContentPane().setLayout(borderLayout1);
      jPanel1.setLayout(borderLayout2);
      taPageText.setLineWrap(true);
      taPageText.setWrapStyleWord(true);
      taPageText.setDisabledTextColor(SystemColor.controlText);
      taPageText.setBackground(getRootPane().getBackground());
      taPageText.setSelectionColor(SystemColor.control);
      taPageText.setEnabled(false);
      taPageText.setText("Welcome to this wizard dude. This is just an example of the kind " +
    "of stuff we can do to create this wizard and blah blah blah...");
      taPageText.setEditable(false);
      lblWizardTitle.setFont(new java.awt.Font("Dialog", 1, 18));
      lblWizardTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblWizardTitle.setText("Wizard Title, Dude");
      pnlBottomBar.setLayout(borderLayout3);
      btnNext.setFont(new java.awt.Font("Dialog", 1, 11));
      btnNext.setMaximumSize(new Dimension(109, 29));
      btnNext.setMinimumSize(new Dimension(109, 20));
      btnNext.setActionCommand("Next");
      btnNext.setMnemonic('N');
      btnNext.setText("Next >>");
      btnPrev.setFont(new java.awt.Font("Dialog", 1, 11));
      btnPrev.setMaximumSize(new Dimension(109, 29));
      btnPrev.setMinimumSize(new Dimension(109, 20));
      btnPrev.setActionCommand("Prev");
      btnPrev.setMnemonic('P');
      btnPrev.setText("<< Previous");
      btnDone.setFont(new java.awt.Font("Dialog", 1, 11));
      btnDone.setMaximumSize(new Dimension(109, 29));
      btnDone.setMinimumSize(new Dimension(109, 20));
      btnDone.setActionCommand("Done");
      btnDone.setMnemonic('D');
      btnDone.setText("I\'m Done!");
      jPanel4.setFont(new java.awt.Font("Dialog", 0, 9));
      pnlCurrentPageContainer.setLayout(borderLayout4);
      btnWizardIcon.setBorder(null);
      this.setTitle("Wizard Title, Dude");
      pnlCurrentPageContainer.setPreferredSize(new Dimension(450, 200));
      btnCancel.setFont(new java.awt.Font("Dialog", 1, 11));
      btnCancel.setMnemonic('C');
      btnCancel.setText("Cancel");
      this.getContentPane().add(jPanel1, BorderLayout.NORTH);
      jPanel1.add(btnWizardIcon, BorderLayout.WEST);
      jPanel1.add(taPageText, BorderLayout.CENTER);
      jPanel1.add(lblWizardTitle, BorderLayout.NORTH);
      this.getContentPane().add(pnlBottomBar, BorderLayout.SOUTH);
      pnlBottomBar.add(jPanel4, BorderLayout.EAST);
      jPanel4.add(btnPrev, null);
      jPanel4.add(btnNext, null);
      jPanel4.add(btnDone, null);
      pnlBottomBar.add(jPanel2, BorderLayout.WEST);
      jPanel2.add(btnCancel, null);
      this.getContentPane().add(pnlCurrentPageContainer, BorderLayout.CENTER);
   }

   public static void main(String[] args) {
      Wizard wiz = new Wizard(null, "Create a new Database Server",
          "This wizard will help you create a new Database Entry.\n"
          +"\nRayando el sol, desesperacion, es mas facil llegar al sol que a tu corazon", true
          );
      wiz.setVisible(true);
      wiz.setEnabled(true);
      wiz.pack();
      wiz.show();

   }

   /**
    * Override this method to make your own decisions
    * on whether DONE is possible or not.
    */
   public boolean isDonePossible() {
      return true;
   }

   /**
    * Set the title of the window that holds the wizard.
    */
   public void setWindowTitle(String windowTitle) { super.setTitle(windowTitle); }

   /**
    * This method updates the logic enabled/disabled state of
    * the buttons at the bottom.
    */
   public void updateControlState() {

      // Set some behavioral defaults
      boolean bNextExists   = false;
      boolean bPrevExists   = false;
      boolean bNextPossible = false;
      boolean bPrevPossible = false;
      boolean bDonePossible = true;

      // Make some decisions on what should be visible.
      if ( pages != null ) {
         bNextExists  = ( currentPageNo < pages.length-1 );
         bPrevExists  = ( currentPageNo > 0 );
         bNextPossible = ( currentPage.nextPageOK() );
         bPrevPossible = ( currentPage.prevPageOK() );
         bDonePossible = isDonePossible();
      }

      if ( currentPage != null ) currentPage.updateControlState();

      // Now go ahead and do the updating
      btnPrev.setVisible(bPrevExists);
      btnNext.setVisible(bNextExists);
      btnNext.setEnabled(bNextPossible);
      btnPrev.setEnabled(bPrevPossible);
      btnDone.setEnabled(bDonePossible);

   }

   /**
    * This method sets a new current page and shows it.
    */
   public void setCurrentPage(int newPage) {
      if ( pages == null ) return;
      if ( currentPage != null ) {
        pnlCurrentPageContainer.remove(currentPage);
        currentPage.setVisible(false);
      }
      currentPage   = pages[newPage];
      currentPageNo = newPage;
      if( currentPage != null ) {
         currentPage.setVisible(true);
         pnlCurrentPageContainer.add(currentPage);
      }
      pack();
      update(getGraphics());
      updateControlState();
   }


   private class DoneAction extends AbstractAction {
	   public void actionPerformed(ActionEvent evt) {
		   result = OK;
		   doneWizard();
	   }
   }


   private class NextPageAction extends AbstractAction {
	   public void actionPerformed(ActionEvent evt) {
		   try {
			   currentPage.toNextPage();
			   setCurrentPage(currentPageNo+1);
		   }
		   catch (VetoWizardPageChange ex) {

		   }
	   }
   }


   private class PreviousPageAction extends AbstractAction {
	   public void actionPerformed(ActionEvent evt) {
		   try {
			   currentPage.toPreviousPage();
			   setCurrentPage(currentPageNo-1);

		   }
		   catch (VetoWizardPageChange ex) {

		   }
	   }
   }

   /**
    * This method is called when the Done button is
    * pressed. You should override it to hide the dialog
    * and do whatever you need the wizard to do.
    */
   public void doneWizard() {
   }

   class CancelAction extends AbstractAction {
	   public void actionPerformed(ActionEvent e) {
		   result = CANCEL;
		   setVisible(false);
	   }
   }


}