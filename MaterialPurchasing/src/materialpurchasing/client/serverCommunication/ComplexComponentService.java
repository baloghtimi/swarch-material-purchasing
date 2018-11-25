package materialpurchasing.client.serverCommunication;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.client.controllers.ComplexComponentController;
import materialpurchasing.shared.component.ComplexComponent;

public class ComplexComponentService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public void getComplexComponents() {
		coreService.getComplexComponents(new AsyncCallback<HashMap<Long, ComplexComponent>>() {
			public void onSuccess(HashMap<Long, ComplexComponent> result) {
				ComplexComponentController.getInstance().setCurrentBC(result);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void addComplexComponent(ComplexComponent complexComponent) {
		coreService.addComplexComponents(complexComponent, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void modifyComponent(ComplexComponent complexComponent) {
		coreService.modifyComplexComponents(complexComponent, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void removeComponent(Long id) {
		coreService.removeComplexComponents(id, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public void SendComplexComponents(HashMap<Long, ComplexComponent> currentBC) {
		coreService.sendComplexComponents(currentBC, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
