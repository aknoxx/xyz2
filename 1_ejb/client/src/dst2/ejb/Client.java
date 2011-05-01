package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst2.ejb.util.*;
import dst2.ejb.dto.*;

public class Client {
	
	private Scanner in = new Scanner(System.in);
	
	private Testing testing;
	private GeneralManagement generalManagement;
	private JobManagement jobManagement;

    public Client(String[] args) {
    	
    	InitialContext ctx = null;
        try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Looking up Beans...");
		
		try {
			testing = (Testing)ctx.lookup("java:global/dst2_1/TestingBean!dst2.ejb.Testing");
			generalManagement = (GeneralManagement)ctx
					.lookup("java:global/dst2_1/GeneralManagementBean!dst2.ejb.GeneralManagement");
			jobManagement = (JobManagement)ctx
					.lookup("java:global/dst2_1/JobManagementBean!dst2.ejb.JobManagement");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Insert testing data: <ENTER>");		
		in.nextLine();
		testing.insertData();
        
        System.out.println("Set some prices: <ENTER>");		
		in.nextLine();
		generalManagement.setFeeForNumberOfHistoricalJobs(1, new BigDecimal(30));
		generalManagement.setFeeForNumberOfHistoricalJobs(5, new BigDecimal(15));
		generalManagement.setFeeForNumberOfHistoricalJobs(10, new BigDecimal(5));
		// update step
		generalManagement.setFeeForNumberOfHistoricalJobs(10, new BigDecimal(7));
        
		System.out.println("Invalid login: <ENTER>");		
		in.nextLine();
		if(!jobManagement.login("username1", "wrongPassword")) {
			System.out.println("Ok: Login rejected.");
		}
		else {
			System.out.println("ERROR");
		}
		
		System.out.println("Valid login user1: <ENTER>");		
		in.nextLine();
		if(jobManagement.login("username1", "password1")) {
			System.out.println("Ok: Login successfull.");
		}
		else {
			System.out.println("ERROR");
		}
		
		System.out.println("Add valid job assignments: <ENTER>");		
		in.nextLine();
		List<String> params = new ArrayList<String>();
		params.add("param1");
		params.add("param2");
		params.add("param3");
		
		try {
			jobManagement.addJobToGridTemporary(new Long(1), 1, "workflow2", params);
			jobManagement.addJobToGridTemporary(new Long(1), 1, "workflow3", params);
			jobManagement.addJobToGridTemporary(new Long(1), 1, "workflow4", params);
		} catch (InvalidGridIdException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (UserNotLoggedInException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (ComputersNotAvailableTemporaryException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		}
		
		System.out.println("Request current assigned amount of jobs of grid 1: <ENTER>");
		in.nextLine();
		System.out.println("Jobs assigned to grid 1: " 
				+ jobManagement.getCurrentAmountOfTemporaryJobsByGrid(new Long(1)));		
		
		System.out.println("Successfully submit temporary job list: <ENTER>");
		in.nextLine();
		try {
			jobManagement.submitJobList();
		} catch (ComputersNotAvailableException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (UserNotLoggedInException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		}
		
		// Login second user
		try {
			jobManagement = (JobManagement)ctx
					.lookup("java:global/dst2_1/JobManagementBean!dst2.ejb.JobManagement");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Valid login with user2: <ENTER>");		
		in.nextLine();
		if(jobManagement.login("username2", "password2")) {
			System.out.println("Ok: Login successfull.");
		}
		else {
			System.out.println("ERROR");
		}
		
		System.out.println("Add job assignments to grids (too much for grid 2): <ENTER>");		
		in.nextLine();
		try {
			jobManagement.addJobToGridTemporary(new Long(1), 1, "workflow5", params);
			
			// 6 jobs, one too much
			jobManagement.addJobToGridTemporary(new Long(2), 2, "workflow6", params);
			jobManagement.addJobToGridTemporary(new Long(2), 4, "workflow7", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow8", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow9", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow10", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow11", params);
		} catch (InvalidGridIdException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (UserNotLoggedInException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (ComputersNotAvailableTemporaryException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		}
		
		System.out.println("Delete job assignments of grid 2: <ENTER>");		
		in.nextLine();
		jobManagement.removeTemporaryJobsFromGrid(new Long(2));
		
		System.out.println("Request current assigned amount of jobs of grids: <ENTER>");
		in.nextLine();
		System.out.println("Jobs assigned to grid 1: " 
				+ jobManagement.getCurrentAmountOfTemporaryJobsByGrid(new Long(1)));
		System.out.println("Jobs assigned to grid 2: " 
				+ jobManagement.getCurrentAmountOfTemporaryJobsByGrid(new Long(2)));	
		
		System.out.println("Add jobs another time: <ENTER>");		
		in.nextLine();
		try {
			jobManagement.addJobToGridTemporary(new Long(2), 2, "workflow6", params);
			jobManagement.addJobToGridTemporary(new Long(2), 4, "workflow7", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow8", params);
			jobManagement.addJobToGridTemporary(new Long(2), 6, "workflow9", params);
		} catch (InvalidGridIdException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (UserNotLoggedInException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (ComputersNotAvailableTemporaryException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		}
		
		System.out.println("Request current assigned amount of jobs of grids: <ENTER>");
		in.nextLine();
		System.out.println("Jobs assigned to grid 1: " 
				+ jobManagement.getCurrentAmountOfTemporaryJobsByGrid(new Long(1)));
		System.out.println("Jobs assigned to grid 2: " 
				+ jobManagement.getCurrentAmountOfTemporaryJobsByGrid(new Long(2)));
		
		System.out.println("Successfully submit temporary job list: <ENTER>");
		in.nextLine();
		try {
			jobManagement.submitJobList();
		} catch (ComputersNotAvailableException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (UserNotLoggedInException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		}
		
		System.out.println("Waiting 3 seconds for timer to complete executions...");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Getting Bill for all jobs: <ENTER>");
        in.nextLine();
        
        try {
        	System.out.println("User1 with username1:");
        	Future<String> bill = generalManagement.getTotalBillByUser("username1");
        	/*while(!bill.isDone()) { // glassfish bug
        		Thread.sleep(1000);
        	}*/
        	Thread.sleep(1000);
			System.out.println(bill.get());
			System.out.println("User2 with username2:");
			bill = generalManagement.getTotalBillByUser("username2");
        	/*while(!bill.isDone()) { // glassfish bug
        		Thread.sleep(1000);
        	}*/
			Thread.sleep(1000);
        	System.out.println(bill.get());
		} catch (InterruptedException e) {
			System.out.println("Error while retrieving bill." + e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("Error while retrieving bill." + e.getMessage());
			e.printStackTrace();
		} catch (NoPriceStepException e) {
			System.out.println("Custom exception: " + e.getMessage());
		}        
		
        System.out.println("Get all saved audits: <ENTER>");
        in.nextLine();
        AuditListDto audits = generalManagement.getAudits();
        int i = 1;
        StringBuilder sb = new StringBuilder();
        for (AuditDto a : audits.audits) {
			sb.append(i+ ". ");
			sb.append(a.invocationTime + ", ");
			sb.append(a.methodName + ", ");			
			sb.append(a.resultValue);
			sb.append("\n   Params: \n");
			
			for (ParamDto p : a.parameters) {
				sb.append("   " + p.paramIndex + ", ");
				sb.append(p.className + ", ");
				sb.append(p.value + ", ");
				sb.append("\n");
			}
			sb.append("\n");			
			
			i++;
		}
        System.out.println(sb.toString());
        
        testing.remove();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = new Client(args);
    }
}
