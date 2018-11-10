package materialpurchasing.client.UI;

import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.client.events.LoginEvent;

public class LoginPage implements LoginEvent {
	
	LoginPage(){
		LoginController.getInstance().addObserver(this);
	}
	
	/*LoginController.getInstance().függvényhívás
	 * Teljesmértékben egyenlő ezzel:
	 * 
	 * LoginController l=LoginController.getInstance();
	 * l.függvényhívás
	 * 
	 * Mindig csak egy controller létezik és pontosan azt adja vissza.
	 * */

	

	@Override
	public void loginSuccessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registrationSuccessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registrationFailed() {
		// TODO Auto-generated method stub
		
	}

}
