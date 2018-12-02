package materialpurchasing.client.UI;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import materialpurchasing.client.controllers.BaseComponentController;
import materialpurchasing.client.controllers.ComplexComponentController;
import materialpurchasing.client.controllers.ProductController;
import materialpurchasing.client.events.ComplexComponentEvent;
import materialpurchasing.client.events.ProductEvent;
import materialpurchasing.shared.component.ComplexComponent;
import materialpurchasing.shared.component.Component;
import materialpurchasing.shared.product.Product;

public class ProducerPage implements IsWidget, ComplexComponentEvent, ProductEvent {

	private VBoxLayoutContainer widget;

	private String user;

	private ComplexComponent currentComplexComponent = null;
	private Product currentProduct = null;
	
	private Toolbar complexComponentToolbar;
	private Toolbar productToolbar;

	private Widget complexComponentDetailPanel = refreshComplexComponentDetailPanel();
	private Widget productDetailPanel = refreshProductDetailPanel();
	private Dialog messageDialog = new Dialog();

	public ProducerPage(String user) {
		ComplexComponentController.getInstance().addObserver(this);
		ProductController.getInstance().addObserver(this);
		this.user = user;
	}

	@Override
	public Widget asWidget() {
		refreshWidget();
		return widget;
	}

	private void refreshWidget() {
		widget = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);

		BoxLayoutData userBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		UserPanel userPanel = new UserPanel(user);
		widget.add(userPanel, userBox);

		BoxLayoutData tabBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		tabBox.setFlex(1);
		TabPanel tabPanel = new TabPanel();
		tabPanel.setBorders(true);
		tabPanel.add(createComplexComponentListTab(), "Complex components");
		tabPanel.add(createProductListTab(), "Products");
		widget.add(tabPanel, tabBox);
	}

	private Widget refreshComplexComponentDetailPanel() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		BoxLayoutData nameBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData listBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		listBox.setFlex(1);

		TextField name = new TextField();
		if (currentComplexComponent != null) {
			name.setValue(currentComplexComponent.getName());
		}

		ListStore<Component> components = new ListStore<Component>(componentProps.id());
		if (currentComplexComponent != null) {
			components.addAll(currentComplexComponent.getComponents());
		}
		ListView<Component, String> listView = new ListView<Component, String>(components, componentProps.nameProp());
		ListField<Component, String> listField = new ListField<Component, String>(listView);

		container.add(new FieldLabel(name, "Name"), nameBox);
		container.add(new FieldLabel(listField, "Components"), listBox);
		
		return container;
	}
	
	private Widget refreshProductDetailPanel() {
		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		BoxLayoutData nameBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData listBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		listBox.setFlex(1);

		TextField name = new TextField();
		if (currentProduct != null) {
			name.setValue(currentProduct.getName());
		}

		ListStore<Component> components = new ListStore<Component>(componentProps.id());
		if (currentProduct != null) {
			components.addAll(currentProduct.getComponents());
		}
		ListView<Component, String> listView = new ListView<Component, String>(components, componentProps.nameProp());
		ListField<Component, String> listField = new ListField<Component, String>(listView);

		container.add(new FieldLabel(name, "Name"), nameBox);
		container.add(new FieldLabel(listField, "Components"), listBox);
		
		return container;
	}

	private Widget createComplexComponentListTab() {
		HBoxLayoutContainer listContainer = new HBoxLayoutContainer(HBoxLayoutAlign.STRETCH);
		BoxLayoutData compListBox = new BoxLayoutData(new Margins(10, 5, 10, 10));
		BoxLayoutData detailBox = new BoxLayoutData(new Margins(10, 10, 10, 5));
		detailBox.setFlex(1);
		listContainer.add(createComplexComponentList(), compListBox);
		listContainer.add(refreshComplexComponentDetailPanel(), detailBox);

		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData listBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		listBox.setFlex(1);
		container.add(createComplexComponentToolbar(), toolbarBox);
		container.add(listContainer, listBox);

		return container;
	}

	interface ComponentProperties extends PropertyAccess<Component> {
		ModelKeyProvider<Component> id();

		LabelProvider<Component> name();

		@Path("name")
		ValueProvider<Component, String> nameProp();
	}

	public static ComponentProperties componentProps = GWT.create(ComponentProperties.class);

	interface ComplexComponentProperties extends PropertyAccess<ComplexComponent> {
		ModelKeyProvider<ComplexComponent> id();

		ValueProvider<Component, String> name();
	}

	interface ProductProperties extends PropertyAccess<Product> {
		ModelKeyProvider<Product> id();
		ValueProvider<Component, String> name();
	}

	private Widget createComplexComponentList() {
		ComplexComponentProperties properties = GWT.create(ComplexComponentProperties.class);
		ListStore<ComplexComponent> complexComps = new ListStore<ComplexComponent>(properties.id());
		complexComps.addAll(ComplexComponentController.getInstance().getCurrentComplexComponents());

		final ListView<ComplexComponent, String> listView = new ListView<ComplexComponent, String>(complexComps,
				properties.name());
		listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ComplexComponent>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<ComplexComponent> se) {
				if (listView.getSelectionModel().getSelectedItem() != null) {
					complexComponentToolbar.setModificationMode(true);
					currentComplexComponent = listView.getSelectionModel().getSelectedItem();
				}
			}
		});

		ListField<ComplexComponent, String> listField = new ListField<ComplexComponent, String>(listView);
		
		FieldLabel fieldLabel = new FieldLabel(listField, "Complex components");
		
		return fieldLabel;
	}

	private Widget createProductListTab() {
		HBoxLayoutContainer listContainer = new HBoxLayoutContainer(HBoxLayoutAlign.STRETCH);
		BoxLayoutData productListBox = new BoxLayoutData(new Margins(10, 5, 10, 10));
		BoxLayoutData detailBox = new BoxLayoutData(new Margins(10, 10, 10, 5));
		detailBox.setFlex(1);
		listContainer.add(createProductList(), productListBox);
		listContainer.add(refreshProductDetailPanel(), detailBox);

		VBoxLayoutContainer container = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		BoxLayoutData toolbarBox = new BoxLayoutData(new Margins(10, 10, 5, 10));
		BoxLayoutData listBox = new BoxLayoutData(new Margins(5, 10, 10, 10));
		listBox.setFlex(1);
		container.add(createProductToolbar(), toolbarBox);
		container.add(listContainer, listBox);

		return container;
	}

	private Widget createProductList() {
		ProductProperties properties = GWT.create(ProductProperties.class);
		ListStore<Product> products = new ListStore<Product>(properties.id());
		products.addAll(ProductController.getInstance().getCurrentProducts());

		final ListView<Product, String> listView = new ListView<Product, String>(products, properties.name());
		listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Product>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<Product> se) {
				if (listView.getSelectionModel().getSelectedItem() != null) {
					productToolbar.setModificationMode(true);
					currentProduct = listView.getSelectionModel().getSelectedItem();
				}
			}
		});

		ListField<Product, String> listField = new ListField<Product, String>(listView);
		
		FieldLabel fieldLabel = new FieldLabel(listField, "Products");
		
		return fieldLabel;
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
				refreshWidget();
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
		ListStore<Component> from = new ListStore<Component>(componentProps.id());
		List <Component> components = new ArrayList<>();
		components.addAll(BaseComponentController.getInstance().getCurrentBaseComponents());
		components.addAll(ComplexComponentController.getInstance().getCurrentComplexComponents());
		from.addAll(components);

		ListStore<Component> to = new ListStore<Component>(componentProps.id());

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to,
				componentProps.nameProp(), new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
	}

	private DualListField<Component, String> createComplexComponentDualListFieldForModification() {
		ListStore<Component> to = new ListStore<Component>(componentProps.id());
		to.addAll(currentComplexComponent.getComponents());

		ListStore<Component> from = new ListStore<Component>(componentProps.id());
		List <Component> components = new ArrayList<>();
		components.addAll(BaseComponentController.getInstance().getCurrentBaseComponents());
		components.addAll(ComplexComponentController.getInstance().getCurrentComplexComponents());
		components.removeAll(currentComplexComponent.getComponents());
		from.addAll(components);

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to,
				componentProps.nameProp(), new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
	}

	private DualListField<Component, String> createProductComponentDualListFieldForModification() {
		ListStore<Component> to = new ListStore<Component>(componentProps.id());
		to.addAll(currentProduct.getComponents());

		ListStore<Component> from = new ListStore<Component>(componentProps.id());
		List <Component> components = new ArrayList<>();
		components.addAll(BaseComponentController.getInstance().getCurrentBaseComponents());
		components.addAll(ComplexComponentController.getInstance().getCurrentComplexComponents());
		components.removeAll(currentComplexComponent.getComponents());
		from.addAll(components);

		final DualListField<Component, String> field = new DualListField<Component, String>(from, to,
				componentProps.nameProp(), new TextCell());
		field.setEnableDnd(true);
		field.setMode(Mode.INSERT);

		return field;
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
				refreshWidget();
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
		messageDialog.clear();
		messageDialog.setHeadingText("Message");
		messageDialog.setPredefinedButtons(PredefinedButton.OK);
		messageDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				refreshWidget();
				refreshComplexComponentDetailPanel();
				refreshProductDetailPanel();
				messageDialog.hide();
			}
		});
		messageDialog.setClosable(false);
		messageDialog.setResizable(false);
		messageDialog.add(new Label(label));
		messageDialog.show();
	}

}
