package materialpurchasing.client.serverCommunication;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.client.controllers.ProductController;
import materialpurchasing.shared.product.Product;

public class ProductService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public void getProducts() {
		coreService.getProducts(new AsyncCallback<HashMap<Long, Product>>() {
			public void onSuccess(HashMap<Long, Product> result) {
				ProductController.getInstance().setCurrentProducts(result);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void addProduct(Product product) {
		coreService.addProducts(product, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void modifyProduct(Product product) {
		coreService.modifyProducts(product, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void removeProduct(Long id) {
		coreService.removeProducts(id, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public void SendProducts(HashMap<Long, Product> currentProduct) {
		coreService.sendProducts(currentProduct, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
