
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.lib.ui;

import javax.swing.JPanel;

public class WizardPage extends JPanel {

   protected Wizard wizard;

   public WizardPage() {
      super();
   }

   public WizardPage(Wizard parentWizard) {
      super();
      setWizard(parentWizard);
   }


   public void setWizard(Wizard wizard) { this.wizard = wizard; }
   public Wizard getWizard() { return wizard; }

   /**
    * Implement this function to let the calling wizard know if it's
    * ok to go to the next page.
    */
   public boolean nextPageOK() {return true;};

   /**
    * Implement this function to let the calling wizard know if it's
    * ok to go to the previous page.
    */
   public boolean prevPageOK() {return true;};

   /**
    * Override this method to do your own updating of the UI when
    * fields change their value.
    */
   public void updateControlState() {
   }

   public void toNextPage() throws VetoWizardPageChange {

   }

   public void toPreviousPage() throws VetoWizardPageChange  {

   }


}