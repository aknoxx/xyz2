package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Local;

import dst2.ejb.util.NoPriceStepException;

@Local
public interface PriceManagementLocal {

	public BigDecimal getFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs) 
							throws NoPriceStepException;
	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs, BigDecimal price);
}
