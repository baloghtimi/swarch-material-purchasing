package materialpurchasing.shared.product;

import java.util.Date;

public class ProductionPlan {
	
	private Product product;
	private Integer amount;
	private Date deadline;
	public Product getProduct() {
		return product;
	}
	public Integer getAmount() {
		return amount;
	}
	public Date getDeadline() {
		return deadline;
	}
}
