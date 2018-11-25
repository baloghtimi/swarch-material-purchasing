package materialpurchasing.client.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import materialpurchasing.client.UI.util.ShoppingListItem;
import materialpurchasing.client.controllers.BaseComponentController;
import materialpurchasing.client.controllers.ProductionPlanController;
import materialpurchasing.client.events.BaseComponentEvent;
import materialpurchasing.shared.component.BaseComponent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.component.Component;
import materialpurchasing.shared.product.ProductionPlan;

public class BuyerPage implements IsWidget, BaseComponentEvent {

	private String user;
	private List<BaseComponent> components = new ArrayList<>();
	private Grid<BaseComponent> grid;
	private BaseComponent currentComponent;
	private Toolbar toolbar;

	public BuyerPage(String user) {
		BaseComponentController.getInstance().addObserver(this);
		this.user = user;
	}

	@Override
	public Widget asWidget() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData userBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		UserPanel userPanel = new UserPanel(user);
		container.add(userPanel, userBox);

		BoxLayoutData tabBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		tabBox.setFlex(1);
		TabPanel tabPanel = new TabPanel();
		tabPanel.setBorders(true);
		tabPanel.add(createShoppingListTab(), "Shopping list");
		tabPanel.add(createComponentListTab(), "Base components");
		container.add(tabPanel, tabBox);

		return container;
	}

	private void refreshComponentList() {
		components = BaseComponentController.getInstance().getCurrentBaseComponents();
		grid.getStore().replaceAll(components);
		grid.getView().refresh(true);
	}

	private Widget createComponentListTab() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData gridBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		gridBox.setFlex(1);

		createBaseComponentGrid();

		container.add(createToolbar(), toolbarBox);
		container.add(grid, gridBox);

		return container;
	}

	private void createBaseComponentGrid() {
		ListStore<BaseComponent> listStore = new ListStore<BaseComponent>(gridProperties.id());
		components = BaseComponentController.getInstance().getCurrentBaseComponents();
		listStore.addAll(components);

		ColumnConfig<BaseComponent, String> nameColumn = new ColumnConfig<BaseComponent, String>(gridProperties.name(),
				200, "Name");
		ColumnConfig<BaseComponent, Integer> priceColumn = new ColumnConfig<BaseComponent, Integer>(
				gridProperties.price(), 200, "Price");
		ColumnConfig<BaseComponent, Integer> purchaseTimeColumn = new ColumnConfig<BaseComponent, Integer>(
				gridProperties.purchaseTime(), 200, "Purchase time");

		List<ColumnConfig<BaseComponent, ?>> columns = new ArrayList<ColumnConfig<BaseComponent, ?>>();
		columns.add(nameColumn);
		columns.add(priceColumn);
		columns.add(purchaseTimeColumn);
		ColumnModel<BaseComponent> columnModel = new ColumnModel<BaseComponent>(columns);

		GridView<BaseComponent> gridView = new GridView<BaseComponent>();
		gridView.setAutoFill(true);
//		gridView.setAutoExpandColumn(nameColumn);

		grid = new Grid<BaseComponent>(listStore, columnModel, gridView);
		grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<BaseComponent>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<BaseComponent> se) {
				if (grid.getSelectionModel().getSelectedItem() != null) {
					toolbar.setModificationMode(true);
					currentComponent = grid.getSelectionModel().getSelectedItem();
				}
			}
		});
		grid.setBorders(true);
	}

	public interface GridProperties extends PropertyAccess<BaseComponent> {
		ModelKeyProvider<BaseComponent> id();

		ValueProvider<BaseComponent, String> name();

		ValueProvider<BaseComponent, Integer> price();

		ValueProvider<BaseComponent, Integer> purchaseTime();
	}

	public static GridProperties gridProperties = GWT.create(GridProperties.class);

	private Toolbar createToolbar() {
		toolbar = new Toolbar();

		SelectHandler refreshSelectHandler = createRefreshSelectionHandler();
		toolbar.setRefreshButtonSelectHandler(refreshSelectHandler);

		SelectHandler addSelectHandler = createAddSelectionHandler();
		toolbar.setAddButtonSelectHandler(addSelectHandler);

		SelectHandler modifySelectHandler = createModifySelectionHandler();
		toolbar.setModifyButtonSelectHandler(modifySelectHandler);

		SelectHandler removeSelectHandler = createRemoveSelectionHandler();
		toolbar.setRemoveButtonSelectHandler(removeSelectHandler);

		return toolbar;
	}

	private SelectHandler createRefreshSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshComponentList();
			}
		};

		return selectHandler;
	}

	private SelectHandler createAddSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final IntegerField priceField = new IntegerField();
				priceField.setAllowBlank(false);
				container.add(new FieldLabel(priceField, "Price"), boxLayoutData);

				final IntegerField purcahseTimeField = new IntegerField();
				purcahseTimeField.setAllowBlank(false);
				container.add(new FieldLabel(purcahseTimeField, "Purchase time"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Adding new base component");
				dialog.add(container);
				dialog.setPredefinedButtons(PredefinedButton.CANCEL, PredefinedButton.OK);
				dialog.getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						dialog.hide();
					}
				});
				dialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (nameField.isValid() && priceField.isValid() && purcahseTimeField.isValid()) {
							BaseComponentController.getInstance().addBaseComponent(nameField.getCurrentValue(),
									priceField.getCurrentValue(), purcahseTimeField.getCurrentValue());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createModifySelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setValue(currentComponent.getName());
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final IntegerField priceField = new IntegerField();
				priceField.setValue(currentComponent.getPrice());
				priceField.setAllowBlank(false);
				container.add(new FieldLabel(priceField, "Price"), boxLayoutData);

				final IntegerField purcahseTimeField = new IntegerField();
				purcahseTimeField.setValue(currentComponent.getPurchaseTime());
				purcahseTimeField.setAllowBlank(false);
				container.add(new FieldLabel(purcahseTimeField, "Purchase time"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Modifying base component");
				dialog.add(container);
				dialog.setPredefinedButtons(PredefinedButton.CANCEL, PredefinedButton.OK);
				dialog.getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						dialog.hide();
					}
				});
				dialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (nameField.isValid() && priceField.isValid() && purcahseTimeField.isValid()) {
							BaseComponentController.getInstance().modifyBaseComponent(currentComponent.getId(),
									nameField.getCurrentValue(), priceField.getCurrentValue(),
									purcahseTimeField.getCurrentValue());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createRemoveSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final Dialog dialog = new Dialog();
				dialog.add(new Label("Are you sure you want to remove this base component?"));
				dialog.setPredefinedButtons(PredefinedButton.NO, PredefinedButton.YES);
				dialog.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						dialog.hide();
					}
				});
				dialog.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						BaseComponentController.getInstance().removeBaseComponent(currentComponent.getId());
						dialog.hide();
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private Widget createShoppingListTab() {
		return new Label("Shopping list");
//		ListStore<ShoppingListItem> listStore = new ListStore<ShoppingListItem>(shoppingListItemProperties.id());
//		listStore.addAll(getShoppingList());
//
//		ColumnConfig<ShoppingListItem, BaseComponent> componentColumn = new ColumnConfig<ShoppingListItem, BaseComponent>(
//				shoppingListItemProperties.component(), 200, "Component");
//		ColumnConfig<ShoppingListItem, Integer> amountColumn = new ColumnConfig<ShoppingListItem, Integer>(
//				shoppingListItemProperties.amount(), 200, "Amount");
//		ColumnConfig<ShoppingListItem, Date> deadlineColumn = new ColumnConfig<ShoppingListItem, Date>(
//				shoppingListItemProperties.deadLine(), 200, "Deadline");
//
//		List<ColumnConfig<ShoppingListItem, ?>> columns = new ArrayList<ColumnConfig<ShoppingListItem, ?>>();
//		columns.add(componentColumn);
//		columns.add(amountColumn);
//		columns.add(deadlineColumn);
//		ColumnModel<ShoppingListItem> columnModel = new ColumnModel<ShoppingListItem>(columns);
//
//		GridView<ShoppingListItem> gridView = new GridView<ShoppingListItem>();
//		gridView.setAutoFill(true);
//
//		Grid<ShoppingListItem> shoppingListGrid = new Grid<ShoppingListItem>(listStore, columnModel, gridView);
//		shoppingListGrid.setBorders(true);
//
//		return shoppingListGrid;
	}

//	private List<ShoppingListItem> getShoppingList() {
//		List<ShoppingListItem> shoppingList = new ArrayList<>();
//		Map<BaseComponent, ShoppingListItem> shoppingMap = new HashMap<>();
//
//		List<ProductionPlan> plans = ProductionPlanController.getInstance().getCurrentProductionPlans();
//		for (ProductionPlan plan : plans) {
//			for (BaseComponent component : getBaseComponents(plan)) {
//				if(!shoppingMap.containsKey(component)) {
//					shoppingMap.put(component, new ShoppingListItem(component, 0, new Date()));
//				} else {
//					Integer newAmount = shoppingMap.get(component).getAmount() + 1;
//					Date newDeadline = plan.getDeadline().before(shoppingMap.get(component).getDeadLine()) ? plan.getDeadline() : shoppingMap.get(component).getDeadLine();
//					shoppingMap.put(component, new ShoppingListItem(component, newAmount, newDeadline));
//				}
//			}
//		}
//
//		return shoppingList;
//	}

//	private List<BaseComponent> getBaseComponents(ProductionPlan plan) {
//		List<BaseComponent> components = new ArrayList<>();
//		for (Component component : plan.getProduct().getComponents()) {
//			if (component instanceof BaseComponent) {
//				components.add((BaseComponent) component);
//			} else {
//				collectComponents(components, (ComplexComponent) component);
//			}
//		}
//		return components;
//	}

//	private void collectComponents(List<BaseComponent> list, ComplexComponent complex) {
//		for (Component component : complex.getComponents()) {
//			if (component instanceof BaseComponent) {
//				list.add((BaseComponent) component);
//			} else {
//				collectComponents(list, (ComplexComponent) component);
//			}
//		}
//	}

//	public interface ShoppingListItemProperties extends PropertyAccess<ShoppingListItem> {
//		@SuppressWarnings("rawtypes")
//		@Path("name")
//		ModelKeyProvider id();
//
//		ValueProvider<ShoppingListItem, BaseComponent> component();
//
//		ValueProvider<ShoppingListItem, Integer> amount();
//
//		ValueProvider<ShoppingListItem, Date> deadLine();
//	}
//
//	public static ShoppingListItemProperties shoppingListItemProperties = GWT.create(ShoppingListItemProperties.class);

	@Override
	public void baseComponentAdded(Boolean result) {
		if (result) {
			showDialog("Base component added successfully.");
		} else {
			showDialog("Failed to add base component.");
		}
	}

	@Override
	public void baseComponentModified(Boolean result) {
		if (result) {
			showDialog("Base component modified successfully.");
		} else {
			showDialog("Failed to modify base component.");
		}
	}

	@Override
	public void baseComponentRemoved(Boolean result) {
		if (result) {
			showDialog("Base component removed successfully.");
		} else {
			showDialog("Failed to remove base component.");
		}
	}

	private void showDialog(String label) {
		final Dialog dialog = new Dialog();
		dialog.setHeadingText("Message");
		dialog.setPredefinedButtons(PredefinedButton.OK);
		dialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshComponentList();
				dialog.hide();
			}
		});
		dialog.setClosable(false);
		dialog.setResizable(false);
		dialog.add(new Label(label));
		dialog.show();
	}

}
