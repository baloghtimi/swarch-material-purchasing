package materialpurchasing.client.UI;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ToStringValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

import materialpurchasing.client.controllers.BaseComponentController;
import materialpurchasing.client.controllers.ComplexComponentController;
import materialpurchasing.client.controllers.ProductController;
import materialpurchasing.client.events.ComplexComponentEvent;
import materialpurchasing.client.events.ProductEvent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.component.Component;
import materialpurchasing.shared.product.Product;

public class ProducerPage implements IsWidget, ComplexComponentEvent, ProductEvent {

	private String user;
	private static List<ComplexComponent> complexComponents = new ArrayList<>();
	private Grid<Component> complexComponentGrid;
	private Toolbar complexComponentToolbar;
	private ComplexComponent currentComplexComponent;
	private List<Product> products = new ArrayList<>();
	private Grid<Component> productGrid;
	private Toolbar productToolbar;
	private Product currentProduct;
	private List<Component> components = new ArrayList<>();

	public ProducerPage(String user) {
		ComplexComponentController.getInstance().addObserver(this);
		ProductController.getInstance().addObserver(this);
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
		tabPanel.add(createComplexComponentListTab(), "Complex components");
		tabPanel.add(createProductListTab(), "Products");
		container.add(tabPanel, tabBox);

		return container;
	}

	private void refreshComplexComponentList() {
		complexComponents = ComplexComponentController.getInstance().getCurrentComplexComponents();
		complexComponentGrid.getStore().replaceAll(complexComponents);
		complexComponentGrid.getView().refresh(true);
	}

	private void refreshProductList() {
		products = ProductController.getInstance().getCurrentProducts();
		productGrid.getStore().replaceAll(products);
		productGrid.getView().refresh(true);
	}

	private void refreshComponentList() {
		components = new ArrayList<>();
		components.addAll(BaseComponentController.getInstance().getCurrentBaseComponents());
		components.addAll(ComplexComponentController.getInstance().getCurrentComplexComponents());
	}

	private Widget createComplexComponentListTab() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData gridBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		gridBox.setFlex(1);

		createComplexComponentGrid();

		container.add(createComplexComponentToolbar(), toolbarBox);
		container.add(complexComponentGrid, gridBox);

		return container;
	}

	private Widget createProductListTab() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData gridBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		gridBox.setFlex(1);

		createProductGrid();

		container.add(createProductToolbar(), toolbarBox);
		container.add(productGrid, gridBox);

		return container;
	}

	class KeyProvider implements ModelKeyProvider<Component> {
		@Override
		public String getKey(Component item) {
			return (item instanceof Component ? "f-" : "m-") + item.getId().toString();
		}
	}

	private void createComplexComponentGrid() {
		TreeStore<Component> store = new TreeStore<Component>(new KeyProvider());

		complexComponents = ComplexComponentController.getInstance().getCurrentComplexComponents();
		for (ComplexComponent complexComponent : complexComponents) {
			store.add(complexComponent);
			for (Component component : complexComponent.getComponents()) {
				store.add(component);
				if (component instanceof ComplexComponent) {
					processFolder(store, (ComplexComponent) component);
				}
			}
		}

		ColumnConfig<Component, String> nameCol = new ColumnConfig<Component, String>(
				new ToStringValueProvider<Component>("name"));
		nameCol.setHeader("Name");

		List<ColumnConfig<Component, ?>> columns = new ArrayList<ColumnConfig<Component, ?>>();
		columns.add(nameCol);

		ColumnModel<Component> cm = new ColumnModel<Component>(columns);

		complexComponentGrid = new TreeGrid<Component>(store, cm, nameCol);
		complexComponentGrid.getView().setAutoExpandColumn(nameCol);
	}

	private void processFolder(TreeStore<Component> store, ComplexComponent folder) {
		for (Component child : folder.getComponents()) {
			store.add(folder, child);
			if (child instanceof ComplexComponent) {
				processFolder(store, (ComplexComponent) child);
			}
		}
	}

	private Toolbar createComplexComponentToolbar() {
		complexComponentToolbar = new Toolbar();

		SelectHandler refreshSelectHandler = createComplexComponentRefreshSelectionHandler();
		complexComponentToolbar.setRefreshButtonSelectHandler(refreshSelectHandler);

		SelectHandler addSelectHandler = createComplexComponentAddSelectionHandler();
		complexComponentToolbar.setAddButtonSelectHandler(addSelectHandler);

		SelectHandler modifySelectHandler = createComplexComponentModifySelectionHandler();
		complexComponentToolbar.setModifyButtonSelectHandler(modifySelectHandler);

		SelectHandler removeSelectHandler = createComplexComponentRemoveSelectionHandler();
		complexComponentToolbar.setRemoveButtonSelectHandler(removeSelectHandler);

		return complexComponentToolbar;
	}

	private SelectHandler createComplexComponentRefreshSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshComplexComponentList();
			}
		};

		return selectHandler;
	}

	private SelectHandler createComplexComponentAddSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final DualListField<Component, String> componentField = createComponentDualListFieldForAddition();
				container.add(new FieldLabel(componentField, "Components"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Adding new complex component");
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
						if (nameField.isValid() && !componentField.getToStore().getAll().isEmpty()) {
							ComplexComponentController.getInstance().addComplexComponent(nameField.getCurrentValue(),
									componentField.getToStore().getAll());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createComplexComponentModifySelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setValue(currentComplexComponent.getName());
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final DualListField<Component, String> componentField = createComplexComponentDualListFieldForModification();
				container.add(new FieldLabel(componentField, "Components"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Modifying complex component");
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
						if (nameField.isValid() && !componentField.getToStore().getAll().isEmpty()) {
							ComplexComponentController.getInstance().modifyComplexComponent(
									currentComplexComponent.getId(), nameField.getCurrentValue(),
									componentField.getToStore().getAll());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createComplexComponentRemoveSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final Dialog dialog = new Dialog();
				dialog.add(new Label("Are you sure you want to remove this complex component?"));
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
						ComplexComponentController.getInstance()
								.removeComplexComponent(currentComplexComponent.getId());
						dialog.hide();
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private DualListField<Component, String> createComponentDualListFieldForAddition() {
		ComponentProperties props = GWT.create(ComponentProperties.class);

		ListStore<Component> from = new ListStore<Component>(props.id());
		refreshComponentList();
		from.replaceAll(components);

		ListStore<Component> to = new ListStore<Component>(props.id());

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to, props.nameProp(),
				new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
	}

	private DualListField<Component, String> createComplexComponentDualListFieldForModification() {
		ComponentProperties props = GWT.create(ComponentProperties.class);

		ListStore<Component> to = new ListStore<Component>(props.id());
		to.addAll(currentComplexComponent.getComponents());

		ListStore<Component> from = new ListStore<Component>(props.id());
		refreshComponentList();
		components.removeAll(currentComplexComponent.getComponents());
		from.addAll(components);

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to, props.nameProp(),
				new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
	}

	private DualListField<Component, String> createProductComponentDualListFieldForModification() {
		ComponentProperties props = GWT.create(ComponentProperties.class);

		ListStore<Component> to = new ListStore<Component>(props.id());
		to.addAll(currentProduct.getComponents());

		ListStore<Component> from = new ListStore<Component>(props.id());
		refreshComponentList();
		components.removeAll(currentProduct.getComponents());
		from.addAll(components);

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to, props.nameProp(),
				new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
	}

	interface ComponentProperties extends PropertyAccess<Component> {
		ModelKeyProvider<Component> id();

		LabelProvider<Component> name();

		@Path("name")
		ValueProvider<Component, String> nameProp();
	}

	private void createProductGrid() {
		TreeStore<Component> store = new TreeStore<Component>(new KeyProvider());

		products = ProductController.getInstance().getCurrentProducts();
		for (Product product : products) {
			store.add(product);
			for (Component component : product.getComponents()) {
				store.add(component);
				if (component instanceof ComplexComponent) {
					processFolder(store, (ComplexComponent) component);
				}
			}
		}

		ColumnConfig<Component, String> nameCol = new ColumnConfig<Component, String>(
				new ToStringValueProvider<Component>("name"));
		nameCol.setHeader("Name");

		List<ColumnConfig<Component, ?>> columns = new ArrayList<ColumnConfig<Component, ?>>();
		columns.add(nameCol);

		ColumnModel<Component> cm = new ColumnModel<Component>(columns);

		productGrid = new TreeGrid<Component>(store, cm, nameCol);
		productGrid.getView().setAutoExpandColumn(nameCol);
	}

	private Toolbar createProductToolbar() {
		productToolbar = new Toolbar();

		SelectHandler refreshSelectHandler = createProductRefreshSelectionHandler();
		productToolbar.setRefreshButtonSelectHandler(refreshSelectHandler);

		SelectHandler addSelectHandler = createProductAddSelectionHandler();
		productToolbar.setAddButtonSelectHandler(addSelectHandler);

		SelectHandler modifySelectHandler = createProductModifySelectionHandler();
		productToolbar.setModifyButtonSelectHandler(modifySelectHandler);

		SelectHandler removeSelectHandler = createProductRemoveSelectionHandler();
		productToolbar.setRemoveButtonSelectHandler(removeSelectHandler);

		return productToolbar;
	}

	private SelectHandler createProductRefreshSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshProductList();
			}
		};

		return selectHandler;
	}

	private SelectHandler createProductAddSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final DualListField<Component, String> componentField = createComponentDualListFieldForAddition();
				container.add(new FieldLabel(componentField, "Components"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Adding new product");
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
						if (nameField.isValid() && !componentField.getToStore().getAll().isEmpty()) {
							ProductController.getInstance().addProduct(nameField.getCurrentValue(),
									componentField.getToStore().getAll());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createProductModifySelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCHMAX);
				BoxLayoutData boxLayoutData = new BoxLayoutData(new Margins(5, 5, 5, 5));

				final TextField nameField = new TextField();
				nameField.setValue(currentProduct.getName());
				nameField.setAllowBlank(false);
				container.add(new FieldLabel(nameField, "Name"), boxLayoutData);

				final DualListField<Component, String> componentField = createProductComponentDualListFieldForModification();
				container.add(new FieldLabel(componentField, "Components"), boxLayoutData);

				final Dialog dialog = new Dialog();
				dialog.setHeadingText("Modifying product");
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
						if (nameField.isValid() && !componentField.getToStore().getAll().isEmpty()) {
							ProductController.getInstance().modifyProduct(currentProduct.getId(),
									nameField.getCurrentValue(), componentField.getToStore().getAll());
							dialog.hide();
						}
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	private SelectHandler createProductRemoveSelectionHandler() {
		SelectHandler selectHandler = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final Dialog dialog = new Dialog();
				dialog.add(new Label("Are you sure you want to remove this product?"));
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
						ProductController.getInstance().removeProduct(currentComplexComponent.getId());
						dialog.hide();
					}
				});
				dialog.show();
			}
		};

		return selectHandler;
	}

	@Override
	public void productAddedEvent(Boolean result) {
		if (result) {
			showDialog("Product added successfully.");
		} else {
			showDialog("Failed to add product.");
		}
	}

	@Override
	public void productModifiedEvent(Boolean result) {
		if (result) {
			showDialog("Product modified successfully.");
		} else {
			showDialog("Failed to modify product.");
		}
	}

	@Override
	public void productRemovedEvent(Boolean result) {
		if (result) {
			showDialog("Product removed successfully.");
		} else {
			showDialog("Failed to remove product.");
		}
	}

	@Override
	public void complexComponentAdded(Boolean result) {
		if (result) {
			showDialog("Complex component added successfully.");
		} else {
			showDialog("Failed to add complex component.");
		}
	}

	@Override
	public void complexComponentModified(Boolean result) {
		if (result) {
			showDialog("Complex component modified successfully.");
		} else {
			showDialog("Failed to modify complex component.");
		}
	}

	@Override
	public void complexComponentRemoved(Boolean result) {
		if (result) {
			showDialog("Complex component removed successfully.");
		} else {
			showDialog("Failed to remove complex component.");
		}
	}

	private void showDialog(String label) {
		final Dialog dialog = new Dialog();
		dialog.setHeadingText("Message");
		dialog.setPredefinedButtons(PredefinedButton.OK);
		dialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshComplexComponentList();
				refreshProductList();
				dialog.hide();
			}
		});
		dialog.setClosable(false);
		dialog.setResizable(false);
		dialog.add(new Label(label));
		dialog.show();
	}

}
