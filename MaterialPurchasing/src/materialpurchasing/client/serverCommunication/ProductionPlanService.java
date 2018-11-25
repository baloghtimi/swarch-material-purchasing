package materialpurchasing.client.serverCommunication;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.client.controllers.ProductionPlanController;
import materialpurchasing.shared.product.ProductionPlan;

public class ProductionPlanService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public void getProductionPlans() {
		coreService.getProductionPlans(new AsyncCallback<HashMap<Long, ProductionPlan>>() {
			public void onSuccess(HashMap<Long, ProductionPlan> result) {
				ProductionPlanController.getInstance().setCurrentProductionPlans(result);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void addProductionPlan(ProductionPlan pp) {
		coreService.addProductionPlans(pp, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void modifyComponent(ProductionPlan pp) {
		coreService.modifyProductionPlans(pp, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void removeComponent(Long id) {
		coreService.removeProductionPlans(id, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public void SendProductionPlans(HashMap<Long, ProductionPlan> pps) {
		coreService.sendProductionPlans(pps, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
