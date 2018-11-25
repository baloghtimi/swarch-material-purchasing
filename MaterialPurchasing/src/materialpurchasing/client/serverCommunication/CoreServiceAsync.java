package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

/**
 * The async counterpart of <code>CoreService</code>.
 */
public interface CoreServiceAsync {
	//void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void SendLoginToServer(String userID, String password, AsyncCallback<User> callback);

	void SendRegistrationToServer(String userID, String password, UserType userType, AsyncCallback<Boolean> callback);

	void SendUserStatusUpdateToServer(User currentUser, UserType ut, AsyncCallback<Boolean> callback);

	void getBaseComponents(AsyncCallback<ArrayList<BaseComponent>> callback);
}
