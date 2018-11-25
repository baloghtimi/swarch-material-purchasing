package materialpurchasing.shared.user;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1115773560155391468L;

	String id;
	
	UserType userType;
	
	public User(){
		this("Default",UserType.MANAGER);
	}
	
	public User(String name, UserType userType){
		this.id=name;
		this.userType=userType;
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

}
