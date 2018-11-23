package materialpurchasing.server;

import materialpurchasing.client.serverCommunication.CoreService;
import materialpurchasing.server.login.LoginServiceImpl;
import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoreServiceImpl extends RemoteServiceServlet implements CoreService {
	
	LoginServiceImpl loginServiceImpl=new LoginServiceImpl();

	@Override
	public User SendLoginToServer(String userID, String password) {
		return loginServiceImpl.SendLoginToServer(userID, password);
	}

	@Override
	public Boolean SendRegistrationToServer(String userID, String password) {
		return loginServiceImpl.SendRegistrationToServer(userID, password);
	}

	@Override
	public Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut) {
		return loginServiceImpl.SendUserStatusUpdateToServer(currentUser, ut);
	}

	@Override
	public ArrayList<BaseComponent> getBaseComponents() {
		// TODO Auto-generated method stub
		return null;
	}
}
