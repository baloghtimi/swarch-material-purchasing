package materialpurchasing.server.productionPlan;

import java.util.HashMap;

import materialpurchasing.shared.product.ProductionPlan;

public class ProductionPlanServiceImpl {
	
	 HashMap<Long, ProductionPlan> serverPP;
	public ProductionPlanServiceImpl(){
		serverPP=new HashMap<Long, ProductionPlan>();
	}

	public HashMap<Long, ProductionPlan> getProductionPlans() {
		return serverPP;
	}

	public boolean addProductionPlans(ProductionPlan product) {
		serverPP.put(product.getId(), product);
		return true;
	}

	public boolean modifyProductionPlans(ProductionPlan product) {
		serverPP.put(product.getId(), product);
		return true;
	}

	public boolean removeProductionPlans(Long id) {
		serverPP.remove(id);
		return true;
	}

	public boolean sendProductionPlans(HashMap<Long, ProductionPlan> currentProductionPlan) {
		
		return false;
	}

}
