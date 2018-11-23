package materialpurchasing.shared.component;

import java.io.Serializable;

public class BaseComponent extends Component implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6400213413501876005L;
	private Integer price;
	private Integer purchaseTime;
	
	protected BaseComponent() {
		
	}
	
	public BaseComponent(Long id, String name,Integer price, Integer purchaseTime) {
		super(id, name);
		this.price = price;
		this.purchaseTime = purchaseTime;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(Integer purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	
	
}
