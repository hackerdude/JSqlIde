package com.hackerdude.apps.sqlide.dataaccess;

import java.util.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;

/**
 * Implement this interface to provide the credentials
 */
public interface CredentialsProvider {

	public static final String KEY_USER_NAME = "_username";
	public static final String KEY_PASSWORD  = "_password";

	/**
	 * Implement this method in order to show your login box, or
	 * otherwise determine if the credentials will be available
	 * when getCredentials() is called.
	 *
	 * @return true if your code obtained the credentials successfully, false otherwise.
	 */
	public boolean areCredentialsAvailable(SqlideHostConfig configuration, CredentialsVerifier credentialsVerifier);


	/**
	 * Here you can return a map with the credentials you would like to supply
	 * to the database. Use KEY_USER_NAME and KEY_PASSWORD to submit the username
	 * and password. Anything else you submit will be passed in as properties
	 * of the connection.
	 *
	 * @return The map with the credentials.
	 */
	public Map getCredentials();

	/**
	 * This inner interface is implemented by all the Database processes, and
	 * it makes a test connection in order to verify that the credentials are
	 * right.
	 */
	public interface CredentialsVerifier {
		public boolean areCredentialsCorrect(Map credentials) throws Exception;
	}

}