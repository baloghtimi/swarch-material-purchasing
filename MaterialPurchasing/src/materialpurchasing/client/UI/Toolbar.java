package materialpurchasing.client.UI;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import materialpurchasing.client.UI.image.ImageResources;

public class Toolbar extends Component implements IsWidget {

	private ImageResources resources = GWT.create(ImageResources.class);
	private TextButton refreshButton = new TextButton("", resources.refresh());
	private TextButton addButton = new TextButton("", resources.plus());
	private TextButton modifyButton = new TextButton("", resources.pencil());
	private TextButton removeButton = new TextButton("", resources.trash());

	private List<TextButton> buttons = new LinkedList<>();

	public Toolbar() {
		buttons.add(refreshButton);
		buttons.add(addButton);
		buttons.add(modifyButton);
		buttons.add(removeButton);
	}

	@Override
	public Widget asWidget() {
		ToolBar container = new ToolBar();
		container.setMinButtonWidth(50);
		container.setEnableOverflow(false);
		
		setModificationMode(false);

		for (TextButton button : buttons) {
			container.add(button);
		}
		return container;
	}

	public void setRefreshButtonSelectHandler(SelectHandler handler) {
		refreshButton.addSelectHandler(handler);
	}

	public void setAddButtonSelectHandler(SelectHandler handler) {
		addButton.addSelectHandler(handler);
	}

	public void setModifyButtonSelectHandler(SelectHandler handler) {
		modifyButton.addSelectHandler(handler);
	}

	public void setRemoveButtonSelectHandler(SelectHandler handler) {
		removeButton.addSelectHandler(handler);
	}

	public void setModificationMode(boolean on) {
		modifyButton.setEnabled(on);
		removeButton.setEnabled(on);
	}
}
