package materialpurchasing.client.UI;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;

import materialpurchasing.client.component.BaseComponent;

public class BuyerPage implements IsWidget {

	private TabPanel widget;
	private List<BaseComponent> components;

	public BuyerPage() {
		// controller.getInstance().addObserver(this);
	}

	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new TabPanel();
			widget.add(createShoppingListTab());
			widget.add(createComponentListTab());
		}

		return widget;
	}
	
	private Widget createComponentListTab() {
		Widget widget = null;
		return widget;
	}
	
	private Widget createShoppingListTab() {
		Widget widget = null;
		return widget;
	}

}
