package dst2.ejb;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import javax.ejb.Remote;

import dst2.ejb.dto.AuditListDto;
import dst2.ejb.util.NoPriceStepException;

@Remote
public interface GeneralManagement {

	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs, BigDecimal price);
	
	public Future<String> getTotalBillByUser(String username) throws NoPriceStepException;
	
	public AuditListDto getAudits();
}
