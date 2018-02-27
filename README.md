# java-programs

#### java_AQJMS
Java program to enqueue/dequeue messages into/from a Queue (multi user queue)

First of all create a new type, used later from the queue to create new messages.
```
CREATE OR REPLACE TYPE "QUEUE_MESSAGE_TYPE" AS OBJECT (
  no NUMBER,
  title VARCHAR2(40),
  text VARCHAR2(3000)
  );
```
Find and configure your data connection inside java classes. Also configure your queue in the required methods, for example:
```
Topic topic = ((AQjmsSession) aq.session).getTopic(
            DB_AQ_ADMIN_NAME,     // Queue owner
            "QUEUE_NAME");       // Queue name
```
*Compiling Java program*
Export path to include JDK path:
```
$ export PATH=$PATH:/u01/app/oracle/product/11.2.0/dbhome_1/jdk/bin
```
Export CLASSPATH to include required classes.
```
$ export CLASSPATH=$CLASSPATH:/u01/app/oracle/product/11.2.0/dbhome_1/jdbc/lib/ojdbc5.jar:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/translator.jar:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/runtime12.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/aqapi.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jmscommon.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jta.jar
```
Create Java Class from Oracle object type created previously, using Oracle JPublisher Tool.
```
$ /u01/app/oracle/product/11.2.0/dbhome_1/jdk/bin/java oracle.jpub.Doit -user=TESTDB/PASSWORD -sql=QUEUE_MESSAGE_TYPE -usertypes=oracle -methods=false
```
Compile Java program
```
$ javac -deprecation *.java
```

*Executing Java program*
Enqueue messages
```
$ java -cp .:/u01/app/oracle/product/11.2.0/dbhome_1/jdbc/lib/ojdbc5.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/aqapi.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jmscommon.jar:/var/tmp/java_AQJMS:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/translator.jar:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/runtime12.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jta.jar AQJmsPublisher
```
Dequeue messages
```
$ java -cp .:/u01/app/oracle/product/11.2.0/dbhome_1/jdbc/lib/ojdbc5.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/aqapi.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jmscommon.jar:/var/tmp/java_AQJMS:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/translator.jar:/u01/app/oracle/product/11.2.0/dbhome_1/sqlj/lib/runtime12.jar:/u01/app/oracle/product/11.2.0/dbhome_1/jlib/jta.jar AQJmsSubscriber 0
```
