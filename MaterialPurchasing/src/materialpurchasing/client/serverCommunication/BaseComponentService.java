package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.client.controllers.BaseComponentController;
import materialpurchasing.shared.component.BaseComponent;

public class BaseComponentService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public void getBaseComponents() {
		coreService.getBaseComponents(new AsyncCallback<HashMap<Long, BaseComponent>>() {
			public void onSuccess(HashMap<Long, BaseComponent> result) {
				BaseComponentController.getInstance().setCurrentBC(result);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void addBaseComponent(BaseComponent baseComponent) {
		coreService.addBaseComponents(baseComponent, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void modifyComponent(BaseComponent baseComponent) {
		coreService.modifyBaseComponents(baseComponent, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void removeComponent(Long id) {
		coreService.removeBaseComponents(id, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public void SendBaseComponents(HashMap<Long, BaseComponent> currentBC) {
		coreService.sendBaseComponents(currentBC, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {

			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
