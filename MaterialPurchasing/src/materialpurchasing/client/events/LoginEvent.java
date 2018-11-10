package materialpurchasing.client.events;


public abstract interface LoginEvent {
	
	public abstract void loginSuccessful();
	
	public abstract void loginFailed();
	
	public abstract void registrationSuccessful();
	
	public abstract void registrationFailed();

}
