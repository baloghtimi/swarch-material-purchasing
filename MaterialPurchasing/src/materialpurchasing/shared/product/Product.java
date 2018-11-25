package materialpurchasing.shared.product;

import java.io.Serializable;
import java.util.List;

import materialpurchasing.shared.component.Component;

public class Product extends Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3252013418913934107L;
	private Long id;
	private String name;

	private List<Component> components;

	protected Product() {

	}

	public Product(Long id, String name, List<Component> components) {
		super();
		this.id = id;
		this.name = name;
		this.components = components;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return name;
	}
}
