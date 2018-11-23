package materialpurchasing.client.serverCommunication;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface CoreService extends RemoteService {
	// String greetServer(String name) throws IllegalArgumentException;

	User SendLoginToServer(String userID, String password);

	Boolean SendRegistrationToServer(String userID, String password);

	Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut);
}
