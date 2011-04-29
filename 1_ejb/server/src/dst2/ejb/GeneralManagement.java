package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;

import dst2.ejb.util.NoPriceStepException;

@Remote
public interface GeneralManagement {

	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs, BigDecimal price);
	
	public String getTotalBillByUser(String username) throws NoPriceStepException;
}
