package materialpurchasing.client.controllers;

public class Id {

	Long ID = Long.MIN_VALUE;
	
	private static Id instance = null;
	
	public static Id getInstance() {
		if (instance == null) {
			instance = new Id();
		}
		return instance;
	}
}
