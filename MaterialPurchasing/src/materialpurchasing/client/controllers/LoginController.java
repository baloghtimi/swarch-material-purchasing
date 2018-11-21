package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.List;

import materialpurchasing.client.events.LoginEvent;
import materialpurchasing.client.serverCommunication.LoginService;

public class LoginController {
	
	LoginService loginService=new LoginService();
	
	private String currentUser="";

	// Singelton Design Pattern
	private static LoginController instance = null;

	protected LoginController() {
		// Exists only to defeat instantiation.
	}

	public static LoginController getInstance() {
		if (instance == null) {
			instance = new LoginController();
		}
		return instance;
	}

	// Observer Design Pattern
	private List<LoginEvent> loginEventListeners = new ArrayList<>();

	public void addObserver(LoginEvent listener) {
		this.loginEventListeners.add(listener);
	}

	public void removeObserver(LoginEvent listener) {
		this.loginEventListeners.remove(listener);
	}

	public void sendLoginSuccessfulEvent(String currentUser) {
		this.currentUser=currentUser;
		for (LoginEvent listener : this.loginEventListeners) {
			listener.loginSuccessful();
		}
	}
	
	public void sendLoginFailedEvent() {
		for (LoginEvent listener : this.loginEventListeners) {
			listener.loginFailed();
		}
	}
	
	public void sendRegistrationSuccessfulEvent() {
		for (LoginEvent listener : this.loginEventListeners) {
			listener.registrationSuccessful();
		}
	}
	
	public void sendRegistrationFailedEvent() {
		for (LoginEvent listener : this.loginEventListeners) {
			listener.loginFailed();
		}
	}

	/*
	 * Returns a LoginEvent
	 * */
	public void register(String userID, String password) {
		loginService.SendRegistration(userID,password);
	}
	
	/*
	 * Returns a LoginEvent
	 * */
	public void login(String userID, String password) {
		loginService.SendLogin(userID,password);
	}
	
	public String getCurrentUser() {
		return this.currentUser;
	}
	
	public void logout() {
		this.currentUser="";
	}

}
