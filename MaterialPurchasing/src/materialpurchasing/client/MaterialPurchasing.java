package materialpurchasing.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

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
		
		Viewport viewport = new Viewport();
		viewport.setWidget(loginPage);
		
		RootPanel.get().add(viewport);
	}
}
