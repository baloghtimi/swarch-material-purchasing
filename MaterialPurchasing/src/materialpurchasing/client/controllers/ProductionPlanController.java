package materialpurchasing.client.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import materialpurchasing.client.events.ProductionPlanEvent;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;

public class ProductionPlanController {
	Long id = Long.MIN_VALUE;

	HashMap<Long, ProductionPlan> currentProductionPlans = new HashMap<Long, ProductionPlan>();

	// Singelton Design Pattern
	private static ProductionPlanController instance = null;

	protected ProductionPlanController() {
		
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
			ProductionPlan p=new ProductionPlan(id,product,amount,deadline);
			currentProductionPlans.put(id,p);
			//
			id++;
			this.sendProductionPlanAddedEvent(true);
		}
		
		public void modifyProductionPlan(Long cid, Product product, Integer amount, Date deadline) {
			ProductionPlan p=new ProductionPlan(cid,product,amount,deadline);
			currentProductionPlans.put(cid,p);
			//
			this.sendProductionPlanModifiedEvent(true);
		}
		
		public void removeProductionPlan(Long id) {
			currentProductionPlans.remove(id);
			this.sendProductionPlanRemovedEvent(true);
		}
}
