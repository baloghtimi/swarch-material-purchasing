package materialpurchasing.server.product;

import java.util.HashMap;

import materialpurchasing.shared.product.Product;

public class ProductServiceImpl {
	
	HashMap<Long, Product> serverProducts;
	
	public ProductServiceImpl() {
		serverProducts=new HashMap<Long, Product>();
	}

	public HashMap<Long, Product> getProducts() {
		// TODO Auto-generated method stub
		return serverProducts;
	}

	public boolean addProducts(Product product) {
		serverProducts.put(product.getId(), product);
		return true;
	}

	public boolean modifyProducts(Product product) {
		serverProducts.put(product.getId(), product);
		return true;
	}

	public boolean removeProducts(Long id) {
		serverProducts.remove(id);
		return true;
	}

	public boolean sendProducts(HashMap<Long, Product> currentProduct) {
		// TODO Auto-generated method stub
		return false;
	}

}
