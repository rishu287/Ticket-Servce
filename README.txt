This is a solution for simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats .
Assumptions made :
The Size of the Hall is 100
The price of Each ticket is 100.
The time interval after which holded seats are removed is 10000 milliseconds.
All the above parameters are configurable using a properties file .
No log4j configuration / logging mechanism is implementated. I have used a custom Exception class to create System defined errors and System.out.println in rare occasions.  

Run the following command to run and execute the project :
git clone https://github.com/rishu287/Ticket-Servce.git
mvn clean install

If you want to run indivdual test cases , you can use maven to do that . For ex if you want to run testFindAndHoldSeats in test Class TestTicketService  :
 mvn -Dtest=TestTicketService#testFindAndHoldSeats test  
The project can be imported in a Java editor like Eclipse / Net Beans and test cases then be executed .

There is also a plugin added to include test coverage which is Jacoco. It will run as part of the maven test phase and generate report . The report can be
viewed under target\site\jacoco\index.html.

All my test cases and application code  is  following a single threaded model i.e when each test case runs it creates a new Object of TicketService API and thus
has its own set of holded seats and reserved seats . The design will entirely change if I go with assumption that the test cases can be executed in parrallal with multiple
thread execution