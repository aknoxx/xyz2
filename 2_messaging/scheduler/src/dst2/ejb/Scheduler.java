package dst2.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.annotation.Resource;

import dst2.ejb.dto.AssignJobDto;

public class Scheduler {

	@Resource(lookup = "Factory")
    private static ConnectionFactory connectionFactory;
    @Resource(lookup = "MyQueue")
    private static Queue queue;
    
	private static BufferedReader stdIn;
	
	public static void main(String[] args) {
	
		//Destination dest = (Destination) queue;
		Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        TextMessage message = null;
		
		System.out.println("Cheduler: ");

		try {
			System.out.println("Cheduler0: ");
			connection = connectionFactory.createConnection();
			System.out.println("Cheduler1: ");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Cheduler2: ");
            messageProducer = session.createProducer(queue);
            System.out.println("Cheduler3: ");
            message = session.createTextMessage();
            System.out.println("Cheduler4: ");
            
            /*MessageConsumer consumer = session.createConsumer(dest);
            TaskInfoListener listener = new TaskInfoListener();
            consumer.setMessageListener(listener);
            connection.start();*/
            
            
            
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
    						
    						for (int i = 0; i < 10; i++) {
    			                message.setText("This is message " + (i + 1));
    			                System.out.println("Sending message: " + message.getText());
    			                messageProducer.send(message);
    			            }
    						
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
