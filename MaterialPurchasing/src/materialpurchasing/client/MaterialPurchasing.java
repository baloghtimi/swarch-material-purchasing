package materialpurchasing.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import materialpurchasing.client.UI.LoginPage;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MaterialPurchasing implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		LoginPage loginPage = new LoginPage();
		RootPanel.get().add(loginPage.asWidget());
	}
}
