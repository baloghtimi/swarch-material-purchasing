package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.ProductEvent;
import materialpurchasing.shared.component.Component;
import materialpurchasing.shared.product.Product;

public class ProductController {
	Long id = Long.MIN_VALUE;

	HashMap<Long, Product> currentProducts = new HashMap<Long, Product>();

	// Singelton Design Pattern
	private static ProductController instance = null;

	protected ProductController() {
		
	}

	public static ProductController getInstance() {
		if (instance == null) {
			instance = new ProductController();
		}
		return instance;
	}

	// Observer Design Pattern
	private List<ProductEvent> productEventListeners = new ArrayList<ProductEvent>();

	public void addObserver(ProductEvent listener) {
		this.productEventListeners.add(listener);
	}

	public void removeObserver(ProductEvent listener) {
		this.productEventListeners.remove(listener);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Product> getCurrentProducts(){
		
		return (ArrayList)currentProducts.values();
	}
	
	public void sendProductAddedEvent(Boolean result) {
		for (ProductEvent listener : this.productEventListeners) {
			listener.productAddedEvent(result);
		}
	}
	
	public void sendProductModifiedEvent(Boolean result) {
		for (ProductEvent listener : this.productEventListeners) {
			listener.productModifiedEvent(result);
		}
	}
	
	public void sendProductRemovedEvent(Boolean result) {
		for (ProductEvent listener : this.productEventListeners) {
			listener.productRemovedEvent(result);
		}
	}
	
	
	public void addProduct(String name,ArrayList<Component> components) {
		Product p=new Product(id,name,components);
		currentProducts.put(id,p);
		//
		id++;
		this.sendProductAddedEvent(true);
	}
	
	public void modifyProduct(Long cid, String name,ArrayList<Component> components) {
		Product p=new Product(cid,name,components);
		currentProducts.put(cid,p);
		//
		this.sendProductModifiedEvent(true);
	}
	
	public void removeProduct(Long id) {
		currentProducts.remove(id);
		this.sendProductRemovedEvent(true);
	}
	
	public void changeProductName(Long cid,String name) {
		currentProducts.get(cid).setName(name);
		this.sendProductModifiedEvent(true);
	}
	
	public void addComponentToProduct(Long cid,ArrayList<Component> components) {
		for(Component c: components) {
			currentProducts.get(cid).getComponents().add(c);
		}
		this.sendProductModifiedEvent(true);
	}
	
	public void removeComponentFromProduct(Long cid,ArrayList<Component> components) {
		for(Component c: components) {
			currentProducts.get(cid).getComponents().remove(c);
		}
		this.sendProductModifiedEvent(true);
	}
}
