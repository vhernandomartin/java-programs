import java.sql.*;

import oracle.AQ.*;

public class AQDequeue extends AQApplication {

	/** Main program that starts this application

		@param arguments Arguments passed at program start
	*/
	static public void main(String[] arguments) {
    AQDequeue application = new AQDequeue(arguments);
	}

	/** Constructor

		@param arguments Arguments passed at program start
	*/
	protected AQDequeue(String[] arguments) {
		try {
    	NativeAQ aq = createNativeClient();
      doTest(aq);
		}
		catch (Exception ex) {
			System.err.println("AQDequeue.AQDequeue(): " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/** Test method

		@param aq Used AQ connection and session

  	@exception oracle.AQ.AQException This exception is raised when the user
      encounters any error while using the Java AQ API
  	@exception java.sql.SQLException JDBC SQL exceptions
	*/
	protected void doTest(NativeAQ aq) throws AQException, SQLException {

		// Get a handle to a queue
		AQQueue queue = aq.session.getQueue(
                DB_AQ_ADMIN_NAME,         // Queue owner
                "QUEUE_NAME");         // Queue name
		System.out.println("getQueue() successfully");

		// Get default dequeue options
		AQDequeueOption dequeueOption = new AQDequeueOption();

    // Dequeue
		System.out.println("Waiting for message to dequeue...");
		AQMessage message = ((AQOracleQueue)queue).dequeue(
                dequeueOption,            // Options and paramters to dequeue
                                          // Passes instance of QUEUE_MESSAGE_TYPE
								//QUEUE_MESSAGE_TYPE.getFactory());
								QUEUE_MESSAGE_TYPE.getORADataFactory());
		System.out.println("dequeue() successfully");

		// Read used message type out of raw payload data
    // A structured message queue entry is called payload
    // Typically an Oracle type defined AS OBJECT with a couple of fields is used
		AQObjectPayload    payload     = message.getObjectPayload();
		QUEUE_MESSAGE_TYPE messageData = (QUEUE_MESSAGE_TYPE) payload.getPayloadData();

		// Print message
		System.out.println("Dequeued no:    " + messageData.getNo());
		System.out.println("Dequeued title: " + messageData.getTitle());
		System.out.println("Dequeued text:  " + messageData.getText());

		aq.connection.commit();
	}

} // End of class AQDequeue
