package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst2.ejb.util.ComputersNotAvailableException;
import dst2.ejb.util.InvalidGridIdException;
import dst2.ejb.util.UserNotLoggedInException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Try to get BEAN: ");
		
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
        
		System.out.println("Invalid login: <ENTER>");		
		in.nextLine();
		if(!jobManagement.login("username1", "wrongPassword")) {
			System.out.println("Ok: Login rejected.");
		}
		else {
			System.out.println("ERROR");
		}
		
		System.out.println("Valid login: <ENTER>");		
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
			jobManagement.addJobToGridTemporary(new Long(1), 5, "workflow1", params);
		} catch (ComputersNotAvailableException e) {
			System.out.println("Custom Exception: " + e.getMessage());
		} catch (InvalidGridIdException e) {
			e.printStackTrace();
		} catch (UserNotLoggedInException e) {
			e.printStackTrace();
		}
		
        System.out.println("Finish: <ENTER>");
        in.nextLine();
        testing.remove();
        
        System.out.println("Client: all fine.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = new Client(args);
    }
}
