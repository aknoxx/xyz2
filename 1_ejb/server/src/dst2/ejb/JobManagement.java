package dst2.ejb;

import java.util.List;

import javax.ejb.Remote;

import dst2.ejb.util.*;

@Remote
public interface JobManagement {

	public boolean login(String username, String password);
	public void addJobToGridTemporary(Long gridId, int numCPUs, String workflow, List<String> params) 
					throws ComputersNotAvailableTemporaryException, 
					InvalidGridIdException, UserNotLoggedInException;
	public void submitJobList() throws ComputersNotAvailableException,
										UserNotLoggedInException;
	public void removeTemporaryJobsFromGrid(Long gridId);
	public int getCurrentAmountOfTemporaryJobsByGrid(Long gridId);
	
}
