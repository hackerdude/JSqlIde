package com.hackerdude.apps.sqlide;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.hackerdude.apps.sqlide.dataaccess.CredentialsProvider;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * Credentials Provider that shows a login box to the user in order to 
 * obtain the credentials. Once these have been successfully obtained, these
 * credentials are provided to the caller. 
 * 
 * @author davidm <a href="mailto:david@hackerdude.com">david@hackerdude.com</a>
 */
public class InteractiveCredentialsProvider implements CredentialsProvider {

	LoginResponse currentLoginResponse;

    public InteractiveCredentialsProvider() {
    }

	/**
	 * Shows the Login Box.
	 * @return <code>true</code> if the login was successful.
	 */
	public LoginResponse showLoginBox(SqlideHostConfig hostConfiguration) {

		LoginResponse result = new LoginResponse();

		Object[]      message = new Object[5];
		result.userName = hostConfiguration.getJdbc().getUserName();
		JTextField fName = new JTextField(result.userName);
		JPasswordField fPassword = new JPasswordField();
		JLabel lUserName = new JLabel("User Name:");
		lUserName.setDisplayedMnemonic('N');
		lUserName.setLabelFor(fName);
		JLabel lPassword = new JLabel("Password");
		lPassword.setDisplayedMnemonic('P');
		lPassword.setLabelFor(fPassword);

		JOptionPane pane = new JOptionPane();
		pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		message[0] = hostConfiguration.getName();
		message[1] = lUserName;
		message[2] = fName;
		message[3] = lPassword;
		message[4] = fPassword;
		pane.setMessage(message);

		JFrame parentComponent = com.hackerdude.apps.sqlide.SqlIdeApplication.getFrame();
		JDialog dialog = pane.createDialog(parentComponent, "Database Login");
		dialog.show();

		int dialogResult = ((Integer)pane.getValue()).intValue();

		if ( dialogResult == JOptionPane.OK_OPTION ) {
			result.userName = fName.getText();
			result.password = new String(fPassword.getPassword());
			return result;
		}
		return null;

	}

    public boolean areCredentialsAvailable(SqlideHostConfig configuration, CredentialsVerifier verifier) {
		/** @todo Now that we have the beginnings of this, refactor! */
		currentLoginResponse = showLoginBox(configuration);
		if ( currentLoginResponse == null ) return false;
		try {
			if (verifier.areCredentialsCorrect(getCredentials())) {
				return true;
			}
		} catch ( Exception exc ) {
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), "Could not make test connection: "+exc.toString(), "Could not make test connection", JOptionPane.ERROR_MESSAGE);
		}
		return false;
    }

    public Map getCredentials() {
		HashMap map = new HashMap();
		map.put(KEY_USER_NAME, currentLoginResponse.userName);
		map.put(KEY_PASSWORD, currentLoginResponse.password);
		return map;
    }

	class LoginResponse {
		String userName;
		String password;
	}


}