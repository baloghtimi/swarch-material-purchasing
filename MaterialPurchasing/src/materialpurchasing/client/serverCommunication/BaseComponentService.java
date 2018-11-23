package materialpurchasing.client.serverCommunication;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import materialpurchasing.shared.component.BaseComponent;

public class BaseComponentService {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	public void getBaseComponents() {
		coreService.getBaseComponents(new AsyncCallback<ArrayList<BaseComponent>>() {
			public void onSuccess(ArrayList<BaseComponent> result) {

			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void addBaseComponent(BaseComponent baseComponent) {
		// TODO Auto-generated method stub
		
	}
}
