package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.List;

import materialpurchasing.client.events.LoginEvent;
import materialpurchasing.client.events.LogoutEvent;
import materialpurchasing.client.serverCommunication.LoginService;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

public class LoginController {
	
	LoginService loginService=new LoginService();
	
	private User currentUser=new User();

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

	public void sendLoginSuccessfulEvent(User currentUser) {
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
	
	// Observer Design Pattern
		private List<LogoutEvent> logoutEventListeners = new ArrayList<>();

		public void addObserver(LogoutEvent listener) {
			this.logoutEventListeners.add(listener);
		}

		public void removeObserver(LogoutEvent listener) {
			this.logoutEventListeners.remove(listener);
		}
	
	public void sendLogOutEvent() {
		for (LogoutEvent listener : this.logoutEventListeners) {
			listener.logOut();
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
	
	/*
	 * Set UserStatus
	 * */
	public void setUserStatus(UserType ut) {
		this.currentUser.setUserType(ut);
		loginService.SendUserStatusUpdate(getCurrentUser(),ut);
	}
	
	
	public UserType getUserType() {
		return getCurrentUser().getUserType();
	}
	
	public String getUserId() {
		return getCurrentUser().getId();
	}
	
	private User getCurrentUser() {
		return this.currentUser;
	}
	
	public void logout() {
		this.sendLogOutEvent();
		this.currentUser=null;
	}

}
