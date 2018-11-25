package materialpurchasing.client.UI.util;

import java.util.Date;

import materialpurchasing.shared.component.BaseComponent;

public class ShoppingListItem {

	private BaseComponent component;
	private Integer amount;
	private Date deadLine;
	
	public ShoppingListItem(BaseComponent component, Integer amount, Date deadLine) {
		super();
		this.component = component;
		this.amount = amount;
		this.deadLine = deadLine;
	}
	
	public BaseComponent getComponent() {
		return component;
	}
	public void setComponent(BaseComponent component) {
		this.component = component;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}
}
