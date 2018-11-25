package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface CoreService extends RemoteService {
	// String greetServer(String name) throws IllegalArgumentException;

	User SendLoginToServer(String userID, String password);

	Boolean SendRegistrationToServer(String userID, String password, UserType userType);

	Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut);
	
	ArrayList<BaseComponent> getBaseComponents();
}
