package dst2.ejb;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.model.Computer;
import dst2.ejb.model.Grid;
import dst2.ejb.model.Job;
import dst2.ejb.model.JobStatus;
import dst2.ejb.model.Membership;
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
		
		// TODO Asynchronously
		
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
			
			sb.append("\n");
			sb.append(" " + jobNumber + ". Job: ");
			sb.append("\n");
			sb.append("  Setup costs: ");
			query = em.createNamedQuery("findNumberOfPaidJobsByUser");
			query.setParameter("username", username);
			Long numberOfPaidJobs = (Long) query.getSingleResult();
			
			BigDecimal staticFee = pmb.getFeeForNumberOfHistoricalJobs(numberOfPaidJobs);
			
			double discount = 1;
			for (Membership membership : user.getMemberships()) {
				if(membership.getGrid().getId() == gridOfJob.getId()) {
					discount = membership.getDiscount();
					break;
				}
			}			
			
			// TODO round
			staticFee = staticFee.multiply(new BigDecimal(discount));
			totalPrice.add(staticFee);
			sb.append(staticFee);
			
			sb.append("\n");
			sb.append("  Execution costs: ");
			BigDecimal costsPerCPUMinute = gridOfJob.getCostPerCPUMinute();
			
			int executionTimeInSeconds = job.getExecutionTime();
			
			BigDecimal executionCosts = 
				new BigDecimal(executionTimeInSeconds).divide(new BigDecimal(60))
					.multiply(costsPerCPUMinute)
					.multiply(new BigDecimal(job.getNumCPUs()));
			
			totalPrice.add(executionCosts);
			sb.append(executionCosts);
			
			sb.append("\n");
			sb.append("  Number of computers: ");
			sb.append(job.getExecution().getComputers().size());
			
			// persist job payment
			Job paidJob = em.find(Job.class, job.getId());
			paidJob.setPaid(true);
			em.merge(paidJob);
			
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
}
