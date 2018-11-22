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
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

import materialpurchasing.client.controllers.LoginController;
import materialpurchasing.client.events.LoginEvent;
import materialpurchasing.shared.user.User;

public class LoginPage implements IsWidget, LoginEvent {

	private VBoxLayoutContainer widget;

	public LoginPage() {
		LoginController.getInstance().addObserver(this);
	}

	@Override
	public Widget asWidget() {
		if (widget == null) {
			BoxLayoutData login = new BoxLayoutData(new Margins(0, 0, 20, 0));
			login.setFlex(1);
		    BoxLayoutData registration = new BoxLayoutData(new Margins(10, 0, 0, 0));
			
			widget = new VBoxLayoutContainer(VBoxLayoutAlign.CENTER);
			widget.add(createLoginForm(), login);
			widget.add(createRegistrationForm(), registration);
		}

		return widget;
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
		panel.setHeadingText("Login");
		panel.add(vlc, new MarginData(15));
		panel.addButton(registrate);

		return panel;
	}

	@Override
	public void loginSuccessful() {
		//LoginController.getInstance().getUserType();
		BuyerPage buyerPage = new BuyerPage();
		RootPanel.get().add(buyerPage.asWidget());
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
