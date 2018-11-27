package materialpurchasing.client.UI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import materialpurchasing.client.UI.image.ImageResources;
import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.client.events.LogoutEvent;

public class UserPanel extends Component implements IsWidget, LogoutEvent {
	
	private String user;
	
	public UserPanel(String user) {
		LoginController.getInstance().addObserver(this);
		this.user = user;
	}

	@Override
	public Widget asWidget() {
		ImageResources resources = GWT.create(ImageResources.class);
		
		HBoxLayoutContainer container = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
		container.setBorders(true);
		
		BoxLayoutData flexLayout = new BoxLayoutData(new Margins(0, 5, 0, 5));
		flexLayout.setFlex(1);
		container.add(new Label("Material purchasing"), flexLayout);
		
		BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(0, 5, 0, 5));
		
		container.add(new Label(user), boxLayoutData);
		container.add(new Image(resources.user()), boxLayoutData);
		
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				LoginController.getInstance().logout();
			}
		};
		TextButton logout = new TextButton("", resources.onoff());
		logout.addSelectHandler(selectHandler);
		container.add(logout, boxLayoutData);
		
		return container;
	}

	@Override
	public void logOut() {
		LoginPage loginPage = new LoginPage();
		
		Viewport viewport = new Viewport();
		viewport.setWidget(loginPage);
		
		RootPanel.get().clear();
		RootPanel.get().add(viewport);
	}

}
