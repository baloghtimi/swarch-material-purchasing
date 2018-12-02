package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.ProductEvent;
import materialpurchasing.client.serverCommunication.ProductService;
import materialpurchasing.shared.component.Component;
import materialpurchasing.shared.product.Product;

public class ProductController {
	HashMap<Long, Product> currentProducts = new HashMap<Long, Product>();

	ProductService ps = new ProductService();

	// Singelton Design Pattern
	private static ProductController instance = null;

	protected ProductController() {
		ps.getProducts();
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

	public ArrayList<Product> getCurrentProducts() {
		return new ArrayList<>(currentProducts.values());
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

	public void addProduct(String name, List<Component> components) {
		Product p = new Product(Id.getInstance().ID, name, components);
		currentProducts.put(Id.getInstance().ID, p);
		ps.addProduct(p);
		//
		Id.getInstance().ID++;
		this.sendProductAddedEvent(true);
	}

	public void modifyProduct(Long cid, String name, List<Component> components) {
		Product p = new Product(cid, name, components);
		currentProducts.put(cid, p);
		ps.modifyProduct(p);
		//
		this.sendProductModifiedEvent(true);
	}

	public void removeProduct(Long id) {
		currentProducts.remove(id);
		ps.removeProduct(id);
		this.sendProductRemovedEvent(true);
	}

	public void changeProductName(Long cid, String name) {
		currentProducts.get(cid).setName(name);
		ps.modifyProduct(currentProducts.get(cid));
		this.sendProductModifiedEvent(true);
	}

	public void addComponentToProduct(Long cid, ArrayList<Component> components) {
		for (Component c : components) {
			currentProducts.get(cid).getComponents().add(c);
		}
		ps.modifyProduct(currentProducts.get(cid));
		this.sendProductModifiedEvent(true);
	}

	public void removeComponentFromProduct(Long cid, ArrayList<Component> components) {
		for (Component c : components) {
			currentProducts.get(cid).getComponents().remove(c);
		}
		ps.modifyProduct(currentProducts.get(cid));
		this.sendProductModifiedEvent(true);
	}

	public void setCurrentProducts(HashMap<Long, Product> currentProducts) {
		this.currentProducts = currentProducts;
	}

}
