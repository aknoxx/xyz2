package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.dto.*;
import dst2.ejb.model.Audit;
import dst2.ejb.model.Grid;
import dst2.ejb.model.Job;
import dst2.ejb.model.JobStatus;
import dst2.ejb.model.Membership;
import dst2.ejb.model.PA;
import dst2.ejb.model.User;
import dst2.ejb.util.NoPriceStepException;

@Stateless
public class GeneralManagementBean implements GeneralManagement {

	@EJB
	PriceManagementLocal pmb;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs,
			BigDecimal price) {
		pmb.setFeeForNumberOfHistoricalJobs(numberOfHistoricalJobs, price);
	}

	@Asynchronous
	@Override
	public Future<String> getTotalBillByUser(String username) throws NoPriceStepException {
		
		Query query = em.createNamedQuery("findUserByName");
		query.setParameter("username", username);		
		User user = (User) query.getSingleResult();
		
		StringBuilder sb = new StringBuilder();
		
		query = em.createNamedQuery("findUnpaidFinishedJobsByUser");
		query.setParameter("username", username);
		query.setParameter("statusFinished", JobStatus.FINISHED);
		List<Job> finishedJobs = query.getResultList();		
		
		sb.append("Price per job: ");
		sb.append("\n");
		sb.append("--------------");
		sb.append("\n");
		
		BigDecimal totalPrice = new BigDecimal(0);
		int jobNumber = 1;
		for (Job job : finishedJobs) {
			
			Grid gridOfJob = job.getExecution().getComputers().get(0)
									.getCluster().getGrid();
			BigDecimal jobPrice = new BigDecimal(0);
			
			sb.append("\n");
			sb.append(" " + jobNumber + ". Job: ");
			sb.append("\n");
			sb.append("  Setup costs: ");
			query = em.createNamedQuery("findNumberOfPaidJobsByUser");
			query.setParameter("username", username);
			Long numberOfPaidJobs = (Long) query.getSingleResult();
			
			BigDecimal staticFee = pmb.getFeeForNumberOfHistoricalJobs(numberOfPaidJobs);		
			
			jobPrice = jobPrice.add(staticFee);
			sb.append(staticFee);
			
			sb.append("\n");
			sb.append("  Execution costs: ");
			BigDecimal costsPerCPUSecond = gridOfJob.getCostPerCPUMinute();//.divide(new BigDecimal(60));
			
			int executionTimeInSeconds = job.getExecutionTime();
			
			BigDecimal executionCosts = 
				new BigDecimal(executionTimeInSeconds)
					.multiply(costsPerCPUSecond)
					.multiply(new BigDecimal(job.getNumCPUs()))
					.divide(new BigDecimal(60))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			jobPrice = jobPrice.add(executionCosts);
			sb.append(executionCosts);
			
			double discount = 0; // discount 0%
			for (Membership membership : user.getMemberships()) {
				if(membership.getGrid().getId() == gridOfJob.getId()) {
					discount = membership.getDiscount();
					break;
				}
			}
			
			sb.append("\n");
			sb.append("  Discount: ");
			BigDecimal dc = jobPrice.multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
			jobPrice = jobPrice.subtract(dc);
			sb.append("-" + dc);			
			
			sb.append("\n");
			sb.append("  Number of computers: ");
			sb.append(job.getExecution().getComputers().size());
			
			// persist job payment
			Job paidJob = em.find(Job.class, job.getId());
			paidJob.setPaid(true);
			em.merge(paidJob);
			
			totalPrice = totalPrice.add(jobPrice);
			jobNumber++;
		}		
		
		sb.append("\n");
		sb.append("--------------");
		sb.append("\n");
		sb.append("Total price: ");
		sb.append(totalPrice);	
		sb.append("\n");
		sb.append("\n");
		
		return new AsyncResult<String>(sb.toString());
	}

	@Override
	public AuditListDto getAudits() {
		
		Query query = em.createNamedQuery("findAllAudits");
		List<Audit> audits = query.getResultList();

		List<AuditDto> auditDtos = new ArrayList<AuditDto>();
		for (Audit a : audits) {
			List<PA> parameters = a.getParameters();
			List<ParamDto> paramDtos = new ArrayList<ParamDto>();
			for (PA p : parameters) {
				paramDtos.add(new ParamDto(p.getIndex(), p.getClassName(), p.getValue()));
			}
			AuditDto auditDto = new AuditDto(a.getInvocationTime(), a.getMethodName(), 
					paramDtos, a.getResultValue());
			auditDtos.add(auditDto);
		}	
		return new AuditListDto(auditDtos);		
	}
}
