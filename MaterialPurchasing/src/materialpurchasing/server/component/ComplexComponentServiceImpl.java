package materialpurchasing.server.component;

import java.util.HashMap;

import materialpurchasing.shared.component.ComplexComponent;

public class ComplexComponentServiceImpl {
	HashMap<Long, ComplexComponent> serverCC;
	
	public ComplexComponentServiceImpl(){
		serverCC= new HashMap<Long, ComplexComponent>();
	}

	public HashMap<Long, ComplexComponent> getComplexComponents() {
		return serverCC;
	}

	public boolean addComplexComponents(ComplexComponent complexComponent) {
		serverCC.put(complexComponent.getId(), complexComponent);
		return true;
	}

	public boolean modifyComplexComponents(ComplexComponent complexComponent) {
		serverCC.put(complexComponent.getId(), complexComponent);
		return true;
	}

	public boolean removeComplexComponents(Long id) {
		serverCC.remove(id);
		return true;
	}

	public boolean sendComplexComponents(HashMap<Long, ComplexComponent> currentBC) {
		// TODO Auto-generated method stub
		return false;
	}

}
