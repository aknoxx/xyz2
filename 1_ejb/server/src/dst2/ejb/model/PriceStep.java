package dst2.ejb.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PriceStep implements Serializable {
	
	private static final long serialVersionUID = 3542643164933510683L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private int numberOfHistoricalJobs;
	@Column(nullable=false)
	private BigDecimal price;
	
	public PriceStep() {
		super();
	}

	public PriceStep(int numberOfHistoricalJobs, BigDecimal price) {
		super();
		this.numberOfHistoricalJobs = numberOfHistoricalJobs;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumberOfHistoricalJobs() {
		return numberOfHistoricalJobs;
	}

	public void setNumberOfHistoricalJobs(int numberOfHistoricalJobs) {
		this.numberOfHistoricalJobs = numberOfHistoricalJobs;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}	
}
