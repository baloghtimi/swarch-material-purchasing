package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.ProductionPlanEvent;
import materialpurchasing.client.serverCommunication.ProductionPlanService;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;

public class ProductionPlanController {
	HashMap<Long, ProductionPlan> currentProductionPlans = new HashMap<Long, ProductionPlan>();

	// Singelton Design Pattern
	private static ProductionPlanController instance = null;

	ProductionPlanService pps=new ProductionPlanService();
	
	protected ProductionPlanController() {
		pps.getProductionPlans();
	}

	public static ProductionPlanController getInstance() {
		if (instance == null) {
			instance = new ProductionPlanController();
		}
		return instance;
	}
	
	// Observer Design Pattern
		private List<ProductionPlanEvent> productPlanEventListeners = new ArrayList<ProductionPlanEvent>();

		public void addObserver(ProductionPlanEvent listener) {
			this.productPlanEventListeners.add(listener);
		}

		public void removeObserver(ProductionPlanEvent listener) {
			this.productPlanEventListeners.remove(listener);
		}
		
		public void sendProductionPlanAddedEvent(Boolean result) {
			for (ProductionPlanEvent listener : this.productPlanEventListeners) {
				listener.productionPlanAddedEvent(result);
			}
		}

		public void sendProductionPlanModifiedEvent(Boolean result) {
			for (ProductionPlanEvent listener : this.productPlanEventListeners) {
				listener.productionPlanModifiedEvent(result);
			}
		}

		public void sendProductionPlanRemovedEvent(Boolean result) {
			for (ProductionPlanEvent listener : this.productPlanEventListeners) {
			listener.productionPlanRemovedEvent(result);
			}
		}
		

		public List<ProductionPlan> getCurrentProductionPlans() {
			return new ArrayList<>(currentProductionPlans.values());
		}

		public void addProductionPlan(Product product, Integer amount, Date deadline) {
			ProductionPlan p=new ProductionPlan(Id.getInstance().ID,product,amount,deadline);
			currentProductionPlans.put(Id.getInstance().ID,p);
			pps.addProductionPlan(p);
			//
			Id.getInstance().ID++;
			this.sendProductionPlanAddedEvent(true);
		}
		
		public void modifyProductionPlan(Long cid, Product product, Integer amount, Date deadline) {
			ProductionPlan p=new ProductionPlan(cid,product,amount,deadline);
			currentProductionPlans.put(cid,p);
			pps.modifyComponent(p);
			//
			this.sendProductionPlanModifiedEvent(true);
		}
		
		public void removeProductionPlan(Long id) {
			currentProductionPlans.remove(id);
			pps.removeComponent(id);
			this.sendProductionPlanRemovedEvent(true);
		}

		public void setCurrentProductionPlans(HashMap<Long, ProductionPlan> currentProductionPlans) {
			this.currentProductionPlans = currentProductionPlans;
		}
		
		
}
