package materialpurchasing.client.serverCommunication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

public class LoginService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public LoginService() {
		SendRegistration("Default", "default", UserType.BUYER);
	}

	public void SendLogin(String userID, String password) {
		coreService.SendLoginToServer(userID, password, new AsyncCallback<User>() {
			public void onSuccess(User result) {
				if (result != null) {
					LoginController.getInstance().sendLoginSuccessfulEvent(result);
				} else {
					LoginController.getInstance().sendLoginFailedEvent();
				}
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				LoginController.getInstance().sendLoginFailedEvent();
			}
		});
	}

	public void SendRegistration(String userID, String password, UserType userType) {
		coreService.SendRegistrationToServer(userID, password, userType, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if (result) {
					LoginController.getInstance().sendRegistrationSuccessfulEvent();
				} else {
					LoginController.getInstance().sendRegistrationFailedEvent();
				}
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				LoginController.getInstance().sendRegistrationFailedEvent();
			}
		});
	}

	public void SendUserStatusUpdate(User currentUser, UserType ut) {
		coreService.SendUserStatusUpdateToServer(currentUser, ut, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
