package materialpurchasing.shared.component;

public class BaseComponent {

	private String name;
	private Double price;
	private Integer acquireTime;

	public BaseComponent(String name, Double price, Integer acquireTime) {
		this.name = name;
		this.price = price;
		this.acquireTime = acquireTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getAcquireTime() {
		return acquireTime;
	}

	public void setAcquireTime(Integer acquireTime) {
		this.acquireTime = acquireTime;
	}

}
