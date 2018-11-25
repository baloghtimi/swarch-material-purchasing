package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface CoreService extends RemoteService {
	// String greetServer(String name) throws IllegalArgumentException;

	User SendLoginToServer(String userID, String password);

	Boolean SendRegistrationToServer(String userID, String password);

	Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut);
	
	HashMap<Long, BaseComponent> getBaseComponents();

	boolean addBaseComponents(BaseComponent baseComponent);

	boolean modifyBaseComponents(BaseComponent baseComponent);

	boolean removeBaseComponents(Long id);

	boolean sendBaseComponents(HashMap<Long, BaseComponent> currentBC);

	HashMap<Long, ComplexComponent> getComplexComponents();

	boolean addComplexComponents(ComplexComponent complexComponent);

	boolean modifyComplexComponents(ComplexComponent complexComponent);

	boolean removeComplexComponents(Long id);

	boolean sendComplexComponents(HashMap<Long, ComplexComponent> currentBC);

	HashMap<Long, Product> getProducts();

	boolean addProducts(Product product);

	boolean modifyProducts(Product product);

	boolean removeProducts(Long id);

	boolean sendProducts(HashMap<Long, Product> currentProduct);

	HashMap<Long, ProductionPlan> getProductionPlans();

	boolean addProductionPlans(ProductionPlan pp);

	boolean modifyProductionPlans(ProductionPlan pp);

	boolean removeProductionPlans(Long id);

	boolean sendProductionPlans(HashMap<Long, ProductionPlan> pps);
}
