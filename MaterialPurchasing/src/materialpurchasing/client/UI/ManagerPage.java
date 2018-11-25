package materialpurchasing.client.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.DatePicker;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import materialpurchasing.client.controllers.BaseComponentController;
import materialpurchasing.client.controllers.ProductController;
import materialpurchasing.client.controllers.ProductionPlanController;
import materialpurchasing.client.events.ProductionPlanEvent;
import materialpurchasing.shared.product.Product;
import materialpurchasing.shared.product.ProductionPlan;

public class ManagerPage implements IsWidget, ProductionPlanEvent {

	private String user;
	private List<ProductionPlan> ProductionPlans = new ArrayList<>();
	private Grid<ProductionPlan> grid;
	private ProductionPlan currentProductionPlan;
	private Toolbar toolbar;

	public ManagerPage(String user) {
		ProductionPlanController.getInstance().addObserver(this);
		this.user = user;
	}

	@Override
	public Widget asWidget() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData userBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		UserPanel userPanel = new UserPanel(user);
		container.add(userPanel, userBox);

		BoxLayoutData ProductionPlanBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		ProductionPlanBox.setFlex(1);
		container.add(createProductionPlanPanel(), ProductionPlanBox);

		return container;
	}

	private void refreshProductionPlanList() {
		ProductionPlans = ProductionPlanController.getInstance().getCurrentProductionPlans();
		grid.getStore().replaceAll(ProductionPlans);
		grid.getView().refresh(true);
	}

	private Widget createProductionPlanPanel() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData gridBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		gridBox.setFlex(1);

		createProductionPlanGrid();

		container.add(createToolbar(), toolbarBox);
		container.add(grid, gridBox);

		FramedPanel panel = new FramedPanel();
		panel.setHeadingText("ProductionPlanion plans");
		panel.add(container);
		panel.setBorders(true);
		return panel;
	}

	private void createProductionPlanGrid() {
		ListStore<ProductionPlan> listStore = new ListStore<ProductionPlan>(gridProperties.id());
		ProductionPlans = ProductionPlanController.getInstance().getCurrentProductionPlans();
		listStore.addAll(ProductionPlans);

		ColumnConfig<ProductionPlan, String> nameColumn = new ColumnConfig<ProductionPlan, String>(
				gridProperties.name(), 200, "Name");
		ColumnConfig<ProductionPlan, Integer> amountColumn = new ColumnConfig<ProductionPlan, Integer>(
				gridProperties.amount(), 200, "Amount");
		ColumnConfig<ProductionPlan, Date> deadlineColumn = new ColumnConfig<ProductionPlan, Date>(
				gridProperties.deadline(), 200, "Deadline");

		List<ColumnConfig<ProductionPlan, ?>> columns = new ArrayList<ColumnConfig<ProductionPlan, ?>>();
		columns.add(nameColumn);
		columns.add(amountColumn);
		columns.add(deadlineColumn);
		ColumnModel<ProductionPlan> columnModel = new ColumnModel<ProductionPlan>(columns);

		GridView<ProductionPlan> gridView = new GridView<ProductionPlan>();
		gridView.setAutoFill(true);
//		gridView.setAutoExpandColumn(nameColumn);

		grid = new Grid<ProductionPlan>(listStore, columnModel, gridView);
		grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ProductionPlan>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<ProductionPlan> se) {
				if (grid.getSelectionModel().getSelectedItem() != null) {
					toolbar.setModificationMode(true);
					currentProductionPlan = grid.getSelectionModel().getSelectedItem();
				}
			}
		});
		grid.setBorders(true);
	}

	public interface GridProperties extends PropertyAccess<ProductionPlan> {
		ModelKeyProvider<ProductionPlan> id();

		ValueProvider<ProductionPlan, String> name();

		ValueProvider<ProductionPlan, Integer> amount();

		ValueProvider<ProductionPlan, Date> deadline();
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
				refreshProductionPlanList();
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

				final ComboBox<Product> products = createProductComboBox();
				products.setAllowBlank(false);
				container.add(new FieldLabel(products, "Product"), boxLayoutData);

				final IntegerField amountField = new IntegerField();
				amountField.setAllowBlank(false);
				container.add(new FieldLabel(amountField, "Amount"), boxLayoutData);

				final DatePicker deadlineField = new DatePicker();
				deadlineField.setMinDate(new DateWrapper().addDays(-5).asDate());
				container.add(new FieldLabel(deadlineField, "Deadline"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Adding new product plan");
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
						if (products.isValid() && amountField.isValid() && deadlineField.getValue() != null) {
							ProductionPlanController.getInstance().addBaseComponent(products.getCurrentValue(),
									amountField.getCurrentValue(), deadlineField.getValue());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private ComboBox<Product> createProductComboBox() {
		ListStore<Product> store = new ListStore<Product>(new ModelKeyProvider<Product>() {
			@Override
			public String getKey(Product item) {
				return item.getId().toString();
			}
		});
		store.replaceAll(ProductController.getInstance().getCurrentProducts());

		LabelProvider<Product> labelProvider = new LabelProvider<Product>() {
			@Override
			public String getLabel(Product item) {
				if (item.getName() == null) {
					return "";
				}
				return item.getName();
			}
		};

		ComboBox<Product> combo = new ComboBox<Product>(store, labelProvider);
	}

	private SelectHandler createModifySelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final ComboBox<Product> products = createProductComboBox();
				products.setValue(currentProductionPlan.getProduct());
				products.setAllowBlank(false);
				container.add(new FieldLabel(products, "Product"), boxLayoutData);

				final IntegerField amountField = new IntegerField();
				amountField.setValue(currentProductionPlan.getAmount());
				amountField.setAllowBlank(false);
				container.add(new FieldLabel(amountField, "Amount"), boxLayoutData);

				final DatePicker deadlineField = new DatePicker();
				deadlineField.setValue(currentProductionPlan.getDeadline());
				deadlineField.setMinDate(new DateWrapper().addDays(-5).asDate());
				container.add(new FieldLabel(deadlineField, "Deadline"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Modifying product plan");
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
						if (products.isValid() && amountField.isValid() && deadlineField.getValue() != null) {
							ProductionPlanController.getInstance().modifyProductPlan(currentProductionPlan.getId(),
									products.getCurrentValue(), amountField.getCurrentValue(),
									deadlineField.getValue());
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
				dialog.add(new Label("Are you sure you want to remove this product plan?"));
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
						ProductionPlanController.getInstance().removeBaseComponent(currentProductionPlan.getId());
						dialog.hide();
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private void showDialog(String label) {
		final Dialog dialog = new Dialog();
		dialog.setHeadingText("Message");
		dialog.setPredefinedButtons(PredefinedButton.OK);
		dialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshProductionPlanList();
				dialog.hide();
			}
		});
		dialog.setClosable(false);
		dialog.setResizable(false);
		dialog.add(new Label(label));
		dialog.show();
	}
}
