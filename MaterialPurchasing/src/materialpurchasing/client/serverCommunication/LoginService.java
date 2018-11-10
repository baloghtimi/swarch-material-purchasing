package materialpurchasing.client.serverCommunication;

import java.util.HashMap;

import materialpurchasing.client.controllers.LoginController;

public class LoginService {
	
	HashMap<String,String> users=new HashMap<String,String>();
	
	public LoginService(){
		users.put("User", "User");
	}
	
	public void SendLogin(String userID, String password) {
		if(users.containsKey(userID)&&users.get(userID).equals(password)) {
			LoginController.getInstance().sendLoginSuccessfulEvent(userID);
		}else {
			LoginController.getInstance().sendLoginFailedEvent();
		}
	}
	
	public void SendRegistration(String userID, String password) {
		if(users.containsKey(userID)) {
			LoginController.getInstance().sendRegistrationFailedEvent();
		}else {
			users.put(userID, password);
			LoginController.getInstance().sendRegistrationSuccessfulEvent();
		}
	}

}
