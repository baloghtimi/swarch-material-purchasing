package materialpurchasing.server.login;

import java.util.HashMap;

import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

public class LoginServiceImpl {

	// UserID - pw
	HashMap<String, String> userID_PW = new HashMap<String, String>();
	// User ID - User
	HashMap<String, User> users = new HashMap<String, User>();

	public User SendLoginToServer(String userID, String password) {
		if (userID_PW.containsKey(userID) && userID_PW.get(userID).equals(password)) {
			return users.get(userID);
		}
		return null;
	}

	public Boolean SendRegistrationToServer(String userID, String password) {
		if (userID_PW.containsKey(userID)) {
			return false;
		} else {
			userID_PW.put(userID, password);
			users.put(userID, new User());
			return true;
		}
	}

	public Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut) {
		users.get(currentUser.getId()).setUserType(ut);
		return true;
	}
}
