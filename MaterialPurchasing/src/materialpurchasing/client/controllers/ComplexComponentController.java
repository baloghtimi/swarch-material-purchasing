package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.ComplexComponentEvent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.component.Component;

public class ComplexComponentController {
	Long id = Long.MIN_VALUE;

	HashMap<Long, ComplexComponent> currentCC = new HashMap<Long, ComplexComponent>();

	// Singelton Design Pattern
	private static ComplexComponentController instance = null;

	protected ComplexComponentController() {
		
	}

	public static ComplexComponentController getInstance() {
		if (instance == null) {
			instance = new ComplexComponentController();
		}
		return instance;
	}

	// Observer Design Pattern
	private List<ComplexComponentEvent> complexComponentEventListeners = new ArrayList<ComplexComponentEvent>();

	public void addObserver(ComplexComponentEvent listener) {
		this.complexComponentEventListeners.add(listener);
	}

	public void removeObserver(ComplexComponentEvent listener) {
		this.complexComponentEventListeners.remove(listener);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<ComplexComponent> getCurrentComplexComponents(){
		
		return (ArrayList)currentCC.values();
	}
	
	public void sendComplexComponentAddedEvent(Boolean result) {
		for (ComplexComponentEvent listener : this.complexComponentEventListeners) {
			listener.complexComponentAdded(result);
		}
	}
	
	public void sendComplexComponentModifiedEvent(Boolean result) {
		for (ComplexComponentEvent listener : this.complexComponentEventListeners) {
			listener.complexComponentModified(result);
		}
	}
	
	public void sendComplexComponentRemovedEvent(Boolean result) {
		for (ComplexComponentEvent listener : this.complexComponentEventListeners) {
			listener.complexComponentRemoved(result);
		}
	}
	
	
	public void addComplexComponent(String name,ArrayList<Component> components) {
		ComplexComponent cc=new ComplexComponent(id,name);
		cc.setComponents(components);
		currentCC.put(id,cc);
		//
		id++;
		this.sendComplexComponentAddedEvent(true);
	}
	
	public void modifyComplexComponent(Long cid, String name,ArrayList<Component> components) {
		ComplexComponent cc=new ComplexComponent(cid,name);
		cc.setComponents(components);
		currentCC.put(cid,cc);
		//
		this.sendComplexComponentModifiedEvent(true);
	}
	
	public void removeComplexComponent(Long id) {
		currentCC.remove(id);
		this.sendComplexComponentRemovedEvent(true);
	}
	
	public void changeComplexComponentName(Long cid,String name) {
		currentCC.get(cid).setName(name);
		this.sendComplexComponentModifiedEvent(true);
	}
	
	public void addComponentToCC(Long cid,ArrayList<Component> components) {
		for(Component c: components) {
			currentCC.get(cid).getComponents().add(c);
		}
		this.sendComplexComponentModifiedEvent(true);
	}
	
	public void removeComponentFromCC(Long cid,ArrayList<Component> components) {
		for(Component c: components) {
			currentCC.get(cid).getComponents().remove(c);
		}
		this.sendComplexComponentModifiedEvent(true);
	}
}
