package com.hackerdude.swing.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * An extension to JMenuItem that follows changes to accelerator keys.
 *
 * Use this instead of JMenuItem if you intend to change the accelerator keys
 * for an action attached to the JMEnuItem.
 *
 * @author David Martinez
 * @version 1.0
 */

public class JMenuItemWithSugar extends JMenuItem {
	public JMenuItemWithSugar() { super(); }
	public JMenuItemWithSugar(Action act) {
		this();
		setAction(act);

	}
	public JMenuItemWithSugar(Icon icon) { 	super(icon); }

	public JMenuItemWithSugar(String text) { super(text); }

	public JMenuItemWithSugar(String text, Icon icon) { super(text, icon); }
	public JMenuItemWithSugar(String text, int mnemonic) {
		super(text, mnemonic);

	}

	public void configurePropertiesFromAction(Action myAction) {
		super.configurePropertiesFromAction(myAction);
        if (myAction != null)  {
            KeyStroke keyStroke = (KeyStroke)myAction.getValue(Action.ACCELERATOR_KEY);
            if (keyStroke != null) setAccelerator(keyStroke);
        }

	}
    /**
	 * This is a copy of the JMenuItem's method witht the same name. The only
	 * thing that was added is a check for the accelerator key.
	 */
    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return
		new AbstractActionPropertyChangeListener(this, a){

			public void propertyChange(PropertyChangeEvent e) {
				String propertyName = e.getPropertyName();
				JMenuItem mi = (JMenuItem)getTarget();
				if (mi == null) {   //WeakRef GC'ed in 1.2
					Action action = (Action)e.getSource();
					action.removePropertyChangeListener(this);
				} else {
					if (e.getPropertyName().equals(Action.NAME)) {
					String text = (String) e.getNewValue();
					mi.setText(text);
					mi.repaint();
					} else if (propertyName.equals("enabled")) {
					Boolean enabledState = (Boolean) e.getNewValue();
					mi.setEnabled(enabledState.booleanValue());
					mi.repaint();
					} else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
					Icon icon = (Icon) e.getNewValue();
					mi.setIcon(icon);
					mi.invalidate();
					mi.repaint();
					} else if (e.getPropertyName().equals(Action.MNEMONIC_KEY)) {
					Integer mn = (Integer) e.getNewValue();
					mi.setMnemonic(mn.intValue());
					mi.invalidate();
					mi.repaint();
					} else if ( e.getPropertyName().equals(Action.ACCELERATOR_KEY)) {
						KeyStroke keyStroke = (KeyStroke)e.getNewValue();
						mi.setAccelerator(keyStroke);
						mi.invalidate();
						mi.repaint();
					}

				}
			}
		};
    }

}
	abstract class AbstractActionPropertyChangeListener implements PropertyChangeListener {
		private static ReferenceQueue queue;
		private WeakReference target;
		private Action action;

		AbstractActionPropertyChangeListener(JComponent c, Action a) {
		super();
		setTarget(c);
		this.action = a;
		}

		public void setTarget(JComponent c) {
			if (queue==null) {
			queue = new ReferenceQueue();
		}
		// Check to see whether any old buttons have
		// been enqueued for GC.  If so, look up their
		// PCL instance and remove it from its Action.
		OwnedWeakReference r;
		while ( (r = (OwnedWeakReference)queue.poll()) != null) {
			AbstractActionPropertyChangeListener oldPCL =
				(AbstractActionPropertyChangeListener)r.getOwner();
			Action oldAction = oldPCL.getAction();
			if (oldAction!=null) {
				oldAction.removePropertyChangeListener(oldPCL);
			}
		}
		this.target = new OwnedWeakReference(c, queue, this);
		}

		public JComponent getTarget() {
			return (JComponent)this.target.get();
		}

		public Action getAction() {
		  return action;
		}

		private static class OwnedWeakReference extends WeakReference {
			private Object owner;

			OwnedWeakReference(Object target, ReferenceQueue queue, Object owner) {
			super(target, queue);
			this.owner = owner;
		}

		public Object getOwner() {
			return owner;
		}
		}
	}
