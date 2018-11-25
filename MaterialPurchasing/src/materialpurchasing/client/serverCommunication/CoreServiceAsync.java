package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

/**
 * The async counterpart of <code>CoreService</code>.
 */
public interface CoreServiceAsync {
	//void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void SendLoginToServer(String userID, String password, AsyncCallback<User> callback);

	void SendRegistrationToServer(String userID, String password, AsyncCallback<Boolean> callback);

	void SendUserStatusUpdateToServer(User currentUser, UserType ut, AsyncCallback<Boolean> callback);

	void addBaseComponents(BaseComponent baseComponent, AsyncCallback<Boolean> asyncCallback);

	void modifyBaseComponents(BaseComponent baseComponent, AsyncCallback<Boolean> asyncCallback);

	void removeBaseComponents(Long id, AsyncCallback<Boolean> asyncCallback);

	void sendBaseComponents(HashMap<Long, BaseComponent> currentBC, AsyncCallback<Boolean> asyncCallback);

	void getBaseComponents(AsyncCallback<HashMap<Long, BaseComponent>> asyncCallback);

	void getComplexComponents(AsyncCallback<HashMap<Long, ComplexComponent>> asyncCallback);

	void addComplexComponents(ComplexComponent complexComponent, AsyncCallback<Boolean> asyncCallback);

	void modifyComplexComponents(ComplexComponent complexComponent, AsyncCallback<Boolean> asyncCallback);

	void removeComplexComponents(Long id, AsyncCallback<Boolean> asyncCallback);

	void sendComplexComponents(HashMap<Long, ComplexComponent> currentBC, AsyncCallback<Boolean> asyncCallback);

	void getProducts(AsyncCallback<HashMap<Long, Product>> asyncCallback);

	void addProducts(Product product, AsyncCallback<Boolean> asyncCallback);

	void modifyProducts(Product product, AsyncCallback<Boolean> asyncCallback);

	void removeProducts(Long id, AsyncCallback<Boolean> asyncCallback);

	void sendProducts(HashMap<Long, Product> currentProduct, AsyncCallback<Boolean> asyncCallback);

	void getProductionPlans(AsyncCallback<HashMap<Long, ProductionPlan>> asyncCallback);

	void addProductionPlans(ProductionPlan pp, AsyncCallback<Boolean> asyncCallback);

	void modifyProductionPlans(ProductionPlan pp, AsyncCallback<Boolean> asyncCallback);

	void removeProductionPlans(Long id, AsyncCallback<Boolean> asyncCallback);

	void sendProductionPlans(HashMap<Long, ProductionPlan> pps, AsyncCallback<Boolean> asyncCallback);

}
