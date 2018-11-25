package materialpurchasing.client.UI;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.client.events.LoginEvent;

public class LoginPage implements IsWidget, LoginEvent {

	public LoginPage() {
		LoginController.getInstance().addObserver(this);
	}

	@Override
	public Widget asWidget() {
		HBoxLayoutContainer container = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
		container.setPack(BoxLayoutPack.CENTER);

		BoxLayoutData login = new BoxLayoutData(new Margins(0, 10, 0, 0));
		BoxLayoutData registration = new BoxLayoutData(new Margins(0, 0, 0, 10));

		container.add(createLoginForm(), login);
		container.add(createRegistrationForm(), registration);

		return container;
	}

	/*
	 * LoginController.getInstance().függvényhívás Teljesmértékben egyenlõ ezzel:
	 * 
	 * LoginController l=LoginController.getInstance(); l.függvényhívás
	 * 
	 * Mindig csak egy controller létezik és pontosan azt adja vissza.
	 */

	private FramedPanel createLoginForm() {
		final TextField username = new TextField();
		username.setAllowBlank(false);

		final PasswordField password = new PasswordField();
		password.setAllowBlank(false);

		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		vlc.add(new FieldLabel(username, "Username"));
		vlc.add(new FieldLabel(password, "Password"));

		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				LoginController.getInstance().login(username.getCurrentValue(), password.getCurrentValue());
			}
		};

		TextButton login = new TextButton("Login", selectHandler);

		FramedPanel panel = new FramedPanel();
		panel.setHeadingText("Login");
		panel.add(vlc, new MarginData(15, 15, 0, 15));
		panel.addButton(login);

		return panel;
	}

	private FramedPanel createRegistrationForm() {
		final TextField username = new TextField();
		username.setAllowBlank(false);

		final PasswordField password = new PasswordField();
		final PasswordField passwordAgain = new PasswordField();

		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		vlc.add(new FieldLabel(username, "Username"));
		vlc.add(new FieldLabel(password, "Password"));
		vlc.add(new FieldLabel(passwordAgain, "Password again"));

		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (password.getCurrentValue().equals(passwordAgain.getCurrentValue())) {
					LoginController.getInstance().register(username.getCurrentValue(), password.getCurrentValue());
				} else {
					showDialog("The two passwords are not the same.");
				}
			}
		};

		TextButton registrate = new TextButton("Registrate", selectHandler);

		FramedPanel panel = new FramedPanel();
		panel.setHeadingText("Registration");
		panel.add(vlc, new MarginData(15));
		panel.addButton(registrate);

		return panel;
	}

	@Override
	public void loginSuccessful() {
		RootPanel.get().clear();
		Viewport viewport = new Viewport();
		switch (LoginController.getInstance().getUserType()) {
		case MANAGER:
			ManagerPage managerPage = new ManagerPage(LoginController.getInstance().getUserId());
			viewport.setWidget(managerPage);
			break;
		case PRODUCER:
			ProducerPage producerPage = new ProducerPage(LoginController.getInstance().getUserId());
			viewport.setWidget(producerPage);
			break;
		case BUYER:
			BuyerPage buyerPage = new BuyerPage(LoginController.getInstance().getUserId());
			viewport.setWidget(buyerPage);
		}
		;
		RootPanel.get().add(viewport);
	}

	@Override
	public void loginFailed() {
		showDialog("Login failed.");
	}

	@Override
	public void registrationSuccessful() {
		showDialog("Successful registration.");
	}

	@Override
	public void registrationFailed() {
		showDialog("Registration failed.");
	}

	private void showDialog(String label) {
		Dialog dialog = new Dialog();
		dialog.setPredefinedButtons(PredefinedButton.OK);
		dialog.setHideOnButtonClick(true);
		dialog.setResizable(false);
		dialog.add(new Label(label));
		dialog.show();
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

}
