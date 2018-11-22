package materialpurchasing.shared.user;

public class User {
	
	String id;
	
	UserType userType;
	
	public User(){
		this("Default",UserType.BUYER);
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
