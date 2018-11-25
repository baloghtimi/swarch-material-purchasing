package materialpurchasing.shared.product;

import java.io.Serializable;
import java.util.Date;

public class ProductionPlan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4099377511417046849L;

	private Long id=Long.MIN_VALUE;
	
	private Product product;
	private Integer amount;
	private Date deadline;
	
	
	protected ProductionPlan() {
		
	}
	
	public ProductionPlan(Long id, Product product, Integer amount, Date deadline) {
		super();
		this.id = id;
		this.product = product;
		this.amount = amount;
		this.deadline = deadline;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	
}
