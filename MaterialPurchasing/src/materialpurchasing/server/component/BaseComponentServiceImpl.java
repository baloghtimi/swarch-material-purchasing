package materialpurchasing.server.component;

import java.util.HashMap;

import materialpurchasing.shared.component.BaseComponent;

public class BaseComponentServiceImpl {

	private HashMap<Long, BaseComponent> serverBC;

	public BaseComponentServiceImpl() {

		serverBC = new HashMap<Long, BaseComponent>();
	}

	public HashMap<Long, BaseComponent> getBaseComponents() {
		return serverBC;
	}

	public boolean addBaseComponents(BaseComponent baseComponent) {
		serverBC.put(baseComponent.getId(), baseComponent);
		// TODO Auto-generated method stub
		return false;
	}

	public boolean modifyBaseComponents(BaseComponent baseComponent) {
		serverBC.put(baseComponent.getId(), baseComponent);
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeBaseComponents(Long id) {
		serverBC.remove(id);
		// TODO Auto-generated method stub
		return false;
	}

	public boolean sendBaseComponents(HashMap<Long, BaseComponent> currentBC) {
		// TODO Auto-generated method stub
		return false;
	}
}
