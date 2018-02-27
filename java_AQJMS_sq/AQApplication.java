import java.sql.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;

import oracle.AQ.*;
import oracle.jms.*;


abstract public class AQApplication {

	private   final String DB_CONNECTION 				= "jdbc:oracle:thin:@localhost:1521:TESTDB";
	protected final String DB_AQ_ADMIN_NAME 		= "TESTUSER";
	private   final String DB_AQ_ADMIN_PASSWORD = "PASSWORD";

	private   final String DB_AQ_USER_NAME 			= "TESTUSER";
	private   final String DB_AQ_USER_PASSWORD 	= "PASSWORD";

	/** Native AQ references, DB connection for transaction handling and AQ session */
  protected class NativeAQ {
  	java.sql.Connection connection = null;
  	AQSession           session = null;
  }

	/** JMS AQ references, topic connection and AQ session */
  protected class JMSQueue {
  	QueueConnection     connection = null;
  	QueueSession        session = null;
  }


	/** Creates a native AQ administrator connection and session
		@return AQ connection and session reference
	*/
	protected NativeAQ createNativeAdmin() {
    return createNative(DB_CONNECTION, DB_AQ_ADMIN_NAME, DB_AQ_ADMIN_PASSWORD);
	}

	/** Creates a native AQ user connection and session
		@return AQ connection and session reference
	*/
	protected NativeAQ createNativeClient() {
    return createNative(DB_CONNECTION, DB_AQ_USER_NAME, DB_AQ_USER_PASSWORD);
	}

	/** Creates a JMS AQ administrator connection and session
		@return AQ connection and session reference
	*/
	protected JMSQueue createJMSAdmin() {
    return createJMS(DB_CONNECTION, DB_AQ_ADMIN_NAME, DB_AQ_ADMIN_PASSWORD);
	}

	/** Creates a JMS AQ user connection and session
		@return AQ connection and session reference
	*/
	protected JMSQueue createJMSClient() {
    return createJMS(DB_CONNECTION, DB_AQ_USER_NAME, DB_AQ_USER_PASSWORD);
	}


	private NativeAQ createNative(
              String connectString, String userName, String userPassword) {

		NativeAQ aq = new NativeAQ();

		try {

			// Loads the Oracle JDBC driver
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// DB connection
			aq.connection = DriverManager.getConnection(
						connectString, userName, userPassword);
			aq.connection.setAutoCommit(false);
			System.out.println("JDBC connection with user '" + userName
            + "' and password '" + userPassword + "' opened to: " + connectString);

			// Loads the Oracle AQ driver
			Class.forName("oracle.AQ.AQOracleDriver");

			// Creates an AQ session
			aq.session = AQDriverManager.createAQSession(aq.connection);
			System.out.println("Successfully created AQ session");
		}
		catch (Exception ex) {
			System.err.println("AQApplication.createNative(): " + ex.getMessage());
			System.err.println(
        "user = " + userName + ", password = " + userPassword + ", to = " + connectString);
			ex.printStackTrace();
		}

		return aq;
	}


	/** Creates a JMS AQ connection and session.

		@return AQ connection and session reference
	*/
	private JMSQueue createJMS(
              String connectString, String userName, String userPassword) {

		JMSQueue aq = new JMSQueue();

		try {

      QueueConnectionFactory queueConnectionFactory = null;

      // Get topic connection factory
      Properties info = new Properties();
      info.put(userName, userPassword);
      queueConnectionFactory = AQjmsFactory.getQueueConnectionFactory(connectString, info);
			System.out.println("JDBC connection with user '" + userName
            + "' and password '" + userPassword + "' opened to: " + connectString);

			// Creates an AQ topic connection and session

      aq.connection = queueConnectionFactory.createQueueConnection(userName, userPassword);
      // If a session is transacted, message acknowledgment is handled automatically
      //   by commit and recovery is handled automatically by rollback
      aq.session = aq.connection.createQueueSession(
            true,                         // Session is transacted
            Session.CLIENT_ACKNOWLEDGE);  // Acknowledges by commit and rollback
			System.out.println("Successfully created AQ session");
		}
		catch (Exception ex) {
			System.err.println("AQApplication.createJMS(): " + ex.getMessage());
			System.err.println(
        "user = " + userName + ", password = " + userPassword + ", destination = " + connectString);
			ex.printStackTrace();
		}

		return aq;
	}

} // End of class AQApplication
