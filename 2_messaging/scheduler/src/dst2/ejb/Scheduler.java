package dst2.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.annotation.Resource;

import dst2.ejb.dto.AssignJobDto;

public class Scheduler {

    private static ConnectionFactory connectionFactory;
    private static Queue queue;
    
	private static BufferedReader stdIn;
	
	public static void main(String[] args) {
	
		InitialContext ctx = null;
        try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		try {
			connectionFactory = (ConnectionFactory)ctx.lookup("dst.Factory");
			queue = (Queue)ctx.lookup("queue.dst.MyQueue");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		
		//Destination dest = (Destination) queue;
		Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        TextMessage message = null;
        
        ObjectMessage oMsg = null;

		try {

			connection = connectionFactory.createConnection();			
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(queue);
            oMsg = session.createObjectMessage();
            message = session.createTextMessage();
            
            /*MessageConsumer consumer = session.createConsumer(dest);
            TaskInfoListener listener = new TaskInfoListener();
            consumer.setMessageListener(listener);
            connection.start();*/
            
            System.out.println("Setup finished, waiting for commands.");

            stdIn = new BufferedReader(new InputStreamReader(System.in));
            String command;        
            try {
    			while((command = stdIn.readLine()) != null) {
    				if (command.startsWith("assign ")) {
    					String[] params = command.split(" ");
    					if (params.length == 2) {
    						Long jobId = Long.parseLong(params[1]);
    						
    						
    						// advise server to create new task
    						AssignJobDto assignJob = new AssignJobDto(jobId);
    						
    						oMsg.setObject(assignJob);
    			            messageProducer.send(oMsg);
    						
    						/*for (int i = 0; i < 10; i++) {
    			                message.setText("This is message " + (i + 1));
    			                System.out.println("Sending message: " + message.getText());
    			                messageProducer.send(message);
    			            }*/
    						
    						// receive taskId
    						System.out.println("TaskId: ");
    					}
    				}
    				else if(command.startsWith("info ")) {
    					String[] params = command.split(" ");
    					if (params.length == 2) {
    						Long taskId = Long.parseLong(params[1]);
    						
    						// advise server to send information about this task
    						
    						// receive information
    						System.out.println("Task information: ");
    					}
    			    }
    				else if(command.startsWith("stop")) {
    			    	break;
    			    }
    			}
    		} catch (IOException e) {
    			System.out.println("Error reading from command line.");
    		}
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
            System.exit(0);
        }
	}
}
