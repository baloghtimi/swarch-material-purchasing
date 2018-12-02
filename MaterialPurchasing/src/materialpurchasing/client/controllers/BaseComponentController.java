package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.BaseComponentEvent;
import materialpurchasing.client.serverCommunication.BaseComponentService;
import materialpurchasing.shared.component.BaseComponent;

public class BaseComponentController {

	BaseComponentService bcs = new BaseComponentService();

	HashMap<Long, BaseComponent> currentBC = new HashMap<Long, BaseComponent>();

	// Singelton Design Pattern
	private static BaseComponentController instance = null;

	protected BaseComponentController() {
		bcs.getBaseComponents();
	}

	public static BaseComponentController getInstance() {
		if (instance == null) {
			instance = new BaseComponentController();
		}
		return instance;
	}

	// Observer Design Pattern
	private List<BaseComponentEvent> baseComponentEventListeners = new ArrayList<BaseComponentEvent>();

	public void addObserver(BaseComponentEvent listener) {
		this.baseComponentEventListeners.add(listener);
	}

	public void removeObserver(BaseComponentEvent listener) {
		this.baseComponentEventListeners.remove(listener);
	}

	public void sendBaseComponentAddedEvent(Boolean result) {
		for (BaseComponentEvent listener : this.baseComponentEventListeners) {
			listener.baseComponentAdded(result);
		}
	}

	public void sendBaseComponentModifiedEvent(Boolean result) {
		for (BaseComponentEvent listener : this.baseComponentEventListeners) {
			listener.baseComponentModified(result);
		}
	}

	public void sendBaseComponentRemovedEvent(Boolean result) {
		for (BaseComponentEvent listener : this.baseComponentEventListeners) {
			listener.baseComponentRemoved(result);
		}
	}

	public void addBaseComponent(String name, Integer price, Integer acquireTime) {
		currentBC.put(Id.getInstance().ID, new BaseComponent(Id.getInstance().ID, name, price, acquireTime));
		bcs.addBaseComponent(new BaseComponent(Id.getInstance().ID, name, price, acquireTime));
		//
		Id.getInstance().ID++;
		this.sendBaseComponentAddedEvent(true);
	}

	public ArrayList<BaseComponent> getCurrentBaseComponents() {
		return new ArrayList<>(currentBC.values());
	}

	public void modifyBaseComponent(Long cid, String name, Integer price, Integer acquireTime) {
		currentBC.put(cid, new BaseComponent(cid, name, price, acquireTime));
		bcs.modifyComponent(new BaseComponent(cid, name, price, acquireTime));
		//
		this.sendBaseComponentModifiedEvent(true);
	}

	public void removeBaseComponent(Long id) {
		if (!currentBC.remove(id).equals(null)) {
			bcs.removeComponent(id);
			this.sendBaseComponentRemovedEvent(true);
		} else {
			this.sendBaseComponentRemovedEvent(false);
		}
	}

	public HashMap<Long, BaseComponent> getCurrentBC() {
		return currentBC;
	}

	public void setCurrentBC(HashMap<Long, BaseComponent> currentBC) {
		this.currentBC = currentBC;
	}
	
}
