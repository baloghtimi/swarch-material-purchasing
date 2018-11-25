package materialpurchasing.shared.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComplexComponent extends Component implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8725499295912208803L;
	private List<Component> components;
	
	protected ComplexComponent() {
		
	}

	public ComplexComponent(Long id, String name) {
		super(id, name);
		this.components = new ArrayList<Component>();
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
	public void addComponent(Component c) {
		components.add(c);
	}
}
