
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

import java.awt.*;
import javax.swing.*;

/**
 * This class simply asks some basic questions with no
 * type checking or anything of the sort.
 */
public class BasicQuestionsWizardPage extends WizardPage {
   BorderLayout borderLayout1 = new BorderLayout();
   JLabel jLabel1 = new JLabel();
   JPanel jPanel1 = new JPanel();
   GridBagLayout gridBagLayout1 = new GridBagLayout();
   JPanel pnlQuestionAnswer = new JPanel();
   BorderLayout borderLayout2 = new BorderLayout();
   JLabel lblQuestion = new JLabel();
   JTextField jTextField1 = new JTextField();

   public BasicQuestionsWizardPage() {
      try {
         jbInit();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }



   public BasicQuestionsWizardPage(String[] questions) {
      this();
      for ( int i=0; i<questions.length; i++) {
        String currentQuestion = questions[i];
      }
   }

   public void jbInit() {
      jLabel1.setToolTipText("Please answer the following questions:");
      jLabel1.setText("jLabel1");
      this.setLayout(borderLayout1);
      jPanel1.setLayout(gridBagLayout1);
      pnlQuestionAnswer.setLayout(borderLayout2);
      lblQuestion.setText("jLabel2");
      jTextField1.setText("fldAnswer");
      this.add(jLabel1, BorderLayout.NORTH);
      this.add(jPanel1, BorderLayout.CENTER);
      jPanel1.add(pnlQuestionAnswer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 200, 0));
      pnlQuestionAnswer.add(lblQuestion, BorderLayout.WEST);
      pnlQuestionAnswer.add(jTextField1, BorderLayout.CENTER);
   }

   public boolean nextPageOK() {
      //TODO: implement this com.hackerdude.lib.ui.WizardPage abstract method
      return true;
   }

   public boolean prevPageOK() {
      //TODO: implement this com.hackerdude.lib.ui.WizardPage abstract method
      return true;
   }
}