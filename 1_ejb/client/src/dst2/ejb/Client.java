package dst2.ejb;

import java.math.BigDecimal;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {
	
	private Scanner in = new Scanner(System.in);
	
	private Testing testing;
	//private GeneralManagement generalManagement;

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
			//generalManagement = 
			//	(GeneralManagement)ctx.lookup("java:global/dst2_1/GeneralManagement");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Insert testing data: <ENTER>");		
		in.nextLine();
		testing.insertData();
        
        System.out.println("Set some prices: <ENTER>");		
		in.nextLine();
		//generalManagement.setFeeForNumberOfHistoricalJobs(1, new BigDecimal(30));
		//generalManagement.setFeeForNumberOfHistoricalJobs(5, new BigDecimal(15));
		//generalManagement.setFeeForNumberOfHistoricalJobs(10, new BigDecimal(5));
        
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
