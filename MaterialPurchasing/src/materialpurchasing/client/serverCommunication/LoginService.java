package materialpurchasing.client.serverCommunication;

import java.util.HashMap;

import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

public class LoginService {
	
	//UserID - pw
	HashMap<String,String> userID_PW=new HashMap<String,String>();
	//User ID - User
	HashMap<String,User> users=new HashMap<String,User>();
	
	public LoginService(){
		SendRegistration("Default","default");
	}
	
	public void SendLogin(String userID, String password) {
		if(userID_PW.containsKey(userID)&&userID_PW.get(userID).equals(password)) {
			LoginController.getInstance().sendLoginSuccessfulEvent(users.get(userID));
		}else {
			LoginController.getInstance().sendLoginFailedEvent();
		}
	}
	
	public void SendRegistration(String userID, String password) {
		if(userID_PW.containsKey(userID)) {
			LoginController.getInstance().sendRegistrationFailedEvent();
		}else {
			userID_PW.put(userID, password);
			users.put(userID, new User());
			LoginController.getInstance().sendRegistrationSuccessfulEvent();
		}
	}

	public void SendUserStatusUpdate(User currentUser, UserType ut) {
		users.get(currentUser.getId()).setUserType(ut);
		
	}

}
