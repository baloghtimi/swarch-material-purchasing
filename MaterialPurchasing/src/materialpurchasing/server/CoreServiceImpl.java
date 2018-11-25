package materialpurchasing.server;

import materialpurchasing.client.serverCommunication.CoreService;
import materialpurchasing.server.login.LoginServiceImpl;
import materialpurchasing.server.product.ProductServiceImpl;
import materialpurchasing.server.productionPlan.ProductionPlanServiceImpl;
import materialpurchasing.server.component.BaseComponentServiceImpl;
import materialpurchasing.server.component.ComplexComponentServiceImpl;
import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;
import materialpurchasing.shared.user.User;
import materialpurchasing.shared.user.UserType;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoreServiceImpl extends RemoteServiceServlet implements CoreService {
	
	LoginServiceImpl loginServiceImpl=new LoginServiceImpl();
	
	BaseComponentServiceImpl bcsImpl=new BaseComponentServiceImpl();
	ComplexComponentServiceImpl ccsImpl=new ComplexComponentServiceImpl();
	
	ProductServiceImpl psImpl=new ProductServiceImpl();
	ProductionPlanServiceImpl ppsImpl=new ProductionPlanServiceImpl();

	@Override
	public User SendLoginToServer(String userID, String password) {
		return loginServiceImpl.SendLoginToServer(userID, password);
	}

	@Override
	public Boolean SendRegistrationToServer(String userID, String password) {
		return loginServiceImpl.SendRegistrationToServer(userID, password);
	}

	@Override
	public Boolean SendUserStatusUpdateToServer(User currentUser, UserType ut) {
		return loginServiceImpl.SendUserStatusUpdateToServer(currentUser, ut);
	}

	@Override
	public HashMap<Long, BaseComponent> getBaseComponents() {
		return bcsImpl.getBaseComponents();
	}

	@Override
	public boolean addBaseComponents(BaseComponent baseComponent) {
		return bcsImpl.addBaseComponents(baseComponent);
	}

	@Override
	public boolean modifyBaseComponents(BaseComponent baseComponent) {
		return bcsImpl.modifyBaseComponents(baseComponent);
	}

	@Override
	public boolean removeBaseComponents(Long id) {
		return bcsImpl.removeBaseComponents(id);
	}

	@Override
	public boolean sendBaseComponents(HashMap<Long, BaseComponent> currentBC) {
		return bcsImpl.sendBaseComponents(currentBC);
	}

	@Override
	public HashMap<Long, ComplexComponent> getComplexComponents() {
		return ccsImpl.getComplexComponents();
	}

	@Override
	public boolean addComplexComponents(ComplexComponent complexComponent) {
		return ccsImpl.addComplexComponents(complexComponent);
	}

	@Override
	public boolean modifyComplexComponents(ComplexComponent complexComponent) {
		return ccsImpl.modifyComplexComponents(complexComponent);
	}

	@Override
	public boolean removeComplexComponents(Long id) {
		return ccsImpl.removeComplexComponents(id);
	}

	@Override
	public boolean sendComplexComponents(HashMap<Long, ComplexComponent> currentBC) {
		return ccsImpl.sendComplexComponents(currentBC);
	}

	@Override
	public HashMap<Long, Product> getProducts() {
		return psImpl.getProducts();
	}

	@Override
	public boolean addProducts(Product product) {
		return psImpl.addProducts(product);
	}

	@Override
	public boolean modifyProducts(Product product) {
		return psImpl.modifyProducts(product);
	}

	@Override
	public boolean removeProducts(Long id) {
		return psImpl.removeProducts(id);
	}

	@Override
	public boolean sendProducts(HashMap<Long, Product> currentProduct) {
		return psImpl.sendProducts(currentProduct);
	}

	@Override
	public HashMap<Long, ProductionPlan> getProductionPlans() {
		return ppsImpl.getProductionPlans();
	}

	@Override
	public boolean addProductionPlans(ProductionPlan pp) {
		return ppsImpl.addProductionPlans(pp);
	}

	@Override
	public boolean modifyProductionPlans(ProductionPlan pp) {
		return ppsImpl.modifyProductionPlans(pp);
	}

	@Override
	public boolean removeProductionPlans(Long id) {
		return ppsImpl.removeProductionPlans(id);
	}

	@Override
	public boolean sendProductionPlans(HashMap<Long, ProductionPlan> pps) {
		return ppsImpl.sendProductionPlans(pps);
	}
}
