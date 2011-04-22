package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class GeneralManagementBean implements GeneralManagement {

	@EJB
	PriceManagementLocal pmb;
	
	@Override
	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs,
			BigDecimal price) {
		pmb.setFeeForNumberOfHistoricalJobs(numberOfHistoricalJobs, price);
	}

	@Override
	public String getTotalBillByUser(String username) {
		
		// TODO Asynchronously
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Total price: ");
		sb.append("\n");
		
		sb.append("Price per job: ");
		sb.append("\n");
		
			sb.append("Setup cost: ");
			sb.append("\n");
			sb.append("Execution cost: ");
			sb.append("\n");
			sb.append("Number of computers: ");
			sb.append("\n");		
		
		return sb.toString();
	}
}
